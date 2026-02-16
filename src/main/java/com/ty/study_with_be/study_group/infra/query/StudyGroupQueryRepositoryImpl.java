package com.ty.study_with_be.study_group.infra.query;

import com.ty.study_with_be.study_group.applicaiton.query.StudyGroupQueryRepository;
import com.ty.study_with_be.study_group.domain.model.enums.OperationStatus;
import com.ty.study_with_be.study_group.domain.model.enums.RecruitStatus;
import com.ty.study_with_be.study_group.domain.model.enums.StudyMode;
import com.ty.study_with_be.study_group.domain.model.enums.StudyRole;
import com.ty.study_with_be.study_group.presentation.query.dto.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class StudyGroupQueryRepositoryImpl implements StudyGroupQueryRepository {

    private final EntityManager em;

    @Override
    public Optional<StudyGroupDetailRes> findDetail(Long studyGroupId, Long currentMemberId) {

        return em.createQuery("""
        select new com.ty.study_with_be.study_group.presentation.query.dto.StudyGroupDetailRes(
            sg.studyGroupId,
            sg.title,

            sg.category,
            cat.codeNm,

            sg.topic,

            sg.region,
            reg.codeNm,

            sg.studyMode,
            sg.capacity,
            sg.currentCount,
            sg.description,
            case
                when sg.recruitStatus =
                    com.ty.study_with_be.study_group.domain.model.enums.RecruitStatus.RECRUITING
                     and (sg.applyDeadlineAt is null or sg.applyDeadlineAt >= current_date)
                     and sg.currentCount < sg.capacity
                then com.ty.study_with_be.study_group.domain.model.enums.RecruitStatus.RECRUITING
                else com.ty.study_with_be.study_group.domain.model.enums.RecruitStatus.RECRUIT_END
            end,
            sg.operationStatus,
            sg.applyDeadlineAt,

            m.memberId,
            m.loginId,
            m.nickname,

            sg.createdAt,
            sg.updatedAt,

            /* canRequestJoin */
            case
                when :currentMemberId is null then false                
                when not (
                    sg.recruitStatus =
                        com.ty.study_with_be.study_group.domain.model.enums.RecruitStatus.RECRUITING
                    and (sg.applyDeadlineAt is null or sg.applyDeadlineAt >= current_date)
                    and sg.currentCount < sg.capacity
                ) then false
                when exists (
                    select sm.studyMemberId
                    from StudyMember sm
                    where sm.studyGroup.studyGroupId = sg.studyGroupId
                      and sm.memberId = :currentMemberId
                ) then false
                when exists (
                    select jr.joinRequestId
                    from JoinRequest jr
                    where jr.studyGroupId = sg.studyGroupId
                      and jr.requesterId = :currentMemberId
                      and jr.status =
                        com.ty.study_with_be.join_request.domain.model.enums.JoinRequestStatus.PENDING
                ) then false
                else true
            end,

            /* canCloseRecruit (방장만) */
            case
                when :currentMemberId is null then false
                when sg.recruitStatus <>
                    com.ty.study_with_be.study_group.domain.model.enums.RecruitStatus.RECRUITING
                then false
                when exists (
                    select sm
                    from StudyMember sm
                    where sm.studyGroup.studyGroupId = sg.studyGroupId
                      and sm.memberId = :currentMemberId
                      and sm.role =
                          com.ty.study_with_be.study_group.domain.model.enums.StudyRole.LEADER
                ) then true
                else false
            end,

            /* canReopenRecruit (방장만) */
            case
               when :currentMemberId is null then false
               when sg.recruitStatus <>
                   com.ty.study_with_be.study_group.domain.model.enums.RecruitStatus.RECRUIT_END
               then false
               when (sg.applyDeadlineAt is not null and sg.applyDeadlineAt < current_date) then false
               when sg.currentCount >= sg.capacity then false
               when exists (
                   select sm
                   from StudyMember sm
                   where sm.studyGroup.studyGroupId = sg.studyGroupId
                     and sm.memberId = :currentMemberId
                     and sm.role =
                         com.ty.study_with_be.study_group.domain.model.enums.StudyRole.LEADER
               ) then true
               else false
            end
        )
        from StudyGroup sg
        join Member m
            on m.memberId = sg.ownerId
        left join CommonCode cat
            on cat.code = sg.category
           and cat.useYn = true
           and cat.depth = 2
        left join CommonCode reg
            on reg.code = sg.region
           and reg.useYn = true
           and reg.depth = 2
        where sg.studyGroupId = :id
    """, StudyGroupDetailRes.class)
                .setParameter("id", studyGroupId)
                .setParameter("currentMemberId", currentMemberId)
                .getResultStream()
                .findFirst();
    }

    @Override
    public Page<StudyGroupListItem> findStudyGroups(
            String title,
            String category,
            String topic,
            String region,
            StudyMode studyMode,
            RecruitStatus recruitStatus,
            Pageable pageable,
            Long currentMemberId
            ) {

        StringBuilder where = new StringBuilder(" where 1=1 ");
        Map<String, Object> params = new HashMap<>();

        // -----------------------------
        // 기본 필터
        // -----------------------------
        if (StringUtils.hasText(title)) {
            where.append(" and sg.title like :title ");
            params.put("title", "%" + title.trim() + "%");
        }

        if (StringUtils.hasText(category)) {
            where.append(" and sg.category = :category ");
            params.put("category", category);
        }

        if (StringUtils.hasText(topic)) {
            where.append(" and sg.topic like :topic ");
            params.put("topic", "%" + topic.trim() + "%");
        }

        if (StringUtils.hasText(region)) {
            where.append(" and sg.region = :region ");
            params.put("region", region);
        }

        if (studyMode != null) {
            where.append(" and sg.studyMode = :studyMode ");
            params.put("studyMode", studyMode);
        }

        // -----------------------------
        // 모집 상태 필터 (연산 기반)
        // -----------------------------
        if (recruitStatus != null) {

            if (recruitStatus == RecruitStatus.RECRUITING) {

                where.append("""
                and sg.recruitStatus = :recruitingStatus
                and (sg.applyDeadlineAt is null or sg.applyDeadlineAt >= current_date)
                and sg.currentCount < sg.capacity
            """);

                params.put("recruitingStatus", RecruitStatus.RECRUITING);

            } else if (recruitStatus == RecruitStatus.RECRUIT_END) {

                where.append("""
                and (
                        sg.recruitStatus <> :recruitingStatus
                     or (sg.applyDeadlineAt is not null and sg.applyDeadlineAt < current_date)
                     or sg.currentCount >= sg.capacity
                    )
            """);

                params.put("recruitingStatus", RecruitStatus.RECRUITING);
            }
        }

        // -----------------------------
        // 운영 상태 필터
        // CLOSED는 방장/매니저만 조회 가능
        // -----------------------------
        if (currentMemberId == null) {

            // 비로그인은 ONGOING만 조회 가능
            where.append("""
            and sg.operationStatus =
                com.ty.study_with_be.study_group.domain.model.enums.OperationStatus.ONGOING
        """);

        } else {

            where.append("""
            and (
                    sg.operationStatus =
                        com.ty.study_with_be.study_group.domain.model.enums.OperationStatus.ONGOING
                 or (
                        sg.operationStatus =
                            com.ty.study_with_be.study_group.domain.model.enums.OperationStatus.CLOSED
                        and exists (
                            select sm.studyMemberId
                            from StudyMember sm
                            where sm.studyGroup.studyGroupId = sg.studyGroupId
                              and sm.memberId = :currentMemberId
                              and sm.role in (
                                  com.ty.study_with_be.study_group.domain.model.enums.StudyRole.LEADER
                              )
                        )
                    )
                )
        """);

            params.put("currentMemberId", currentMemberId);
        }

        // -----------------------------
        // Content Query
        // -----------------------------
        String contentJpql = """
        select new com.ty.study_with_be.study_group.presentation.query.dto.StudyGroupListItem(
            sg.studyGroupId,
            sg.title,
            sg.category,
            cat.codeNm,
            sg.topic,
            sg.recruitStatus,
            case
                when sg.recruitStatus =
                    com.ty.study_with_be.study_group.domain.model.enums.RecruitStatus.RECRUITING
                     and (sg.applyDeadlineAt is null or sg.applyDeadlineAt >= current_date)
                     and sg.currentCount < sg.capacity
                then '모집중'
                else '모집마감'
            end,
            sg.operationStatus,
            sg.description,
            sg.capacity,
            sg.currentCount,
            case
                when sg.applyDeadlineAt is null then null
                else cast(function('datediff', sg.applyDeadlineAt, current_date) as integer)
            end,
            case
                when :currentMemberId is null then false
                when exists (
                    select sm.studyMemberId
                    from StudyMember sm
                    where sm.studyGroup.studyGroupId = sg.studyGroupId
                      and sm.memberId = :currentMemberId
                )
                then true
                else false
            end
        )
        from StudyGroup sg
        left join CommonCode cat
            on cat.code = sg.category
           and cat.useYn = true
           and cat.depth = 2
    """ + where + """
        order by sg.createdAt desc
    """;

        TypedQuery<StudyGroupListItem> contentQuery =
                em.createQuery(contentJpql, StudyGroupListItem.class);

        params.forEach(contentQuery::setParameter);

        contentQuery.setFirstResult((int) pageable.getOffset());
        contentQuery.setMaxResults(pageable.getPageSize());

        List<StudyGroupListItem> content = contentQuery.getResultList();

        // -----------------------------
        // Count Query
        // -----------------------------
        String countJpql = """
            select count(sg.studyGroupId)
            from StudyGroup sg
        """ + where;

        TypedQuery<Long> countQuery = em.createQuery(countJpql, Long.class);
        params.forEach(countQuery::setParameter);

        long total = countQuery.getSingleResult();

        return new PageImpl<>(content, pageable, total);
    }


    @Override
    public Optional<StudyRole> findRole(Long groupId, Long memberId) {
        List<StudyRole> result = em.createQuery("""
            select sm.role
            from StudyMember sm
            where sm.studyGroup.studyGroupId = :groupId
              and sm.memberId = :memberId
        """, StudyRole.class)
                .setParameter("groupId", groupId)
                .setParameter("memberId", memberId)
                .getResultList();

        return result.stream().findFirst();
    }

    @Override
    public boolean hasManagerRole(Long studyGroupId, Long memberId) {

        Long count = em.createQuery("""
        select count(sm)
        from StudyMember sm
        where sm.studyGroup.studyGroupId = :studyGroupId
          and sm.memberId = :memberId
          and sm.role in (:roles)
    """, Long.class)
                .setParameter("studyGroupId", studyGroupId)
                .setParameter("memberId", memberId)
                .setParameter("roles", List.of(StudyRole.LEADER, StudyRole.MANAGER))
                .getSingleResult();

        return count > 0;
    }

    @Override
    public List<StudyMemberItem> findStudyMemberList(Long studyGroupId) {

        return em.createQuery("""
            select new com.ty.study_with_be.study_group.presentation.query.dto.StudyMemberItem(
                sm.studyMemberId,
                m.nickname,
                m.email,
                sm.role,
                sm.createdAt
            )
            from StudyMember sm
            join Member m
                on sm.memberId = m.memberId
            where sm.studyGroup.studyGroupId = :studyGroupId
        """, StudyMemberItem.class)
                .setParameter("studyGroupId", studyGroupId)
                .getResultList();
    }

    @Override
    public List<Long> findManagers(Long studyGroupId) {
        return em.createQuery("""
            select sm.memberId
            from StudyMember sm
            where sm.studyGroup.studyGroupId = :groupId
              and sm.role in (:roles)
        """, Long.class)
                .setParameter("groupId", studyGroupId)
                .setParameter("roles", List.of(StudyRole.LEADER, StudyRole.MANAGER))
                .getResultList();
    }

    @Override
    public List<Long> findAllMember(Long studyGroupId) {
        return em.createQuery("""
            select sm.memberId
            from StudyMember sm
            where sm.studyGroup.studyGroupId = :groupId
              and sm.role in (:roles)
        """, Long.class)
                .setParameter("groupId", studyGroupId)
                .setParameter("roles", List.of(StudyRole.LEADER, StudyRole.MANAGER,StudyRole.MEMBER))
                .getResultList();
    }

    @Override
    public Optional<Long> findLeaderId(Long studyGroupId) {
        List<Long> resultList = em.createQuery("""
                            select sm.memberId
                            from StudyMember sm
                            where sm.studyGroup.studyGroupId = :groupId
                              and sm.role in (:roles)
                        """, Long.class)
                .setParameter("groupId", studyGroupId)
                .setParameter("roles", List.of(StudyRole.LEADER))
                .getResultList();

        return resultList.stream().findFirst();
    }


    @Override
    public boolean existsMember(Long studyGroupId, Long memberId) {

        Long count = em.createQuery("""
            select count(sm)
            from StudyMember sm
            where sm.studyGroup.studyGroupId = :groupId
              and sm.memberId = :memberId
        """, Long.class)
                .setParameter("groupId", studyGroupId)
                .setParameter("memberId", memberId)
                .getSingleResult();

        return count > 0;
    }

    /** 내가 참여중인 그룹 갯수 조회 (방장X) */
    public int countByMemberIdJoined(Long memberId) {

        Long count = em.createQuery("""
            select count(sm)
            from StudyMember sm
            join StudyGroup sg
                on sm.studyGroup.studyGroupId = sg.studyGroupId
                and sm.role != :role
            where sm.memberId = :memberId
                and sg.operationStatus != :operationStatus
        """, Long.class)
                .setParameter("memberId", memberId)
                .setParameter("operationStatus", OperationStatus.CLOSED)
                .setParameter("role", StudyRole.LEADER)
                .getSingleResult();

        return count.intValue();
    }

    /** 내가 운영중인 그룹 갯수 조회 (방장O) */
    public int countByMemberIdOperate(Long memberId) {

        Long count = em.createQuery("""
            select count(sg)
            from StudyMember sm
            join StudyGroup sg
                on sm.studyGroup.studyGroupId = sg.studyGroupId
                and sm.role = :role
            where sm.memberId = :memberId
                and sg.operationStatus != :operationStatus
        """, Long.class)
                .setParameter("memberId", memberId)
                .setParameter("operationStatus", OperationStatus.CLOSED)
                .setParameter("role", StudyRole.LEADER)
                .getSingleResult();

        return count.intValue();
    }

    @Override
    public Page<MyStudyGroupListItem> findMyStudyGroups(
            Long memberId,
            List<MyStudyGroupOperationFilter> operationFilter,
            List<StudyRole> roleFilter,
            Pageable pageable
    ) {

        StringBuilder where = new StringBuilder(" where sm.memberId = :memberId ");
        Map<String, Object> params = new HashMap<>();
        params.put("memberId", memberId);

        if (operationFilter != null && !operationFilter.isEmpty()) {
            List<OperationStatus> operationStatuses = operationFilter.stream()
                    .map(filter -> OperationStatus.valueOf(filter.name()))
                    .toList();

            where.append(" and sg.operationStatus in(:operationStatus) ");
            params.put("operationStatus", operationStatuses);
        }

        if (roleFilter != null && !roleFilter.isEmpty()) {
            where.append(" and sm.role in (:roleFilter) ");
            params.put("roleFilter", roleFilter);
        }

        String contentQuery = """
        select new com.ty.study_with_be.study_group.presentation.query.dto.MyStudyGroupListItem(
           sg.studyGroupId,
                sg.title,
                sm.role,
                sg.category,
                cat.codeNm,
                sg.topic,
                sg.recruitStatus,
                case sg.recruitStatus
                    when com.ty.study_with_be.study_group.domain.model.enums.RecruitStatus.RECRUITING
                        then '모집중'
                    else '모집마감'
                end,
                sg.operationStatus,
                sg.capacity,
                sg.currentCount
        )
        from StudyMember sm
        join sm.studyGroup sg
        left join CommonCode cat
            on cat.code = sg.category
           and cat.useYn = true
           and cat.depth = 2
    """ + where + """
        order by sm.createdAt desc
    """;

        TypedQuery<MyStudyGroupListItem> query =
                em.createQuery(contentQuery, MyStudyGroupListItem.class);

        params.forEach(query::setParameter);
        query.setFirstResult((int) pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());

        List<MyStudyGroupListItem> content = query.getResultList();

        String countQuery = """
        select count(sg.studyGroupId)
        from StudyMember sm
        join sm.studyGroup sg
    """ + where;

        TypedQuery<Long> countTypedQuery = em.createQuery(countQuery, Long.class);
        params.forEach(countTypedQuery::setParameter);

        long total = countTypedQuery.getSingleResult();

        return new PageImpl<>(content, pageable, total);
    }

    @Override
    public Page<StudyGroupListItem> findPopularStudyGroups(Pageable pageable) {

        StringBuilder where = new StringBuilder(" where 1=1 ");
        Map<String, Object> params = new HashMap<>();

        where.append("""
            and sg.recruitStatus = com.ty.study_with_be.study_group.domain.model.enums.RecruitStatus.RECRUITING
            and (sg.applyDeadlineAt is null or sg.applyDeadlineAt >= current_date)
            and sg.currentCount < sg.capacity
            and sg.operationStatus = com.ty.study_with_be.study_group.domain.model.enums.OperationStatus.ONGOING
        """);

        String contentJpql = """
            select new com.ty.study_with_be.study_group.presentation.query.dto.StudyGroupListItem(
                sg.studyGroupId,
                sg.title,
                sg.category,
                cat.codeNm,
                sg.topic,
                sg.recruitStatus,
                '모집중',
                sg.operationStatus,
                sg.description,
                sg.capacity,
                sg.currentCount,
                case
                    when sg.applyDeadlineAt is null then null
                    else cast(function('datediff', sg.applyDeadlineAt, current_date) as integer)
                end,
                false
            )
            from StudyGroup sg
            left join CommonCode cat
                on cat.code = sg.category
               and cat.useYn = true
               and cat.depth = 2
        """ + where + """
            order by
                case
                    when sg.applyDeadlineAt is not null
                     and function('datediff', sg.applyDeadlineAt, current_date) between 0 and 3
                    then 1
                    else 0
                end desc,
                case
                    when sg.capacity is null or sg.capacity = 0 then 0.0
                    else (1.0 * sg.currentCount / sg.capacity)
                end desc,
                sg.createdAt desc
        """;

        TypedQuery<StudyGroupListItem> contentQuery =
                em.createQuery(contentJpql, StudyGroupListItem.class);
        params.forEach(contentQuery::setParameter);
        contentQuery.setFirstResult((int) pageable.getOffset());
        contentQuery.setMaxResults(pageable.getPageSize());

        List<StudyGroupListItem> content = contentQuery.getResultList();

        String countJpql = """
            select count(sg.studyGroupId)
            from StudyGroup sg
        """ + where;

        TypedQuery<Long> countQuery = em.createQuery(countJpql, Long.class);
        params.forEach(countQuery::setParameter);

        long total = countQuery.getSingleResult();

        return new PageImpl<>(content, pageable, total);
    }

}

