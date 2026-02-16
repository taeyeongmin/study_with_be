package com.ty.study_with_be.notification.application.query;

import com.ty.study_with_be.notification.presentation.query.dto.NotificationItem;
import com.ty.study_with_be.notification.presentation.query.dto.MyNotificationListReq;
import com.ty.study_with_be.notification.presentation.query.dto.MyNotificationListRes;
import com.ty.study_with_be.notification.presentation.query.dto.MyNotificationReadFilter;
import com.ty.study_with_be.notification.presentation.query.dto.NotificationListRes;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NotificationQueryService {

    private final NotificationQueryRepository notificationQueryRepository;

    public NotificationListRes getMyNotiList(Long memberId) {

        List<NotificationItem> list = notificationQueryRepository.findNotReadByMemberId(memberId);
        list.forEach(notificationItem -> notificationItem.setEventTypeNm(notificationItem.getType().getEventName()));

        return new NotificationListRes(list, list.size());
    }

    public int countByMemberId(Long memberId) {
        return notificationQueryRepository.countNotReadByMemberId(memberId);
    }

    public MyNotificationListRes getMyNotificationList(
            Long memberId,
            MyNotificationListReq request,
            Pageable pageable
    ) {
        Boolean unreadOnly = toUnreadOnly(request.getReadFilter());

        Page<NotificationItem> page = notificationQueryRepository.findMyNotifications(memberId, unreadOnly, pageable);
        page.getContent().forEach(item -> item.setEventTypeNm(item.getType().getEventName()));

        return new MyNotificationListRes(
                page.getContent(),
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.hasNext()
        );
    }

    private Boolean toUnreadOnly(MyNotificationReadFilter filter) {
        return switch (filter) {
            case ALL -> null;
            case UNREAD -> true;
            case READ -> false;
        };
    }
}
