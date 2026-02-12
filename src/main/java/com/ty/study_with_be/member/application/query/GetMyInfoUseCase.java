package com.ty.study_with_be.member.application.query;

import com.ty.study_with_be.member.domain.model.AuthType;
import com.ty.study_with_be.member.domain.model.Member;
import com.ty.study_with_be.member.presentation.query.dto.MemberInfoRes;

public interface GetMyInfoUseCase {

    MemberInfoRes getMemberInfo(Long memberId);

    boolean existsSocialMember(AuthType authType, String providerUserId);

    Member findSocialMember(AuthType authType, String providerUserId);
}
