package com.ty.study_with_be.study_notice.application.query;

import com.ty.study_with_be.global.error.ErrorCode;
import com.ty.study_with_be.global.exception.DomainException;
import com.ty.study_with_be.study_group.applicaiton.query.StudyGroupQueryRepository;
import com.ty.study_with_be.study_notice.presentation.query.dto.StudyNoticeItem;
import com.ty.study_with_be.study_notice.presentation.query.dto.StudyNoticeListRes;
import com.ty.study_with_be.study_notice.presentation.query.dto.StudyNoticeQueryReq;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly=true)
@Tag(name = "스터디 그룹 공지 조회", description = "스터디 그룹 공지 조회 API")
public class StudyNoticeQueryService {

    private final StudyNoticeQueryRepository studyNoticeQueryRepository;
    private final StudyGroupQueryRepository studyGroupQueryRepository;

    public StudyNoticeListRes getNotices(
            Long groupId,
            Long memberId,
            StudyNoticeQueryReq param
    ) {
        if (!studyGroupQueryRepository.existsMember(groupId,memberId))
            throw new DomainException(ErrorCode.NOT_GROUP_MEMBER);

        List<StudyNoticeItem> items;

        if (param.isRecent()) {
            items = studyNoticeQueryRepository.findRecent(
                    groupId,
                    param.resolveLimit()
            );
        } else {
            items = studyNoticeQueryRepository.findAll(groupId);
        }

        return new StudyNoticeListRes(items);
    }
}
