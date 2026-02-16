package com.ty.study_with_be.commoncode.infra;

import com.ty.study_with_be.commoncode.domain.CommonCode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommonCodeJpaRepository extends JpaRepository<CommonCode, Long> {

    List<CommonCode> findByUseYnTrueAndDepthAndParent_CodeIn(
            int depth,
            List<String> parentCodes
    );
}
