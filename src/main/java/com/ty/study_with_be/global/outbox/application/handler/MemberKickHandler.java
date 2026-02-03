package com.ty.study_with_be.global.outbox.application.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ty.study_with_be.global.event.domain.EventType;
import com.ty.study_with_be.global.outbox.application.dto.MemberKickPayload;
import com.ty.study_with_be.global.outbox.application.support.MemberNicknameResolver;
import com.ty.study_with_be.notification.application.event.NotificationCreatedEvent;
import com.ty.study_with_be.notification.domain.Notification;
import com.ty.study_with_be.notification.infra.NotificationJpaRepository;
import com.ty.study_with_be.study_group.applicaiton.query.StudyGroupQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class MemberKickHandler implements OutboxEventHandler {

    private final ObjectMapper objectMapper;
    private final StudyGroupQueryRepository studyGroupQueryRepository;
    private final MemberNicknameResolver nicknameResolver;
    private final NotificationJpaRepository notificationJpaRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Override
    public EventType getType() {
        return EventType.MEMBER_KICK;
    }

    @Override
    public void handle(EventType type, String payloadJson) throws Exception {
        MemberKickPayload payload = objectMapper.readValue(payloadJson, MemberKickPayload.class);
        Long studyGroupId = payload.getStudyGroupId();
        Long processorId = payload.getProcessorId();
        Long targetMemberId = payload.getTargetMemberId();
        String processorNickname = nicknameResolver.nicknameOf(processorId);
        String targetNickname = nicknameResolver.nicknameOf(targetMemberId);

        String content = String.format("[%s]님이 [%s]님을 강퇴했습니다.", processorNickname, targetNickname);

        // 대상자에게 알림
        Notification noti = notificationJpaRepository.save(
                Notification.of(targetMemberId, type, studyGroupId, processorId, targetMemberId, content)
        );
        eventPublisher.publishEvent(new NotificationCreatedEvent(noti.getId(), targetMemberId));

        // 관리자에게도 알림 (대상자 중복 제거)
        List<Long> managers = studyGroupQueryRepository.findManagers(studyGroupId);

        for (Long manager : managers) {

            if (manager.equals(targetMemberId)) continue;

            Notification save = notificationJpaRepository.save(
                    Notification.of(manager, type, studyGroupId, processorId, targetMemberId, content)
            );

            eventPublisher.publishEvent(new NotificationCreatedEvent(save.getId(), manager));
        }
    }
}
