package com.ty.study_with_be.join_request.application.command.service;

import com.ty.study_with_be.global.error.ErrorCode;
import com.ty.study_with_be.global.exception.DomainException;
import com.ty.study_with_be.join_request.domain.JoinRequestRepository;
import com.ty.study_with_be.join_request.domain.model.JoinRequest;
import com.ty.study_with_be.join_request.domain.model.enums.JoinRequestStatus;
import com.ty.study_with_be.member.domain.model.Member;
import com.ty.study_with_be.member.infra.MemberJpaRepository;
import com.ty.study_with_be.study_group.domain.GroupRepository;
import com.ty.study_with_be.study_group.domain.model.StudyGroup;
import com.ty.study_with_be.study_group.domain.model.enums.StudyMode;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hibernate.validator.internal.util.Contracts.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doAnswer;

@SpringBootTest
@ActiveProfiles("test")
class ProcessJoinRequestServiceTest {

    @MockitoSpyBean
    private ProcessJoinRequestService processJoinRequestService;
    @Autowired
    private GroupRepository groupRepository;
    @Autowired
    private JoinRequestRepository joinRequestRepository;
    @Autowired
    private MemberJpaRepository memberJpaRepository;
    @Autowired
    private EntityManager em;

    @Test
    void 동시에_승인되어_정원_초과() throws Exception {
        // 테스트 데이터 준비
        // 방장 1명 + 기존 멤버 3명을 미리 가입시켜 currentCount=4 상태를 만든다.
        // 이후 가입요청 2건을 동시에 승인하면 currentCount가 6까지 증가할 수 있다.
        Member owner = saveMember("owner");
        Member m2 = saveMember("m2");
        Member m3 = saveMember("m3");
        Member m4 = saveMember("m4");
        Member r1 = saveMember("r1");
        Member r2 = saveMember("r2");

        // capacity=5, 현재 멤버 4명(방장 포함) 상태로 세팅
        StudyGroup group = StudyGroup.create(
                "title", "category", "topic", "region",
                StudyMode.ONLINE, 5, "desc", null, null, owner.getMemberId()
        );
        group.joinMember(m2.getMemberId(),1L);
        group.joinMember(m3.getMemberId(),1L);
        group.joinMember(m4.getMemberId(),1L);
        groupRepository.save(group);

        // 가입요청 2건 생성 (동시 승인 대상)
        JoinRequest jr1 = joinRequestRepository.save(JoinRequest.create(group.getStudyGroupId(), r1.getMemberId()));
        JoinRequest jr2 = joinRequestRepository.save(JoinRequest.create(group.getStudyGroupId(), r2.getMemberId()));

        // 동시성 재현 준비
        // ready: 각 스레드가 출발선에 도착했는지 확인
        // start: 두 스레드를 같은 순간에 출발시키는 신호
        CountDownLatch ready = new CountDownLatch(2);
        CountDownLatch start = new CountDownLatch(1);
        ExecutorService pool = Executors.newFixedThreadPool(2);

        // Lock 처리가 안되게 처리 일반 조회 쿼리 조회 => Lock 되면 동시 호출이 안되니
        doAnswer(inv -> groupRepository.findById(inv.getArgument(0)))
                .when(processJoinRequestService).getStudyGroup(anyLong());

        Future<?> f1 = pool.submit(() -> {
            System.err.println("쓰레드1 실행 : "+System.currentTimeMillis());
            // thread_1 준비 완료 표시
            ready.countDown();
            // start 신호를 받을 때까지 대기
            await(start);
            // thread_1: 승인 처리
            System.err.println("쓰레드1 실제 출발 : "+System.currentTimeMillis());
            processJoinRequestService.process(group.getStudyGroupId(), jr1.getJoinRequestId(), owner.getMemberId(), JoinRequestStatus.APPROVED);
        });
        Future<?> f2 = pool.submit(() -> {
            // 해당 코드가 있어도 실제 thread_2가 준비될 때까지 실행을 대기
            sleep(300);

            System.err.println("쓰레드2 실행 : "+System.currentTimeMillis());

            // thread_2 준비 완료 표시
            ready.countDown();
            // start 신호를 받을 때까지 대기
            await(start);
            // thread_2 승인 처리
            // thread_1과  동일하게 currentCount=4를 기준으로 +1 처리할 수 있다.
            System.err.println("쓰레드2 실제 출발 : "+System.currentTimeMillis());
            processJoinRequestService.process(group.getStudyGroupId(), jr2.getJoinRequestId(), owner.getMemberId(), JoinRequestStatus.APPROVED);
        });

        // 두 스레드를 동시에 출발
        // ready.await()는 동시 시작을 보장
        ready.await();
        start.countDown();
        f1.get();
        f2.get();
        pool.shutdown();

        // 실제 멤버 수가 capacity를 초과했는지 확인
        // 영속성 컨텍스트 초기화 (DB 최신 상태 재조회 목적)
        em.clear();

        StudyGroup studyGroup = em.createQuery("""
              select sg
              from StudyGroup sg
              left join fetch sg.members
              where sg.studyGroupId = :id
          """, StudyGroup.class)
                .setParameter("id", group.getStudyGroupId())
                .getSingleResult();

        // 현재원이 최대 정원보다 큰지 확인
        assertThat(studyGroup.getMembers().size()).isGreaterThan(studyGroup.getCapacity());
    }

