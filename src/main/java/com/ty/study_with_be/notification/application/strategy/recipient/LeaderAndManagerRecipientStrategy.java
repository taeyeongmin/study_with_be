package com.ty.study_with_be.notification.application.strategy.recipient;

import com.ty.study_with_be.notification.application.strategy.NotificationContext;
import com.ty.study_with_be.study_group.applicaiton.query.StudyGroupQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
@RequiredArgsConstructor
public class LeaderAndManagerRecipientStrategy implements RecipientStrategy {

    private final StudyGroupQueryRepository studyGroupQueryRepository;

    @Override
    public RecipientType getType() {
        return RecipientType.LEADER_AND_MANAGER;
    }

    @Override
    public List<Long> resolveTargetMemberId(NotificationContext context) {

        Long studyGroupId = context.getStudyGroupId();

        return studyGroupQueryRepository.findManagers(studyGroupId);
    }
}