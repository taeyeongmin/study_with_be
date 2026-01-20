package com.ty.study_with_be.study_group.applicaiton.command.service;

import com.ty.study_with_be.member.domain.model.Member;
import com.ty.study_with_be.member.domain.repository.MemberRepository;
import com.ty.study_with_be.study_group.applicaiton.command.CreateGroupUseCase;
import com.ty.study_with_be.study_group.domain.GroupCreatePolicy;
import com.ty.study_with_be.study_group.domain.GroupRepository;
import com.ty.study_with_be.study_group.domain.model.StudyGroup;
import com.ty.study_with_be.study_group.presentation.command.dto.StudyGroupReq;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CreateGroupService implements CreateGroupUseCase {

    private final GroupRepository groupRepository;
    private final GroupCreatePolicy groupCreatePolicy;
    private final MemberRepository memberRepository;

    @Override
    @Transactional
    public void create(StudyGroupReq studyGroupReq, Long memberId) {

        // 그룹 생성 규칙 검증
        groupCreatePolicy.valid(memberId, studyGroupReq.getTitle());

        // 회원 Entity 조회
        Member member = memberRepository.findByMemberId(memberId);

        // 그룹 Entity생성
        StudyGroup studyGroup = StudyGroup.create(
                studyGroupReq.getTitle()
                , studyGroupReq.getCategory()
                , studyGroupReq.getTopic()
                , studyGroupReq.getRegion()
                , studyGroupReq.getStudyMode()
                , studyGroupReq.getCapacity()
                , studyGroupReq.getDescription()
                , studyGroupReq.getApplyDeadlineAt()
                , studyGroupReq.getSchedules()
                , memberId
                , member
        );

        // DB 저장
        groupRepository.save(studyGroup);
    }
}
