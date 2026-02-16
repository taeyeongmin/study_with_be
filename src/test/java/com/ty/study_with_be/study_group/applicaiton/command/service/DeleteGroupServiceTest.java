package com.ty.study_with_be.study_group.applicaiton.command.service;

import com.ty.study_with_be.global.error.ErrorCode;
import com.ty.study_with_be.global.exception.DomainException;
import com.ty.study_with_be.member.domain.model.Member;
import com.ty.study_with_be.study_group.domain.GroupRepository;
import com.ty.study_with_be.study_group.domain.model.StudyGroup;
import com.ty.study_with_be.study_group.domain.model.StudyMember;
import com.ty.study_with_be.study_group.domain.model.enums.StudyMode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DeleteGroupServiceTest {

    StudyGroup samplestudyGroup;

    @Mock
    private GroupRepository groupRepository;

    @InjectMocks
    private DeleteGroupService deleteGroupService;

    @BeforeEach
    void setUp() {
        samplestudyGroup = StudyGroup.create(
                "title"
                , "category"
                , "주제"
                , ""
                , StudyMode.ONLINE
                , 3
                , "ㅇㅇ"
                , null
                , null
                , 1L
        );
    }

    @Test
    void 그룹_삭제_실패_그룹원존재() {
        
        // given
        samplestudyGroup.joinMember(2L,1L);
        when(groupRepository.findById(any())).thenReturn(Optional.of(samplestudyGroup));;

        // when & then
        DomainException ex = assertThrows(DomainException.class, () -> {
            deleteGroupService.deleteGroup(1L,1L);
        });

        assertEquals(ErrorCode.NOT_ONLY_OWNER, ex.getErrorCode());
    }

    @Test
    void 그룹_삭제_실패_방장X() {

        // 방장의 memberId == 1
        // 일반 회원 memberId == 2

        // given
        samplestudyGroup.joinMember(2L,1L);
        when(groupRepository.findById(any())).thenReturn(Optional.of(samplestudyGroup));;

        // when & then
        DomainException ex = assertThrows(DomainException.class, () -> {
            deleteGroupService.deleteGroup(1L,2L);
        });

        assertEquals(ErrorCode.NOT_GROUP_OWNER, ex.getErrorCode());
    }

    @Test
    void 그룹_삭제_성공() {

        // 방장의 memberId == 1
        // 일반 회원 memberId == 2
        // given
        when(groupRepository.findById(1L)).thenReturn(Optional.of(samplestudyGroup));

        // when & then
        assertDoesNotThrow(() -> deleteGroupService.deleteGroup(1L, 1L));

        verify(groupRepository).delete(samplestudyGroup);
    }
}
