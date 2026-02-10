package com.ty.study_with_be.notification.application.query;

import com.ty.study_with_be.notification.presentation.query.dto.NotificationItem;
import com.ty.study_with_be.notification.presentation.query.dto.NotificationListRes;
import lombok.RequiredArgsConstructor;
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

        return new NotificationListRes(list,0);
    }

    public int countByMemberId(Long memberId) {
        return notificationQueryRepository.countNotReadByMemberId(memberId);
    }
}
