package com.ty.study_with_be.global.outbox.infra.repository;

import com.ty.study_with_be.global.outbox.domain.OutboxEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OutboxEventRepository extends JpaRepository<OutboxEvent, Long> {

    /**
     * 처리할 이벤트 batch를 락 걸고 조회.
     * - 여러 워커/인스턴스가 동시에 돌아도 SKIP LOCKED로 중복 처리 방지
     * - PENDING / FAILED(재시도 가능) 중 next_retry_at <= now 만 대상
     */
    @Query(value = """
        select *
        from outbox_event
        where status in ('PENDING', 'FAILED')
          and (next_retry_at is null or next_retry_at <= now())
        order by id asc
        limit :limit
        for update skip locked
        """, nativeQuery = true)
    List<OutboxEvent> lockBatchForProcessing(@Param("limit") int limit);

    /**
     * PROCESSING 상태로 오래 묶인 row를 회수한다.
     * (서버 다운/강제 종료 등으로 PROCESSING에서 멈춘 케이스 복구)
     */
    @Query(value = """
        select *
        from outbox_event
        where status = 'PROCESSING'
          and updated_at < date_sub(now(), interval :staleSeconds second)
        order by id asc
        limit :limit
        for update skip locked
        """, nativeQuery = true)
    List<OutboxEvent> lockStaleProcessing(@Param("staleSeconds") int staleSeconds,
                                          @Param("limit") int limit);
}
