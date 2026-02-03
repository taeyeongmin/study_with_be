package com.ty.study_with_be.commoncode.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "common_code")
public class CommonCode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Comment("공통코드 ID")
    private Long id;

    @Column(nullable = false, length = 50)
    @Comment("코드")
    private String code;

    @Column(nullable = false, length = 100)
    @Comment("코드명")
    private String codeNm;

    @Column(name = "sort_order")
    @Comment("정렬 순서")
    private int sortOrder;

    @Column(name = "use_yn")
    @Comment("사용 여부")
    private boolean useYn;

    @Column
    private int depth;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    @Comment("부모 코드 PK")
    private CommonCode parent;

    @OneToMany(mappedBy = "parent")
    private List<CommonCode> children = new ArrayList<>();
}
