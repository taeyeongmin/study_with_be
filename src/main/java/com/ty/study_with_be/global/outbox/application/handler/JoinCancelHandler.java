package com.ty.study_with_be.global.outbox.application.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ty.study_with_be.global.event.domain.EventType;
import com.ty.study_with_be.global.outbox.application.dto.JoinRequestPayload;
import com.ty.study_with_be.notification.domain.Notification;
import com.ty.study_with_be.notification.infra.NotificationJpaRepository;
import com.ty.study_with_be.study_group.applicaiton.query.StudyGroupQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class JoinCancelHandler implements OutboxEventHandler {

    private final ObjectMapper objectMapper;
    private final StudyGroupQueryRepository studyGroupQueryRepository;
    private final NotificationJpaRepository notificationJpaRepository;

    @Override
    public EventType getType() {
        return EventType.JOIN_CANCEL;
    }

    @Override
    public void handle(EventType type, String payloadJson) throws Exception {
        JoinRequestPayload payload = objectMapper.readValue(payloadJson, JoinRequestPayload.class);
        Long studyGroupId = payload.getStudyGroupId();
        Long requesterMemberId = payload.getRequesterMemberId();

        List<Long> recipients = studyGroupQueryRepository.findManagers(studyGroupId);
        for (Long r : recipients) {
            String notiPayload = objectMapper.writeValueAsString(Map.of(
                "type", type.name(),
                "studyGroupId", studyGroupId,
                "requesterMemberId", requesterMemberId
            ));
            notificationJpaRepository.save(
                Notification.of(r, type, studyGroupId, requesterMemberId, null, notiPayload)
            );
        }
    }
}
