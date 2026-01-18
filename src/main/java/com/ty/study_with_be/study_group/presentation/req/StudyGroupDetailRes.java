package com.ty.study_with_be.study_group.presentation.req;

import com.ty.study_with_be.study_group.domain.model.StudyGroup;
import com.ty.study_with_be.study_group.domain.model.enums.StudyMode;
import com.ty.study_with_be.study_group.domain.model.enums.StudyStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class StudyGroupDetailRes {

    private Long studyGroupId;
    private Long ownerId;

    private String title;
    private String category;
    private String topic;
    private String region;

    private StudyMode studyMode;
    private int capacity;
    private int currentCount;

    private String description;
    private StudyStatus status;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static StudyGroupDetailRes from(StudyGroup group) {
        return StudyGroupDetailRes.builder()
                .studyGroupId(group.getStudyGroupId())
                .ownerId(group.getOwnerId())
                .title(group.getTitle())
                .category(group.getCategory())
                .topic(group.getTopic())
                .region(group.getRegion())
                .studyMode(group.getStudyMode())
                .capacity(group.getCapacity())
                .currentCount(group.getCurrentCount())
                .description(group.getDescription())
                .status(group.getStatus())
                .createdAt(group.getCreatedAt())
                .updatedAt(group.getUpdatedAt())
                .build();
    }
}