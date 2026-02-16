package com.ty.study_with_be.member.presentation.query;

import com.ty.study_with_be.member.application.query.GetMyInfoService;
import com.ty.study_with_be.member.presentation.query.dto.MemberInfoRes;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/member")
@Tag(name = "회원", description = "회원 API")
public class MemberQueryController {

    private final GetMyInfoService getMyInfoService;

    @PreAuthorize("isAuthenticated()")
    @Operation(
            summary = "나의 정보 조회",
            description = """
                    ## 기능 설명
                    - 로그인 정보를 가져오기 위한 API로 회원의 기본 정보를 조회한다.
                    """
    )
    @GetMapping("/me")
    public ResponseEntity<MemberInfoRes> memberInfo(@AuthenticationPrincipal User principal){
        Long memberId = Long.parseLong(principal.getUsername());
        MemberInfoRes memberInfo = getMyInfoService.getMemberInfo(memberId);

        return ResponseEntity.status(HttpStatus.OK).body(memberInfo);
    }

}
