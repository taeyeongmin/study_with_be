//package com.ty.study_with_be.notification.presentation.query;
//
//import com.ty.study_with_be.notification.application.query.NotificationQueryService;
//import com.ty.study_with_be.study_group.presentation.query.dto.StudyGroupDetailRes;
//import io.swagger.v3.oas.annotations.Operation;
//import io.swagger.v3.oas.annotations.Parameter;
//import io.swagger.v3.oas.annotations.enums.ParameterIn;
//import io.swagger.v3.oas.annotations.tags.Tag;
//import jakarta.annotation.security.PermitAll;
//import lombok.RequiredArgsConstructor;
//import org.springframework.security.core.annotation.AuthenticationPrincipal;
//import org.springframework.security.core.userdetails.User;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController
//@RequestMapping("/api/study_group/notification")
//@RequiredArgsConstructor
//@Tag(name = "스터디 그룹 알림")
//public class NotificationController {
//
//    private final NotificationQueryService notificationQueryService;
//
//    @PermitAll
//    @GetMapping("/my")
//    @Operation(
//            summary = "알림 목록 조회",
//            description = """
//                    ## 기능 설명
//                    - 나에게 온 알림 목록 조회
//                    """
//    )
//    @Parameter(name = "studyGroupId", description = "스터디 그룹 ID", in = ParameterIn.PATH)
//    public StudyGroupDetailRes getMyNotiList(
//            @AuthenticationPrincipal User user
//            ){
//
//
//    }
//}
