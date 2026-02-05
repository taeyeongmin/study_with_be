package com.ty.study_with_be.notification.application.command.resolver;

import com.ty.study_with_be.notification.application.command.message.NotificationMessageComposer;
import com.ty.study_with_be.notification.application.command.policy.EventRecipientPolicy;
import com.ty.study_with_be.notification.application.command.strategy.NotificationContext;
import com.ty.study_with_be.notification.application.command.strategy.StrategyHolder;
import com.ty.study_with_be.notification.application.command.strategy.recipient.RecipientStrategy;
import com.ty.study_with_be.notification.application.command.strategy.recipient.RecipientType;
import com.ty.study_with_be.notification.application.command.strategy.registry.RecipientStrategyRegistry;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * 각 전략들에 대한 실제 구현체를 가지고 있으며,
 * 해당 구현체를 사용하여 외부에 기능을 제공
 */
@Component
@RequiredArgsConstructor
public class NotificationStrategyResolver {

    private final RecipientStrategyRegistry recipientRegistry;
    private final EventRecipientPolicy eventRecipientPolicy;
    private final NotificationMessageComposer messageComposer;

    public StrategyHolder resolve(NotificationContext context) {

        // 이벤트 타입으로 수신자 ENUM 객체 조회
        RecipientType recipientType =
                eventRecipientPolicy.resolve(context.getEventType());
        
        RecipientStrategy recipientStrategy =
                recipientRegistry.get(recipientType);

        return new StrategyHolder(
                recipientStrategy
                , messageComposer
        );
    }
}