    @Test
    void 동시에_승인_LOCK처리_초과예외발생() throws Exception {
        // 테스트 데이터 준비
        // 방장 1명 + 기존 멤버 3명을 미리 가입시켜 currentCount=4 상태를 만든다.
        // 이후 가입요청 2건을 동시에 승인하면 currentCount가 6까지 증가할 수 있다.
        Member owner = saveMember("owner");
        Member m2 = saveMember("m2");
        Member m3 = saveMember("m3");
        Member m4 = saveMember("m4");
        Member r1 = saveMember("r1");
        Member r2 = saveMember("r2");

        // capacity=5, 현재 멤버 4명(방장 포함) 상태로 세팅
        StudyGroup group = StudyGroup.create(
                "title", "category", "topic", "region",
                StudyMode.ONLINE, 5, "desc", null, null, owner.getMemberId()
        );
        group.joinMember(m2.getMemberId(),1L);
        group.joinMember(m3.getMemberId(),1L);
        group.joinMember(m4.getMemberId(),1L);
        groupRepository.save(group);

        // 가입요청 2건 생성 (동시 승인 대상)
        JoinRequest jr1 = joinRequestRepository.save(JoinRequest.create(group.getStudyGroupId(), r1.getMemberId()));
        JoinRequest jr2 = joinRequestRepository.save(JoinRequest.create(group.getStudyGroupId(), r2.getMemberId()));

        // 동시성 재현 준비
        // ready: 각 스레드가 출발선에 도착했는지 확인
        // start: 두 스레드를 같은 순간에 출발시키는 신호
        CountDownLatch ready = new CountDownLatch(2);
        CountDownLatch start = new CountDownLatch(1);
        ExecutorService pool = Executors.newFixedThreadPool(2);

        Future<?> f1 = pool.submit(() -> {
            System.err.println("쓰레드1 실행 : "+System.currentTimeMillis());
            // thread_1 준비 완료 표시
            ready.countDown();
            // start 신호를 받을 때까지 대기
            await(start);
            // thread_1: 승인 처리
            System.err.println("쓰레드1 실제 출발 : "+System.currentTimeMillis());
            processJoinRequestService.process(group.getStudyGroupId(), jr1.getJoinRequestId(), owner.getMemberId(), JoinRequestStatus.APPROVED);
        });
        Future<?> f2 = pool.submit(() -> {
            // 해당 코드가 있어도 실제 thread_2가 준비될 때까지 실행을 대기
            sleep(300);

            System.err.println("쓰레드2 실행 : "+System.currentTimeMillis());

            // thread_2 준비 완료 표시
            ready.countDown();
            // start 신호를 받을 때까지 대기
            await(start);
            // thread_2 승인 처리
            // thread_1과  동일하게 currentCount=4를 기준으로 +1 처리할 수 있다.
            System.err.println("쓰레드2 실제 출발 : "+System.currentTimeMillis());
            processJoinRequestService.process(group.getStudyGroupId(), jr2.getJoinRequestId(), owner.getMemberId(), JoinRequestStatus.APPROVED);
        });

        // 두 스레드를 동시에 출발
        // ready.await()는 동시 시작을 보장
        ready.await();
        start.countDown();

        pool.shutdown();

        ExecutionException ex1 = null;
        ExecutionException ex2 = null;

        try { f1.get(); } catch (ExecutionException e) { ex1 = e; }
        try { f2.get(); } catch (ExecutionException e) { ex2 = e; }

        assertTrue(ex1 != null || ex2 != null, "둘 중 하나는 예외 발생해야 함");
        ExecutionException ex = (ex1 != null) ? ex1 : ex2;
        assertThat(ex.getCause()).isInstanceOf(DomainException.class);
        assertThat(((DomainException) ex.getCause()).getErrorCode())
                .isEqualTo(ErrorCode.CAPACITY_EXCEEDED);
    }

