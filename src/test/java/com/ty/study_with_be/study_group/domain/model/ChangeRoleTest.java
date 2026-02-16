package com.ty.study_with_be.study_group.domain.model;

import com.ty.study_with_be.global.error.ErrorCode;
import com.ty.study_with_be.global.exception.DomainException;
import com.ty.study_with_be.study_group.domain.model.enums.StudyMode;
import com.ty.study_with_be.study_group.domain.model.enums.StudyRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;

@ExtendWith(MockitoExtension.class)
class ChangeRoleTest {

    StudyGroup samplestudyGroup;

    @BeforeEach
    void setUp() {

        samplestudyGroup = StudyGroup.create(
                "title"
                , "category"
                , "주제"
                , ""
                , StudyMode.ONLINE
                , 5
                , "ㅇㅇ"
                , null
                , null
                , 1L
        );

    }

    @Test
    void 실패_방장이_아닌_경우(){

        // given
        StudyGroup spyGroup = spy(samplestudyGroup);
        StudyMember targetMember = StudyMember.createMember(spyGroup, 2L);
        StudyMember currentMember = StudyMember.createMember(spyGroup, 3L);

        doReturn(targetMember)
                .when(spyGroup)
                .findStudyMemberByStudyMemberId(10L);

        doReturn(currentMember)
                .when(spyGroup)
                .findStudyMemberByMemberId(3L);

        // when & then
        DomainException ex = assertThrows(DomainException.class, () -> {
            spyGroup.changeRole(10L, 3L, StudyRole.MANAGER);
        });

        assertEquals(ErrorCode.NOT_GROUP_OWNER, ex.getErrorCode());
    }

    @Test
    void 실패_셀프변경(){

        // given
        StudyGroup spyGroup = spy(samplestudyGroup);
        StudyMember targetMember = StudyMember.createLeader(spyGroup, 1L);
        StudyMember currentMember = StudyMember.createLeader(spyGroup, 1L);

        doReturn(targetMember)
                .when(spyGroup)
                .findStudyMemberByStudyMemberId(10L);

        doReturn(currentMember)
                .when(spyGroup)
                .findStudyMemberByMemberId(3L);

        // when & then
        DomainException ex = assertThrows(DomainException.class, () -> {
            spyGroup.changeRole(10L, 3L, StudyRole.MANAGER);
        });

        assertEquals(ErrorCode.CANNOT_SELF_PROC, ex.getErrorCode());
    }
}











