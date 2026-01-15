package com.ty.study_with_be.commoncode.presentation;

import com.ty.study_with_be.commoncode.application.CommonCodeQueryUseCase;
import com.ty.study_with_be.commoncode.presentation.res.CommonCodeTypeRes;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.Explode;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/common-code")
@Tag(name = "공통코드", description = "공통코드 API")
public class CommonCodeController {

    private final CommonCodeQueryUseCase commonCodeQueryUseCase;


    @Operation(
            summary = "공통 코드 조회",
            description = """
                    ## 기능 설명
                    - 1뎁스 코드를 리스트로 받아 해당 되는 공통 코드 목록을 리턴
                    ---
                    ## 파라미터 설명
                    - types : 공통 코드 1뎁스 코드
                    ---
                  
                    """
    )
    @Parameters({
            @Parameter(
                    name = "types",
                    description = "공통 코드 1뎁스 코드 ,ex) types=category,region",
                    in = ParameterIn.QUERY,
                    array = @ArraySchema(
                            schema = @Schema(
                                    type = "string"
                            )
                    ),
                    explode = Explode.TRUE
            ),
    })
    @GetMapping
    public List<CommonCodeTypeRes> getCommonCodes(
            @RequestParam List<String> types
    ) {
        return commonCodeQueryUseCase.getCommonCodesByTypes(types);
    }

}
