package com.ty.study_with_be.study_group.applicaiton.service;

import com.ty.study_with_be.study_group.applicaiton.CreateGroupUseCase;
import com.ty.study_with_be.study_group.domain.GroupCreatePolicy;
import com.ty.study_with_be.study_group.domain.GroupRepository;
import com.ty.study_with_be.study_group.presentation.req.StudyGroupReq;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateGroupService implements CreateGroupUseCase {

    private final GroupRepository groupRepository;
    private final GroupCreatePolicy groupCreatePolicy;

    @Override
    public void create(StudyGroupReq studyGroupReq) {

        // 1. 그룹 생성 규칙 검증
        groupCreatePolicy.valid();

        // 2. 그룹 Entity생성

        // 3. DB 저장

    }
}
