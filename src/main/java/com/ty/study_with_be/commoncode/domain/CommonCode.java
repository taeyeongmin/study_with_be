package com.ty.study_with_be.commoncode.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "common_code")
public class CommonCode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String code;

    @Column(nullable = false, length = 100)
    private String codeNm;

    @Column(name = "sort_order")
    private int sortOrder;

    @Column(name = "use_yn")
    private boolean useYn;

    @Column
    private int depth;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private CommonCode parent;

    @OneToMany(mappedBy = "parent")
    private List<CommonCode> children = new ArrayList<>();
}