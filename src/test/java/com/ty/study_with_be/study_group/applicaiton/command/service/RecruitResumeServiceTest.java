package com.ty.study_with_be.study_group.applicaiton.command.service;

import com.ty.study_with_be.global.error.ErrorCode;
import com.ty.study_with_be.global.exception.DomainException;
import com.ty.study_with_be.study_group.applicaiton.command.RecruitResumeUseCase;
import com.ty.study_with_be.study_group.domain.GroupRepository;
import com.ty.study_with_be.study_group.domain.model.StudyGroup;
import com.ty.study_with_be.study_group.domain.model.enums.RecruitStatus;
import com.ty.study_with_be.study_group.domain.model.enums.StudyMode;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RecruitResumeServiceTest {

    StudyGroup samplestudyGroup;

    @Mock
    private GroupRepository groupRepository;

    @InjectMocks
    private RecruitResumeService recruitResumeUseCase;

    @BeforeEach
    void setUp() {
        samplestudyGroup = StudyGroup.create(
                "title"
                , "category"
                , "주제"
                , ""
                , StudyMode.ONLINE
                , 2
                , "ㅇㅇ"
                , null
                , null
                , 1L
        );
    }

    @Test
    void 모집재개_실패_모집중() {

        when(groupRepository.findById(1L)).thenReturn(Optional.of(samplestudyGroup));

        DomainException ex = assertThrows(DomainException.class, () -> {
            recruitResumeUseCase.resumeRecruitment(1L, 1L);
        });

        assertEquals(ErrorCode.NOT_RECRUIT_END, ex.getErrorCode());
    }

    @Test
    void 모집재개_실패_정원초과() {

        samplestudyGroup.joinMember(2L, 1L);

        // 현재 상태를 RECRUIT_END로 강제 세팅
        ReflectionTestUtils.setField(samplestudyGroup, "recruitStatus", RecruitStatus.RECRUIT_END);

        when(groupRepository.findById(1L)).thenReturn(Optional.of(samplestudyGroup));

        DomainException ex = assertThrows(DomainException.class, () -> {
            recruitResumeUseCase.resumeRecruitment(1L, 1L);
        });

        assertEquals(ErrorCode.CAPACITY_EXCEEDED, ex.getErrorCode());
    }
}