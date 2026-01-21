package com.ty.study_with_be.commoncode.presentation.res;

import com.ty.study_with_be.commoncode.presentation.CommonCodeDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class CommonCodeTypeRes {

    private String type;
    private List<CommonCodeDto> items;
}