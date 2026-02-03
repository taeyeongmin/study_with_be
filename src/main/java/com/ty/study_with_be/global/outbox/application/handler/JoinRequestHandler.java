package com.ty.study_with_be.global.outbox.application.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ty.study_with_be.global.event.domain.EventType;
import com.ty.study_with_be.global.outbox.application.dto.JoinRequestPayload;
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
public class JoinRequestHandler implements OutboxEventHandler {

    private final ObjectMapper objectMapper;
    private final StudyGroupQueryRepository studyGroupQueryRepository;
    private final MemberNicknameResolver nicknameResolver;
    private final NotificationJpaRepository notificationJpaRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Override
    public EventType getType() {
        return EventType.JOIN_REQUEST;
    }

    @Override
    public void handle(EventType type, String payloadJson) throws Exception {
        JoinRequestPayload payload = objectMapper.readValue(payloadJson, JoinRequestPayload.class);
        Long studyGroupId = payload.getStudyGroupId();
        Long requesterMemberId = payload.getRequesterMemberId();
        String requesterNickname = nicknameResolver.nicknameOf(requesterMemberId);
        String content = String.format("[%s]님이 가입을 신청했습니다.", requesterNickname);

        List<Long> recipients = studyGroupQueryRepository.findManagers(studyGroupId);

        for (Long recipient : recipients) {

            Notification save = notificationJpaRepository.save(
                    Notification.of(recipient, type, studyGroupId, requesterMemberId, null, content)
            );

            eventPublisher.publishEvent(new NotificationCreatedEvent(save.getId(), recipient));
        }
    }
}
