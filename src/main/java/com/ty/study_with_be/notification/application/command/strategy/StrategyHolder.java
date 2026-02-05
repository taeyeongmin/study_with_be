package com.ty.study_with_be.notification.application.command.strategy;

import com.ty.study_with_be.notification.application.command.message.NotificationMessageComposer;
import com.ty.study_with_be.notification.application.command.strategy.recipient.RecipientStrategy;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 이벤트 타입별 발송 대상 전략, 메세지를 전략 구현체를 가지며
 * 해당 기능을 외부에 제공
 */
public class StrategyHolder {

    private final RecipientStrategy recipientStrategy;
    private final NotificationMessageComposer messageComposer;

    public StrategyHolder(RecipientStrategy recipientStrategy,NotificationMessageComposer messageComposer) {
        this.recipientStrategy = recipientStrategy;
        this.messageComposer = messageComposer;
    }

    /**
     * 알림 대상자 조회
     * @param context
     * @return
     */
    public Set<Long> getRecipientIds(NotificationContext context) {
        List<Long> ids = recipientStrategy.resolveTargetMemberId(context);
        return new HashSet<>(ids);
    }

    /**
     * 이벤트 타입별 메세지를 조회
     * @param context
     * @return
     */
    public String createMessage(NotificationContext context){
        return messageComposer.createMessage(context);
    }
}
