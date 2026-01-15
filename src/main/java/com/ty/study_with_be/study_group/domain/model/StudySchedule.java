package com.ty.study_with_be.study_group.domain.model;

import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.DayOfWeek;

@Embeddable
@NoArgsConstructor
@EqualsAndHashCode
public class StudySchedule {

    @Enumerated(EnumType.STRING)
    private DayOfWeek dayOfWeek;

    public StudySchedule(DayOfWeek dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }
}