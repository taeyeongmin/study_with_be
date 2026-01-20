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

@ExtendWith(MockitoExtension.class)
class StudyGroupTest {

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
                , member
        );
    }

    @Test
    void 그룹생성_실패_오프라인_장소X() {

        // when & then
        DomainException ex = assertThrows(DomainException.class, () -> {
            StudyGroup.create(
                    "title"
                    , "category"
                    , "주제"
                    , ""
                    , StudyMode.OFFLINE
                    , 3
                    , "ㅇㅇ"
                    , null
                    , null
                    , 1L
                    , member
            );
        });

        assertEquals(ErrorCode.OFFLINE_STUDY_REGION_REQUIRED, ex.getErrorCode());
    }

    @Test
    void 그룹생성_실패_정원미달() {

        // when & then
        DomainException ex = assertThrows(DomainException.class, () -> {
            StudyGroup.create(
                    "title"
                    , "category"
                    , "주제"
                    , ""
                    , StudyMode.OFFLINE
                    , 1
                    , "ㅇㅇ"
                    , null
                    , null
                    , 1L
                    , member
            );
        });

        assertEquals(ErrorCode.INVALID_STUDY_CAPACITY, ex.getErrorCode());
    }

    @Test
    void 그룹생성_성공() {

        // when & then
        assertDoesNotThrow(() -> {
            StudyGroup.create(
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
                    , member
            );
        });
    }

    @Test
    void 기본정보수정_실패_오프라인_장소X() {

        // when & then
        DomainException ex = assertThrows(DomainException.class, () -> {
            samplestudyGroup.updateInfo(
                    "title"
                    , "category"
                    , "주제"
                    , ""
                    , StudyMode.OFFLINE
                    , 1
                    , "ㅇㅇ"
                    , null
                    , null
            );
        });

        assertEquals(ErrorCode.OFFLINE_STUDY_REGION_REQUIRED, ex.getErrorCode());
    }

    @Test
    void 기본정보수정_성공() {

        assertDoesNotThrow(() -> samplestudyGroup.updateInfo(
                "new title"
                , "new category"
                , "new topic"
                , null
                , StudyMode.ONLINE
                , 4
                , "new description"
                , null
                , null
        ));

        assertEquals("new title", samplestudyGroup.getTitle());
        assertEquals("new category", samplestudyGroup.getCategory());
        assertEquals("new topic", samplestudyGroup.getTopic());
        assertNull(samplestudyGroup.getRegion());
        assertEquals(StudyMode.ONLINE, samplestudyGroup.getStudyMode());
        assertEquals(4, samplestudyGroup.getCapacity());
        assertEquals("new description", samplestudyGroup.getDescription());
    }
}
