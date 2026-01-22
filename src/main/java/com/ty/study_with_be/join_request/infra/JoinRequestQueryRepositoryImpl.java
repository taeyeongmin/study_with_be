package com.ty.study_with_be.join_request.infra;

import com.ty.study_with_be.join_request.query.repository.JoinRequestQueryRepository;
import com.ty.study_with_be.study_group.domain.model.enums.JoinRequestStatus;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

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
}
