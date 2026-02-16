package com.ty.study_with_be.study_group.domain;

import com.ty.study_with_be.global.error.ErrorCode;
import com.ty.study_with_be.global.exception.DomainException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GroupCreatePolicyTest {

    @Mock
    GroupRepository groupRepository;

    @InjectMocks
    GroupCreatePolicy groupCreatePolicy;

    @Test
    void 생성_최대갯수_체크() {

        // given
        when(groupRepository.countActiveByMemberId(any())).thenReturn(2);

        // when , then
        DomainException ex = assertThrows(
                DomainException.class,
                () -> groupCreatePolicy.valid(1L, "title")
        );

        assertEquals(ErrorCode.TOO_MANY_CREATE_GROUP, ex.getErrorCode());
    }

    @Test
    void 그룹명_중복_검증() {

        // given
        when(groupRepository.existActiveByMemberIdAndTitle(any(),any())).thenReturn(true);

        // when , then
        DomainException ex = assertThrows(
                DomainException.class,
                () -> groupCreatePolicy.valid(1L, "title")
        );

        assertEquals(ErrorCode.DUPLICATE_GROUP_TITLE, ex.getErrorCode());
    }
}