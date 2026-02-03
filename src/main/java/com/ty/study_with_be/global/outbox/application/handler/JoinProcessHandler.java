package com.ty.study_with_be.global.outbox.application.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ty.study_with_be.global.event.domain.EventType;
import com.ty.study_with_be.global.outbox.application.dto.JoinProcessPayload;
import com.ty.study_with_be.notification.domain.Notification;
import com.ty.study_with_be.notification.infra.NotificationJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class JoinProcessHandler implements OutboxEventHandler{

    private final ObjectMapper objectMapper;
    private final NotificationJpaRepository notificationJpaRepository;

    @Override
    public EventType getType() {
        return null;
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

        String notiPayload = objectMapper.writeValueAsString(Map.of(
            "type", type.name(),
            "studyGroupId", studyGroupId,
            "processorId", processorId,
            "requesterMemberId", requesterMemberId
        ));

        notificationJpaRepository.save(
            Notification.of(requesterMemberId, type, studyGroupId, processorId, requesterMemberId, notiPayload)
        );
    }
}
