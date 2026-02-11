package com.ty.study_with_be.notification.presentation.query;

import com.ty.study_with_be.notification.application.query.NotificationQueryService;
import com.ty.study_with_be.notification.presentation.query.dto.MyNotificationListReq;
import com.ty.study_with_be.notification.presentation.query.dto.MyNotificationListRes;
import com.ty.study_with_be.notification.presentation.query.dto.NotificationListRes;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.security.PermitAll;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "스터디 그룹 알림")
@RequestMapping("/api/study_group/notification")
public class NotificationQueryController {

    private final NotificationQueryService notificationQueryService;

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/my")
    @Operation(
            summary = "알림 목록 조회",
            description = """
                    ## 기능 설명
                    - 나에게 온 알림 목록 조회
                    """
    )
    @Parameter(name = "studyGroupId", description = "스터디 그룹 ID", in = ParameterIn.PATH)
    public NotificationListRes getMyNotiList(
            @AuthenticationPrincipal User user
            ){

        return notificationQueryService.getMyNotiList(Long.valueOf(user.getUsername()));
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/my/list")
    @Operation(
            summary = "나의 모든 알람 목록을 조회한다.",
            description = """
                    ## 상세 설명
                    - param에 따라 읽은, 읽지 않은 알림 모두 조회
                    """
    )
    public MyNotificationListRes getMyNotificationList(
            @AuthenticationPrincipal User principal,
            @ModelAttribute MyNotificationListReq request
    ) {
        Pageable pageable = PageRequest.of(request.getPage(), request.getSize());

        return notificationQueryService.getMyNotificationList(
                Long.valueOf(principal.getUsername()),
                request,
                pageable
        );
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/my_count")
    @Operation(
            summary = "알림 목록 조회",
            description = """
                    ## 기능 설명
                    - 나에게 온 읽지 않음 알림 갯수 조회
                    """
    )
    @Parameter(name = "studyGroupId", description = "스터디 그룹 ID", in = ParameterIn.PATH)
    public ResponseEntity<Integer> getMyNotiCount(
            @AuthenticationPrincipal User user
    ){
        int count = notificationQueryService.countByMemberId(Long.valueOf(user.getUsername()));
        return ResponseEntity.ok(count);
    }
}
