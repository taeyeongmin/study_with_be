package com.ty.study_with_be.study_group.infra.query;

import com.ty.study_with_be.study_group.applicaiton.query.StudyGroupQueryRepository;
import com.ty.study_with_be.study_group.domain.model.enums.RecruitStatus;
import com.ty.study_with_be.study_group.domain.model.enums.StudyMode;
import com.ty.study_with_be.study_group.domain.model.enums.StudyRole;
import com.ty.study_with_be.study_group.presentation.query.dto.StudyGroupDetailRes;
import com.ty.study_with_be.study_group.presentation.query.dto.StudyGroupListItem;
import com.ty.study_with_be.study_group.presentation.query.dto.StudyMemberItem;
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
    public Optional<StudyGroupDetailRes> findDetail(Long studyGroupId) {

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
    
                sg.recruitStatus,
                sg.operationStatus,
                sg.applyDeadlineAt,
    
                m.memberId,
                m.loginId,
                m.nickname,
    
                sg.createdAt,
                sg.updatedAt
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
                .getResultStream()
                .findFirst();
    }

    @Override
    public Page<StudyGroupListItem> findStudyGroups(String category, String topic, String region, StudyMode studyMode, RecruitStatus recruitStatus, Pageable pageable) {

        //  where 구성
        StringBuilder where = new StringBuilder(" where 1=1 ");

        Map<String, Object> params = new HashMap<>();

        if (StringUtils.hasText(category)) {
            where.append(" and sg.category = :category ");
            params.put("category", category);
        }
        if (StringUtils.hasText(topic)) {
            // 요구사항: 주제 필터(보통 키워드 검색이 자연스러움)
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
        if (recruitStatus != null) {
            where.append(" and sg.recruitStatus = :recruitStatus ");
            params.put("recruitStatus", recruitStatus);
        }

        // ---- content query (목록) ----
        String contentJpql = """
            select new com.ty.study_with_be.study_group.presentation.query.dto.StudyGroupListItem(
                sg.studyGroupId,
                sg.title,
                sg.category,
                cat.codeNm,
                sg.topic,
                sg.recruitStatus,
                case sg.recruitStatus
                    when com.ty.study_with_be.study_group.domain.model.enums.RecruitStatus.RECRUITING
                        then '모집중'
                    else '모집마감'
                end,
                sg.description,
                sg.capacity,
                sg.currentCount,
                case
                    when sg.applyDeadlineAt is null then null
                    else cast(function('datediff', sg.applyDeadlineAt, current_date) as integer)
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

        // count query (총 개수)
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
}
