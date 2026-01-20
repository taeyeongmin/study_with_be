package com.ty.study_with_be.study_group.applicaiton.service;

import com.ty.study_with_be.study_group.applicaiton.StudyGroupQueryUseCase;
import com.ty.study_with_be.study_group.domain.GroupRepository;
import com.ty.study_with_be.study_group.domain.model.StudyGroup;
import com.ty.study_with_be.study_group.presentation.req.StudyGroupDetailRes;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class StudyGroupQueryService implements StudyGroupQueryUseCase {

    private final GroupRepository groupRepository;

    @Override
    @Transactional(readOnly = true)
    public StudyGroupDetailRes getDetail(Long studyGroupId) {

        StudyGroup group = groupRepository.findById(studyGroupId)
                .orElseThrow(RuntimeException::new);

        return StudyGroupDetailRes.from(group);
    }
}