    @Test
    void 동시에_승인X_정원초과_예외발생() throws Exception {
        // 테스트 데이터 준비
        // 방장 1명 + 기존 멤버 3명을 미리 가입시켜 currentCount=4 상태를 만든다.
        // 이후 가입요청 2건을 동시에 승인하면 currentCount가 6까지 증가할 수 있다.
        Member owner = saveMember("owner");
        Member m2 = saveMember("m2");
        Member m3 = saveMember("m3");
        Member m4 = saveMember("m4");
        Member r1 = saveMember("r1");
        Member r2 = saveMember("r2");

        // capacity=5, 현재 멤버 4명(방장 포함) 상태로 세팅
        StudyGroup group = StudyGroup.create(
                "title", "category", "topic", "region",
                StudyMode.ONLINE, 5, "desc", null, null, owner.getMemberId()
        );
        group.joinMember(m2.getMemberId(),1L);
        group.joinMember(m3.getMemberId(),1L);
        group.joinMember(m4.getMemberId(),1L);
        groupRepository.save(group);

        // 가입요청 2건 생성 (동시 승인 대상)
        JoinRequest jr1 = joinRequestRepository.save(JoinRequest.create(group.getStudyGroupId(), r1.getMemberId()));
        JoinRequest jr2 = joinRequestRepository.save(JoinRequest.create(group.getStudyGroupId(), r2.getMemberId()));

        // 동시성 재현 준비

        // 두 개의 스레드 생성
        ExecutorService pool = Executors.newFixedThreadPool(2);

        Future<?> f1 = pool.submit(() -> {
            System.err.println("쓰레드1 실행 : "+System.currentTimeMillis());

            // thread_1 승인 처리
            System.err.println("쓰레드1 실제 출발 : "+System.currentTimeMillis());
            processJoinRequestService.process(group.getStudyGroupId(), jr1.getJoinRequestId(), owner.getMemberId(), JoinRequestStatus.APPROVED);
        });
        Future<?> f2 = pool.submit(() -> {

            // 순차적인 실행을 보장하기 위해
            sleep(300);

            // thread_2 승인 처리
            // thread_1과  동일하게 currentCount=4를 기준으로 +1 처리할 수 있다.
            System.err.println("쓰레드2 실제 출발 : "+System.currentTimeMillis());
            processJoinRequestService.process(group.getStudyGroupId(), jr2.getJoinRequestId(), owner.getMemberId(), JoinRequestStatus.APPROVED);
        });

        // 두 스레드를 순차적으로 출발
        // ready.await() 를 제거하여 기다리지 않고 바로 다음 스레드 실행
        f1.get();

        // 두 번쨰 호출 시 정상적으로 정원이 초과되어 예외 발생
        ExecutionException ex = assertThrows(ExecutionException.class, f2::get);
        assertThat(ex.getCause()).isInstanceOf(DomainException.class);
        assertThat(((DomainException) ex.getCause()).getErrorCode())
                .isEqualTo(ErrorCode.CAPACITY_EXCEEDED);

    }

