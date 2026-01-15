package com.ty.study_with_be.study_group.presentation;

import com.ty.study_with_be.study_group.applicaiton.CreateGroupUseCase;
import com.ty.study_with_be.study_group.presentation.req.StudyGroupReq;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.Explode;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/study_group")
@RestController
@RequiredArgsConstructor
@Tag(name = "스터디 그룹", description = "스터디 그룹 생성/조회/관리 API")
public class StudyGroupMngController {

    private final CreateGroupUseCase createGroupUseCase;

    @Operation(
            summary = "스터디 그룹 생성",
            description = """
                    ## 기능 설명
                    - 스터디 그룹을 생성한다.
                    ---
                    ## 상세 설명
                    - 한 회원은 모집중 또는 진행중 상태의 스터디 그룹을 최대 N개까지만 생성할 수 있다.
                    - 스터디 정원은 2명 이상 N명 이하만 허용된다.
                    - 오프라인 스터디의 경우 지역 정보는 필수이다.
                    - 한 회원은 모집중 또는 진행중 상태의 스터디 그룹을 동일한 이름으로 생성할 수 없다.
                    ---
                  
                    """
    )
    @PostMapping
    public ResponseEntity createGroup(@Valid @RequestBody StudyGroupReq studyGroupReq){

        createGroupUseCase.create(studyGroupReq);

        return null;
    }
}
