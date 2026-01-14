package com.ty.study_with_be.commoncode.domain;

import java.util.List;

public interface CommonCodeRepository {
    List<CommonCode> findChildrenByParentCodes(List<String> parentCodes);
}
