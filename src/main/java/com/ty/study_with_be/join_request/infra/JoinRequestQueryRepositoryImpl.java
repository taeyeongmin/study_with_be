package com.ty.study_with_be.join_request.infra;

import com.ty.study_with_be.join_request.application.query.JoinRequestQueryRepository;
import com.ty.study_with_be.join_request.domain.model.enums.JoinRequestStatus;
import com.ty.study_with_be.join_request.presentation.query.dto.JoinRequestListItem;
import com.ty.study_with_be.join_request.presentation.query.dto.MyRequestListItem;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
                        then 'PENDING'
                    when com.ty.study_with_be.join_request.domain.model.enums.JoinRequestStatus.APPROVED
                        then 'APPROVED'
                    when com.ty.study_with_be.join_request.domain.model.enums.JoinRequestStatus.REJECTED
                        then 'REJECTED'
                    else 'CANCELED'
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

    @Override
    public Page<MyRequestListItem> findMyRequests(Long memberId, JoinRequestStatus status, Pageable pageable) {

        StringBuilder where = new StringBuilder(" where jr.requesterId = :memberId ");
        Map<String, Object> params = new HashMap<>();
        params.put("memberId", memberId);

        if (status != null) {
            where.append(" and jr.status = :status ");
            params.put("status", status);
        }

        String contentQuery = """
            select new com.ty.study_with_be.join_request.presentation.query.dto.MyRequestListItem(
                jr.joinRequestId,
                sg.studyGroupId,
                sg.title,
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
                jr.createdAt,
                jr.processedAt
            )
            from JoinRequest jr
            join StudyGroup sg
              on sg.studyGroupId = jr.studyGroupId
        """ + where + """
            order by jr.createdAt desc
        """;

        TypedQuery<MyRequestListItem> typedContentQuery = em.createQuery(contentQuery, MyRequestListItem.class);
        params.forEach(typedContentQuery::setParameter);
        typedContentQuery.setFirstResult((int) pageable.getOffset());
        typedContentQuery.setMaxResults(pageable.getPageSize());
        List<MyRequestListItem> content = typedContentQuery.getResultList();

        String countQuery = """
            select count(jr.joinRequestId)
            from JoinRequest jr
        """ + where;

        TypedQuery<Long> typedCountQuery = em.createQuery(countQuery, Long.class);
        params.forEach(typedCountQuery::setParameter);
        long total = typedCountQuery.getSingleResult();

        return new PageImpl<>(content, pageable, total);
    }
}
