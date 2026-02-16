package com.ty.study_with_be.commoncode.application;

import com.ty.study_with_be.commoncode.presentation.res.CommonCodeTypeRes;

import java.util.List;

public interface CommonCodeQueryUseCase {
    List<CommonCodeTypeRes> getCommonCodesByTypes(List<String> types);
}
