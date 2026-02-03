package com.ty.study_with_be.global.outbox.application.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ty.study_with_be.global.event.domain.EventType;
import com.ty.study_with_be.global.outbox.application.dto.JoinProcessPayload;
import com.ty.study_with_be.global.outbox.application.support.MemberNicknameResolver;
import com.ty.study_with_be.notification.application.event.NotificationCreatedEvent;
import com.ty.study_with_be.notification.domain.Notification;
import com.ty.study_with_be.notification.infra.NotificationJpaRepository;
import com.ty.study_with_be.study_group.applicaiton.query.StudyGroupQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class JoinProcessHandler implements OutboxEventHandler{

    private final ObjectMapper objectMapper;
    private final MemberNicknameResolver nicknameResolver;
    private final NotificationJpaRepository notificationJpaRepository;
    private final StudyGroupQueryRepository studyGroupQueryRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Override
    public EventType getType() {
        return EventType.JOIN_APPROVE;
    }

    @Override
    public Set<EventType> getTypes() {
        return Set.of(EventType.JOIN_APPROVE, EventType.JOIN_REJECT);
    }

    @Override
    public void handle(EventType type, String payloadJson) throws Exception {

        JoinProcessPayload payload = objectMapper.readValue(payloadJson, JoinProcessPayload.class);
        Long studyGroupId = payload.getStudyGroupId();
        Long requesterMemberId = payload.getRequesterMemberId();
        Long processorId = payload.getProcessorId();
        String requesterNickname = nicknameResolver.nicknameOf(requesterMemberId);
        String processorNickname = nicknameResolver.nicknameOf(processorId);
        String action = (type == EventType.JOIN_APPROVE) ? "승인" : "거절";
        String content = String.format("[%s]님이 [%s]님의 신청을 %s했습니다.", processorNickname, requesterNickname, action);

        Notification noti = notificationJpaRepository.save(
                Notification.of(requesterMemberId, type, studyGroupId, processorId, requesterMemberId, content)
        );

        eventPublisher.publishEvent(new NotificationCreatedEvent(noti.getId(), requesterMemberId));

        // 관리자에게도 알림
        List<Long> recipients = studyGroupQueryRepository.findManagers(studyGroupId);

        for (Long recipient : recipients) {

            Notification noti2 = notificationJpaRepository.save(
                    Notification.of(recipient, type, studyGroupId, processorId, requesterMemberId, content)
            );

            eventPublisher.publishEvent(new NotificationCreatedEvent(noti2.getId(), recipient));
        }
    }
}
