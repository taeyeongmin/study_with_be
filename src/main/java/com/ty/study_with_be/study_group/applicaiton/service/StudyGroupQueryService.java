package com.ty.study_with_be.study_group.applicaiton.service;

import com.ty.study_with_be.member.domain.repository.MemberRepository;
import com.ty.study_with_be.study_group.applicaiton.StudyGroupQueryUseCase;
import com.ty.study_with_be.study_group.domain.GroupCreatePolicy;
import com.ty.study_with_be.study_group.domain.GroupRepository;
import com.ty.study_with_be.study_group.domain.model.StudyGroup;
import com.ty.study_with_be.study_group.presentation.req.StudyGroupDetailRes;
import com.ty.study_with_be.study_group.presentation.req.StudyGroupReq;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class StudyGroupQueryService implements StudyGroupQueryUseCase {

    private final GroupRepository groupRepository;
    private final MemberRepository memberRepository;

    @Override
    @Transactional(readOnly = true)
    public StudyGroupDetailRes getDetail(Long studyGroupId) {

        StudyGroup group = groupRepository.findById(studyGroupId)
                .filter(g -> !g.isSuspended())
                .orElseThrow(RuntimeException::new);

        return StudyGroupDetailRes.from(group);
    }
}
