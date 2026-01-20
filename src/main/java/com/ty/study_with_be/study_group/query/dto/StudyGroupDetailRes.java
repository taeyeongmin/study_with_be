package com.ty.study_with_be.study_group.query.dto;

import com.ty.study_with_be.study_group.domain.model.enums.OperationStatus;
import com.ty.study_with_be.study_group.domain.model.enums.RecruitStatus;
import com.ty.study_with_be.study_group.domain.model.enums.StudyMode;
import lombok.Data;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Data
public class StudyGroupDetailRes {

    private Long studyGroupId;
    private String title;

    private String category;
    private String categoryNm;

    private String topic;

    private String region;
    private String regionNm;

    private StudyMode studyMode;
    private int capacity;
    private int currentCount;
    private String description;

    private RecruitStatus recruitStatus;
    private String recruitStatusNm;

    private OperationStatus operationStatus;
    private String operationStatusNm;

    private Set<DayOfWeek> schedules = new HashSet<>();

    private Long ownerNo;
    private String ownerId;
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

        this.ownerNo = ownerNo;
        this.ownerId = ownerId;
        this.ownerNickname = ownerNickname;

        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}
