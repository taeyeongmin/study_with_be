package com.ty.study_with_be.notification.application.command.handler;

import com.ty.study_with_be.global.event.domain.EventType;
import com.ty.study_with_be.global.outbox.application.NotificationEventHandler;
import com.ty.study_with_be.global.outbox.application.dto.OutboxPayload;
import com.ty.study_with_be.join_request.domain.model.enums.RejectionReason;
import com.ty.study_with_be.notification.application.command.resolver.NotificationStrategyResolver;
import com.ty.study_with_be.notification.application.command.strategy.NotificationContext;
import com.ty.study_with_be.notification.application.command.strategy.StrategyHolder;
import com.ty.study_with_be.notification.domain.Notification;
import com.ty.study_with_be.notification.infra.NotificationJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.Set;

/**
 * outboxEvent 수정 후 notification 수정과 SSE 이벤트 발행을 수행
 * - 상위 트랜잭션을 따른다.(outBoxEvent와의 무결성 보장을 위해)
 */
@Service
@RequiredArgsConstructor
public class NotificationEventHandlerImpl implements NotificationEventHandler {

    private final ApplicationEventPublisher eventPublisher;
    private final NotificationStrategyResolver notificationStrategyResolver;
    private final NotificationJpaRepository notificationJpaRepository;

    @Override
    public void precess(EventType type, OutboxPayload outboxPayload, RejectionReason rejectionReason)  {

        NotificationContext context = NotificationContext.create(type, outboxPayload, rejectionReason);
        
        // 전략 객체 조회
        StrategyHolder resolve = notificationStrategyResolver.resolve(context);

        // 알림을 날릴 memberId 조회
        Set<Long> recipientIds = resolve.getRecipientIds(context);

        // 본인 제외
        recipientIds.remove(context.getProcessMemberId());

        // 메세지 생성
        String message = resolve.createMessage(context);

        for (Long recipientId : recipientIds) {

            // 발송 대상별 Notification Entity 저장
            Notification save = notificationJpaRepository.save(
                    Notification.of(recipientId, type, context.getStudyGroupId(),context.getProcessMemberId(), context.getTargetMemberId(), message)
            );

            // 발송 대상별 스프링 이벤트 발행 (SSE처리용)
            eventPublisher.publishEvent(new NotificationCreatedEvent(save.getId(), recipientId));
        }
    }
}
