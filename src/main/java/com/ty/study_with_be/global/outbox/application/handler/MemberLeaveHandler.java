package com.ty.study_with_be.global.outbox.application.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ty.study_with_be.global.event.domain.EventType;
import com.ty.study_with_be.global.outbox.application.dto.MemberLeavePayload;
import com.ty.study_with_be.notification.domain.Notification;
import com.ty.study_with_be.notification.infra.NotificationJpaRepository;
import com.ty.study_with_be.study_group.applicaiton.query.StudyGroupQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class MemberLeaveHandler implements OutboxEventHandler {

    private final ObjectMapper objectMapper;
    private final StudyGroupQueryRepository studyGroupQueryRepository;
    private final NotificationJpaRepository notificationJpaRepository;

    @Override
    public EventType getType() {
        return EventType.MEMBER_LEAVE;
    }

    @Override
    public void handle(EventType type, String payloadJson) throws Exception {
        MemberLeavePayload payload = objectMapper.readValue(payloadJson, MemberLeavePayload.class);
        Long studyGroupId = payload.getStudyGroupId();
        Long leaveMemberId = payload.getLeaveMemberId();

        List<Long> recipients = studyGroupQueryRepository.findManagers(studyGroupId);
        for (Long r : recipients) {
            String notiPayload = objectMapper.writeValueAsString(Map.of(
                "type", type.name(),
                "studyGroupId", studyGroupId,
                "leaveMemberId", leaveMemberId
            ));
            notificationJpaRepository.save(
                Notification.of(r, type, studyGroupId, leaveMemberId, leaveMemberId, notiPayload)
            );
        }
    }
}
