package com.ty.study_with_be.study_group.applicaiton.query;

import com.ty.study_with_be.join_request.application.query.JoinRequestQueryRepository;
import com.ty.study_with_be.study_group.domain.GroupRepository;
import com.ty.study_with_be.study_group.domain.model.StudyGroup;
import com.ty.study_with_be.study_group.domain.model.enums.RecruitStatus;
import com.ty.study_with_be.study_group.domain.model.enums.StudyMode;
import com.ty.study_with_be.study_group.domain.model.enums.StudyRole;
import com.ty.study_with_be.study_group.presentation.query.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StudyGroupQueryService {
    private static final int POPULAR_DEFAULT_PAGE = 0;
    private static final int POPULAR_DEFAULT_SIZE = 20;

    private final StudyGroupQueryRepository groupQueryRepository;
    private final JoinRequestQueryRepository joinRequestQueryRepository;
    private final GroupRepository groupRepository;

    public StudyGroupDetailRes getDetail(Long studyGroupId,Long currentMemberId) {

        StudyGroup studyGroup = groupRepository.findById(studyGroupId).orElseThrow(RuntimeException::new);
        Set<DayOfWeek> schedules = studyGroup.getSchedules();

        StudyGroupDetailRes studyGroupDetailRes = groupQueryRepository.findDetail(studyGroupId,currentMemberId)
                .orElseThrow(RuntimeException::new);

        studyGroupDetailRes.setSchedules(schedules);

        return studyGroupDetailRes;
    }

    public StudyGroupListRes getStudyGroupList(
            StudyGroupListReq req,
            Pageable pageable,
            Long currentMemberId
    ) {
        Page<StudyGroupListItem> page = groupQueryRepository.findStudyGroups(
                req.getTitle(),req.getCategory(), req.getTopic(), req.getRegion(), req.getStudyMode(), req.getRecruitStatus(), pageable,currentMemberId
        );

        return new StudyGroupListRes(
                page.getContent(),
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.hasNext()
        );
    }

    /**
     * 스터디 그룹 내 회원 목록 조회
     *
     * @param studyGroupId
     * @return
     */
    public StudyMemberListRes getStudyMemberList(Long studyGroupId) {

        List<StudyMemberItem> studyMemberList = groupQueryRepository.findStudyMemberList(studyGroupId);

        return new StudyMemberListRes(studyMemberList);
    }


    /**
     * 해당 스터디 그룹 내 나의 정보 조회
     * @param groupId
     * @param memberId
     * @return
     */
    public MyStudyGroupStatusRes getMyStatus(Long groupId, Long memberId) {

        // 가입 여부 + 역할 조회
        Optional<StudyRole> role =
                groupQueryRepository.findRole(groupId, memberId);


        if (role.isPresent()) {
            return MyStudyGroupStatusRes.joined(role.get());
        }

        Optional<Long> joinRequestId =
                joinRequestQueryRepository.findPendingJoinRequestId(groupId, memberId);

        return joinRequestId.map(MyStudyGroupStatusRes::pending).orElseGet(MyStudyGroupStatusRes::none);
    }

    /**
     * 회원의 참여중인 그룹 갯수(방장X)*
     * @param memberId
     * @return int
     */
    public int countByMemberIdJoined(Long memberId) {

        return groupQueryRepository.countByMemberIdJoined(memberId);
    }

    /**
     * 회원의 참여중인 그룹 갯수(방장O)*
     * @param memberId
     * @return int
     */
    public int countByMemberIdOperate(Long memberId) {

        return groupQueryRepository.countByMemberIdOperate(memberId);
    }

    public MyStudyGroupListRes getMyGroupList(
            Long memberId, MyStudyGroupListReq request, Pageable pageable
    ) {

        Page<MyStudyGroupListItem> page =
                groupQueryRepository.findMyStudyGroups(
                        memberId,
                        request.getOperationFilter(),
                        request.getRoleFilter(),
                        pageable
                );

        return new MyStudyGroupListRes(
                page.getContent(),
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.hasNext()
        );

    }

    public StudyGroupListRes getPopularStudy() {

        Pageable pageable = PageRequest.of(POPULAR_DEFAULT_PAGE, POPULAR_DEFAULT_SIZE);

        Page<StudyGroupListItem> page = groupQueryRepository.findPopularStudyGroups(pageable);

        return new StudyGroupListRes(
                page.getContent(),
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.hasNext()
        );
    }
}
