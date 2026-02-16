package com.ty.study_with_be.commoncode.infra;

import com.ty.study_with_be.commoncode.domain.CommonCode;
import com.ty.study_with_be.commoncode.domain.CommonCodeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class CommonCodeRepositoryImpl implements CommonCodeRepository {

    private final CommonCodeJpaRepository jpaRepository;

    @Override
    public List<CommonCode> findChildrenByParentCodes(List<String> parentCodes) {
        return jpaRepository.findByUseYnTrueAndDepthAndParent_CodeIn(2, parentCodes);
    }

}
