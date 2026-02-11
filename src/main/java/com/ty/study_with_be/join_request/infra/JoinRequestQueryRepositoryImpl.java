package com.ty.study_with_be.join_request.infra;

import com.ty.study_with_be.join_request.application.query.JoinRequestQueryRepository;
import com.ty.study_with_be.join_request.domain.model.enums.JoinRequestStatus;
import com.ty.study_with_be.join_request.presentation.query.dto.JoinRequestListItem;
import com.ty.study_with_be.study_group.domain.model.enums.OperationStatus;
import com.ty.study_with_be.study_group.domain.model.enums.StudyRole;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class JoinRequestQueryRepositoryImpl implements JoinRequestQueryRepository {

    private final EntityManager em;

    @Override
    public boolean existsPendingJoin(Long studyGroupId, Long memberId) {

        Long count = em.createQuery("""
            select count(jr)
            from JoinRequest jr
            where jr.studyGroupId = :studyGroupId
              and jr.requesterId = :memberId
              and jr.status = :status
        """, Long.class)
                .setParameter("studyGroupId", studyGroupId)
                .setParameter("memberId", memberId)
                .setParameter("status", JoinRequestStatus.PENDING)
                .getSingleResult();

        return count > 0;
    }

    @Override
    public Optional<Long> findPendingJoinRequestId(Long studyGroupId, Long memberId) {

        return em.createQuery("""
            select jr.joinRequestId
            from JoinRequest jr
            where jr.studyGroupId = :studyGroupId
              and jr.requesterId = :memberId
              and jr.status = :status
            order by jr.createdAt desc
        """, Long.class)
                .setParameter("studyGroupId", studyGroupId)
                .setParameter("memberId", memberId)
                .setParameter("status", JoinRequestStatus.PENDING)
                .setMaxResults(1)
                .getResultStream()
                .findFirst();
    }

    @Override
    public List<JoinRequestListItem> findJoinRequests(Long studyGroupId, JoinRequestStatus status) {

        return em.createQuery("""
            select new com.ty.study_with_be.join_request.presentation.query.dto.JoinRequestListItem(
                jr.joinRequestId,
                m.memberId,
                m.loginId,
                m.nickname,
                jr.status,
                case jr.status
                    when com.ty.study_with_be.join_request.domain.model.enums.JoinRequestStatus.PENDING
                        then '대기중'
                    when com.ty.study_with_be.join_request.domain.model.enums.JoinRequestStatus.APPROVED
                        then '승인'
                    when com.ty.study_with_be.join_request.domain.model.enums.JoinRequestStatus.REJECTED
                        then '거절'
                    else '취소'
                end,
                jr.createdAt
            )
            from JoinRequest jr
            join Member m
              on m.memberId = jr.requesterId
            where jr.studyGroupId = :studyGroupId
              and jr.status = :status
            order by jr.createdAt desc
        """, JoinRequestListItem.class)
                .setParameter("studyGroupId", studyGroupId)
                .setParameter("status", status)
                .getResultList();
    }

    @Override
    public int countByMemberIdPending(Long memberId) {

        Long count = em.createQuery("""
            select count(jr)
            from JoinRequest jr
            where jr.requesterId = :memberId
                and jr.status = :status
        """, Long.class)
                .setParameter("memberId", memberId)
                .setParameter("status", JoinRequestStatus.PENDING)
                .getSingleResult();

        return count.intValue();
    }


}
