package com.ty.study_with_be.study_group.applicaiton.service;

import com.ty.study_with_be.member.domain.repository.MemberRepository;
import com.ty.study_with_be.study_group.applicaiton.UpdateGroupUseCase;
import com.ty.study_with_be.study_group.domain.GroupCreatePolicy;
import com.ty.study_with_be.study_group.domain.GroupRepository;
import com.ty.study_with_be.study_group.domain.model.StudyGroup;
import com.ty.study_with_be.study_group.presentation.req.StudyGroupReq;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UpdateGroupService implements UpdateGroupUseCase {

    private final GroupRepository groupRepository;
    private final GroupCreatePolicy groupCreatePolicy;
    private final MemberRepository memberRepository;

    @Override
    @Transactional
    public void update(StudyGroupReq studyGroupReq, Long memberId, Long studyGroupId) {

        // 기존 Group 정보 조회
        StudyGroup studyGroup = groupRepository.findById(studyGroupId).orElseThrow(() -> new RuntimeException("해당 그룹이 없습니다."));

        System.err.println("studyGroup : "+studyGroup);

        // DB 저장
    }
}
