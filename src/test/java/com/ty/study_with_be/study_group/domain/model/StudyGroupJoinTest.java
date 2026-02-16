package com.ty.study_with_be.study_group.domain.model;

import com.ty.study_with_be.global.error.ErrorCode;
import com.ty.study_with_be.global.exception.DomainException;
import com.ty.study_with_be.member.domain.model.Member;
import com.ty.study_with_be.study_group.domain.model.enums.StudyMode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StudyGroupJoinTest {

    Member member;

    StudyGroup samplestudyGroup;

    @BeforeEach
    void setUp() {
        member = Member.createLocalMember("test","111","tester");
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
    void 가입_신청_승인_실패_권한X_SPY() {

        // given
        StudyMember normalMember = StudyMember.createMember(samplestudyGroup, 1L);

        StudyGroup spyGroup = spy(samplestudyGroup);

        doReturn(normalMember)
                .when(spyGroup)
                .findStudyMemberByMemberId(any());

        System.out.println("================================");

        // when & then
        DomainException ex = assertThrows(DomainException.class, () -> {
            spyGroup.joinMember(2L, 1L);
        });

        assertEquals(ErrorCode.HAS_NOT_PERMISSION, ex.getErrorCode());
    }


    @Test
    void 가입_신청_승인_실패_권한X_SPY_WITH_WHENTHEN(){

        // given
        StudyMember normalMember = StudyMember.createMember(samplestudyGroup, 1L);

        StudyGroup spyGroup = spy(samplestudyGroup);

        when(spyGroup.findStudyMemberByMemberId(1L)).thenReturn(normalMember);

        System.out.println("================================");
        // when & then
        DomainException ex = assertThrows(DomainException.class, () -> {
            spyGroup.joinMember(2L, 1L);
        });

        assertEquals(ErrorCode.HAS_NOT_PERMISSION, ex.getErrorCode());
    }

    @Test
    void 가입_신청_승인_성공_권한검증무시_SPY() {

        // given
        StudyGroup spyGroup = spy(samplestudyGroup);

        doAnswer(inv -> 33 + 12)
                .when(spyGroup).validJoinMember(any());
        // when & then
        assertDoesNotThrow(()-> spyGroup.joinMember(2L, 1L));
    }

}
