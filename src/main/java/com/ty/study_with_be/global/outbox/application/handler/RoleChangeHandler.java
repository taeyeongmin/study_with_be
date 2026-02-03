package com.ty.study_with_be.global.outbox.application.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ty.study_with_be.global.event.domain.EventType;
import com.ty.study_with_be.global.outbox.application.dto.RoleChangePayload;
import com.ty.study_with_be.notification.domain.Notification;
import com.ty.study_with_be.notification.infra.NotificationJpaRepository;
import com.ty.study_with_be.study_group.applicaiton.query.StudyGroupQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class RoleChangeHandler implements OutboxEventHandler {

    private final ObjectMapper objectMapper;
    private final StudyGroupQueryRepository studyGroupQueryRepository;
    private final NotificationJpaRepository notificationJpaRepository;

    @Override
    public EventType getType() {
        return EventType.ROLE_CHANGE;
    }

    @Override
    public void handle(EventType type, String payloadJson) throws Exception {
        RoleChangePayload payload = objectMapper.readValue(payloadJson, RoleChangePayload.class);
        Long studyGroupId = payload.getStudyGroupId();
        Long processorId = payload.getProcessorId();
        Long targetMemberId = payload.getTargetMemberId();

        // 대상자에게 알림
        String toTarget = objectMapper.writeValueAsString(Map.of(
            "type", type.name(),
            "studyGroupId", studyGroupId,
            "processorId", processorId,
            "targetMemberId", targetMemberId
        ));
        notificationJpaRepository.save(
            Notification.of(targetMemberId, type, studyGroupId, processorId, targetMemberId, toTarget)
        );

        // 관리자에게도 알림 (대상자 중복 제거)
        List<Long> managers = studyGroupQueryRepository.findManagers(studyGroupId);
        for (Long manager : managers) {
            if (manager.equals(targetMemberId)) continue;

            String managerPayload = objectMapper.writeValueAsString(Map.of(
                "type", type.name(),
                "studyGroupId", studyGroupId,
                "processorId", processorId,
                "targetMemberId", targetMemberId
            ));
            notificationJpaRepository.save(
                Notification.of(manager, type, studyGroupId, processorId, targetMemberId, managerPayload)
            );
        }
    }
}
