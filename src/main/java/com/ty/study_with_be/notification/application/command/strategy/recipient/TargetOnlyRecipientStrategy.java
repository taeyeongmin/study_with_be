package com.ty.study_with_be.notification.application.command.strategy.recipient;

import com.ty.study_with_be.notification.application.command.strategy.NotificationContext;
import com.ty.study_with_be.study_group.applicaiton.query.StudyGroupQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class TargetOnlyRecipientStrategy implements RecipientStrategy {


    @Override
    public RecipientType getType() {
        return RecipientType.TARGET_ONLY;
    }

    @Override
    public List<Long> resolveTargetMemberId(NotificationContext context) {

        Long targetId = context.getRequesterMemberId() ==  null ? context.getTargetMemberId() : context.getRequesterMemberId();

        return List.of(targetId);
    }
}