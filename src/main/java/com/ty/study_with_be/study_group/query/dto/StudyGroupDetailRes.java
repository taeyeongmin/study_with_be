package com.ty.study_with_be.study_group.query.dto;

import com.ty.study_with_be.study_group.domain.model.enums.OperationStatus;
import com.ty.study_with_be.study_group.domain.model.enums.RecruitStatus;
import com.ty.study_with_be.study_group.domain.model.enums.StudyMode;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Schema(description = "스터디 그룹 상세 응답")
@Data
public class StudyGroupDetailRes {

    @Schema(description = "스터디 그룹 ID", example = "1")
    private Long studyGroupId;
    @Schema(description = "스터디 그룹 제목", example = "자바 스터디")
    private String title;

    @Schema(description = "카테고리 코드", example = "development")
    private String category;
    @Schema(description = "카테고리명", example = "개발")
    private String categoryNm;

    @Schema(description = "주제", example = "스프링 부트")
    private String topic;

    @Schema(description = "지역 코드", example = "seoul")
    private String region;
    @Schema(description = "지역명", example = "서울")
    private String regionNm;

    @Schema(description = "온라인/오프라인", example = "ONLINE")
    private StudyMode studyMode;
    @Schema(description = "정원", example = "6")
    private int capacity;
    @Schema(description = "현재 인원", example = "3")
    private int currentCount;
    @Schema(description = "설명", example = "주 2회 온라인 스터디입니다.")
    private String description;

    @Schema(description = "모집 상태", example = "RECRUITING")
    private RecruitStatus recruitStatus;
    @Schema(description = "모집 상태명", example = "모집중")
    private String recruitStatusNm;

    @Schema(description = "운영 상태", example = "PREPARING")
    private OperationStatus operationStatus;
    @Schema(description = "운영 상태명", example = "준비중")
    private String operationStatusNm;
    @Schema(description = "모집 마감일", example = "2026-01-31")
    private LocalDate applyDeadlineAt;

    @Schema(description = "스케줄(요일)", example = "[\"MONDAY\",\"WEDNESDAY\"]")
    private Set<DayOfWeek> schedules = new HashSet<>();

    private Long ownerNo;
    @Schema(description = "방장 로그인 ID", example = "user01")
    private String ownerId;
    @Schema(description = "방장 닉네임", example = "타이거")
    private String ownerNickname;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public StudyGroupDetailRes(
            Long studyGroupId,
            String title,
            String category,
            String categoryNm,
            String topic,
            String region,
            String regionNm,
            StudyMode studyMode,
            int capacity,
            int currentCount,
            String description,
            RecruitStatus recruitStatus,
            OperationStatus operationStatus,
            LocalDate applyDeadlineAt,
            Long ownerNo,
            String ownerId,
            String ownerNickname,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ) {
        this.studyGroupId = studyGroupId;
        this.title = title;

        this.category = category;
        this.categoryNm = categoryNm;

        this.topic = topic;

        this.region = region;
        this.regionNm = regionNm;

        this.studyMode = studyMode;
        this.capacity = capacity;
        this.currentCount = currentCount;
        this.description = description;

        this.recruitStatus = recruitStatus;
        this.recruitStatusNm = recruitStatus.getCodeNm();

        this.operationStatus = operationStatus;
        this.operationStatusNm = operationStatus.getCodeNm();
        this.applyDeadlineAt = applyDeadlineAt;

        this.ownerNo = ownerNo;
        this.ownerId = ownerId;
        this.ownerNickname = ownerNickname;

        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}
