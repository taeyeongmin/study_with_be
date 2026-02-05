package com.ty.study_with_be.notification.application.strategy.registry;

import com.ty.study_with_be.notification.application.strategy.recipient.RecipientStrategy;
import com.ty.study_with_be.notification.application.strategy.recipient.RecipientType;
import org.springframework.stereotype.Component;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

@Component
public class RecipientStrategyRegistry {

    private final Map<RecipientType, RecipientStrategy> map = new EnumMap<>(RecipientType.class);

    public RecipientStrategyRegistry(List<RecipientStrategy> strategies) {

        for (RecipientStrategy strategy : strategies) {
            map.put(strategy.getType(), strategy);
        }
    }

    public RecipientStrategy get(RecipientType type) {
        RecipientStrategy strategy = map.get(type);
        if (strategy == null) throw new IllegalArgumentException("No strategy found for type " + type);
        return strategy;
    }
}