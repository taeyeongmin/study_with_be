package com.ty.study_with_be.notification.application.command.strategy.recipient;

import com.ty.study_with_be.notification.application.command.strategy.NotificationContext;
import com.ty.study_with_be.study_group.applicaiton.query.StudyGroupQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class AllMemberrRecipientStrategy implements RecipientStrategy {

    private final StudyGroupQueryRepository studyGroupQueryRepository;

    @Override
    public RecipientType getType() {
        return RecipientType.ALL_MEMBERS;
    }

    @Override
    public List<Long> resolveTargetMemberId(NotificationContext context) {

        Long studyGroupId = context.getStudyGroupId();

        return studyGroupQueryRepository.findAllMember(studyGroupId);
    }
}