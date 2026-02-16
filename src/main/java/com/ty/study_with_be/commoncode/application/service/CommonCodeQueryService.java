package com.ty.study_with_be.commoncode.application.service;

import com.ty.study_with_be.commoncode.application.CommonCodeQueryUseCase;
import com.ty.study_with_be.commoncode.domain.CommonCode;
import com.ty.study_with_be.commoncode.domain.CommonCodeRepository;
import com.ty.study_with_be.commoncode.presentation.CommonCodeDto;
import com.ty.study_with_be.commoncode.presentation.res.CommonCodeTypeRes;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommonCodeQueryService implements CommonCodeQueryUseCase {

    private final CommonCodeRepository commonCodeRepository;

    @Override
    public List<CommonCodeTypeRes> getCommonCodesByTypes(List<String> types) {

        List<CommonCode> children =
                commonCodeRepository.findChildrenByParentCodes(types);

        return children.stream()
                .collect(Collectors.groupingBy(
                        code -> code.getParent().getCode()
                ))
                .entrySet()
                .stream()
                .map(entry -> new CommonCodeTypeRes(
                        entry.getKey(),
                        entry.getValue().stream()
                                .map(code -> new CommonCodeDto(
                                        code.getCode(),
                                        code.getCodeNm()
                                ))
                                .toList()
                ))
                .toList();
    }
}
