package com.ty.study_with_be.study_group.domain;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * - 한 회원은 모집중 또는 진행중 상태의 스터디 그룹을 최대 N개까지만 생성할 수 있다.
 * - 한 회원은 모집중 또는 진행중 상태의 스터디 그룹을 동일한 이름으로 생성할 수 없다.
 *
 * 두 조건 검증을 위해 각각 쿼리를 조회
 *  - 한번만 조회를 하기 위해 memberId로 모든 그룹을 가져오게 되면 너무 많은 데이터가 나올 수 있음. (성능 저하, 동시성 이슈 확률 증가)
 *  - 활성의 정의(모집중, 진행중)를 한 곳에 가둬서 변경을 국소화
 *  - 각각 다른 repo 메서드를 호출해야 테스트 코드 작성시 용이함.
 *      - 한번에 조회 하면 두 케이스를 각각 테스트 하기 어려워짐.
 *
 */
@Component
@RequiredArgsConstructor
public class GroupCreatePolicy {

    private final GroupRepository groupRepository;

    public void valid(Long creatMemberId, String title){

        groupRepository.countActiveByMemberId(creatMemberId);
        groupRepository.existActiveByMemberIdAndTitle(creatMemberId, title);

    }

}
