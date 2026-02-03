package com.ty.study_with_be.global.outbox.application.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ty.study_with_be.global.event.domain.EventType;
import com.ty.study_with_be.global.outbox.application.dto.MemberLeavePayload;
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
public class MemberLeaveHandler implements OutboxEventHandler {

    private final ObjectMapper objectMapper;
    private final StudyGroupQueryRepository studyGroupQueryRepository;
    private final MemberNicknameResolver nicknameResolver;
    private final NotificationJpaRepository notificationJpaRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Override
    public EventType getType() {
        return EventType.MEMBER_LEAVE;
    }

    @Override
    public void handle(EventType type, String payloadJson) throws Exception {
        MemberLeavePayload payload = objectMapper.readValue(payloadJson, MemberLeavePayload.class);
        Long studyGroupId = payload.getStudyGroupId();
        Long leaveMemberId = payload.getLeaveMemberId();
        String leaveNickname = nicknameResolver.nicknameOf(leaveMemberId);
        String content = String.format("[%s]님이 스터디를 탈퇴했습니다.", leaveNickname);

        List<Long> recipients = studyGroupQueryRepository.findManagers(studyGroupId);
        for (Long recipient : recipients) {

            Notification save = notificationJpaRepository.save(
                    Notification.of(recipient, type, studyGroupId, leaveMemberId, leaveMemberId, content)
            );
            eventPublisher.publishEvent(new NotificationCreatedEvent(save.getId(), recipient));
        }
    }
}
