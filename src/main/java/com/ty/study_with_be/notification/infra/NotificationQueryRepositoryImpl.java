package com.ty.study_with_be.notification.infra;

import com.ty.study_with_be.notification.application.query.NotificationQueryRepository;
import com.ty.study_with_be.notification.presentation.query.dto.NotificationItem;
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

@Repository
@RequiredArgsConstructor
public class NotificationQueryRepositoryImpl implements NotificationQueryRepository {

    private final EntityManager em;

    @Override
    public int countNotReadByMemberId(Long memberId) {

        Long count = em.createQuery("""
              select count(noti)
              from Notification noti
              where noti.recipientMemberId = :memberId
                and noti.readAt is null
              """, Long.class)
                .setParameter("memberId", memberId)
                .getSingleResult();

        return Math.toIntExact(count);
    }

    @Override
    public List<NotificationItem> findNotReadByMemberId(Long memberId) {

    return em.createQuery("""
        select new com.ty.study_with_be.notification.presentation.query.dto.NotificationItem(
            noti.id,
            sg.studyGroupId,
            sg.title,
            noti.recipientMemberId,
            noti.type,
            null,
            noti.content,
            noti.readAt,
            noti.createdAt
        )
        from Notification noti
        left join StudyGroup sg
            on noti.studyGroupId = sg.studyGroupId
        where noti.recipientMemberId = :memberId
            and noti.readAt is null
        order by noti.createdAt desc
        """, NotificationItem.class)
                .setParameter("memberId", memberId)
                .getResultList();
    }

    @Override
    public Page<NotificationItem> findMyNotifications(Long memberId, Boolean unreadOnly, Pageable pageable) {

        StringBuilder where = new StringBuilder(" where noti.recipientMemberId = :memberId ");
        Map<String, Object> params = new HashMap<>();
        params.put("memberId", memberId);

        if (unreadOnly != null) {
            if (unreadOnly) {
                where.append(" and noti.readAt is null ");
            } else {
                where.append(" and noti.readAt is not null ");
            }
        }

        String contentQuery = """
        select new com.ty.study_with_be.notification.presentation.query.dto.NotificationItem(
            noti.id,
            sg.studyGroupId,
            sg.title,
            noti.recipientMemberId,
            noti.type,
            null,
            noti.content,
            noti.readAt,
            noti.createdAt
        )
        from Notification noti
        left join StudyGroup sg
            on noti.studyGroupId = sg.studyGroupId
        """ + where + """
        order by noti.createdAt desc
        """;

        TypedQuery<NotificationItem> typedContentQuery =
                em.createQuery(contentQuery, NotificationItem.class);
        params.forEach(typedContentQuery::setParameter);
        typedContentQuery.setFirstResult((int) pageable.getOffset());
        typedContentQuery.setMaxResults(pageable.getPageSize());

        List<NotificationItem> content = typedContentQuery.getResultList();

        String countQuery = """
        select count(noti.id)
        from Notification noti
        """ + where;

        TypedQuery<Long> typedCountQuery = em.createQuery(countQuery, Long.class);
        params.forEach(typedCountQuery::setParameter);
        long total = typedCountQuery.getSingleResult();

        return new PageImpl<>(content, pageable, total);
    }
}
