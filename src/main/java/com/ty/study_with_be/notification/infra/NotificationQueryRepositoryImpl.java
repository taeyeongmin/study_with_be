package com.ty.study_with_be.notification.infra;

import com.ty.study_with_be.notification.application.query.NotificationQueryRepository;
import com.ty.study_with_be.notification.presentation.query.dto.NotificationItem;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

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
}