    @Test
    void pessimistic_lock_20_concurrent_approvals_block_oversubscribe() throws Exception {
        Member owner = saveMember("owner_lock");
        Member m2 = saveMember("m2_lock");
        Member m3 = saveMember("m3_lock");
        Member m4 = saveMember("m4_lock");

        StudyGroup group = StudyGroup.create(
                "title_lock", "category", "topic", "region",
                StudyMode.ONLINE, 5, "desc", null, null, owner.getMemberId()
        );
        group.joinMember(m2.getMemberId(),1L);
        group.joinMember(m3.getMemberId(),1L);
        group.joinMember(m4.getMemberId(),1L);
        groupRepository.save(group);

        JoinRequest[] requests = new JoinRequest[20];
        for (int i = 0; i < 20; i++) {
            Member requester = saveMember("req_lock_" + i);
            requests[i] = joinRequestRepository.save(
                    JoinRequest.create(group.getStudyGroupId(), requester.getMemberId())
            );
        }

        CountDownLatch ready = new CountDownLatch(20);
        CountDownLatch start = new CountDownLatch(1);
        ExecutorService pool = Executors.newFixedThreadPool(20);

        AtomicInteger success = new AtomicInteger();
        AtomicInteger capacityExceeded = new AtomicInteger();

        System.err.println(">>>>>>> 시작 시간 :"+System.currentTimeMillis());
        List<Future<?>> futures = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            JoinRequest jr = requests[i];
            futures.add(pool.submit(() -> {
                ready.countDown();
                await(start);
                try {
                    processJoinRequestService.process(
                            group.getStudyGroupId(),
                            jr.getJoinRequestId(),
                            owner.getMemberId(),
                            JoinRequestStatus.APPROVED
                    );
                    success.incrementAndGet();
                } catch (DomainException ex) {
                    if (ex.getErrorCode() == ErrorCode.CAPACITY_EXCEEDED) {
                        capacityExceeded.incrementAndGet();
                    } else {
                        throw ex;
                    }
                }
            }));
        }

        ready.await();
        start.countDown();
        for (Future<?> f : futures) {
            try {
                f.get();
            } catch (ExecutionException ex) {
                if (ex.getCause() instanceof DomainException) {
                    throw (DomainException) ex.getCause();
                }
                throw ex;
            }
        }
        pool.shutdown();
        System.err.println(">>>>>>> 종료 시간 :"+System.currentTimeMillis());

        em.clear();
        StudyGroup studyGroup = em.createQuery("""
              select sg
              from StudyGroup sg
              left join fetch sg.members
              where sg.studyGroupId = :id
          """, StudyGroup.class)
                .setParameter("id", group.getStudyGroupId())
                .getSingleResult();

        assertThat(success.get()).isEqualTo(1);
        assertThat(capacityExceeded.get()).isEqualTo(19);
        assertThat(studyGroup.getMembers().size()).isLessThanOrEqualTo(studyGroup.getCapacity());
    }

    // 테스트용 멤버 저장 헬퍼
    private Member saveMember(String loginId) {
        return memberJpaRepository.save(Member.createLocalMember(loginId, "pw", loginId));
    }

    // CountDownLatch 대기 헬퍼 (인터럽트 처리 포함)
    private void await(CountDownLatch latch) {
        try {
            latch.await();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private void sleep(long ms) {
        try { Thread.sleep(ms); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
    }
}
