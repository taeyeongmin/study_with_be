package com.ty.study_with_be.study_group.infra;

import com.ty.study_with_be.study_group.domain.model.enums.JoinRequestStatus;
import com.ty.study_with_be.study_group.query.dto.StudyGroupDetailRes;
import com.ty.study_with_be.study_group.query.repository.StudyGroupQueryRepository;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class StudyGroupQueryRepositoryImpl implements StudyGroupQueryRepository {

    private final EntityManager em;

    @Override
    public Optional<StudyGroupDetailRes> findDetail(Long studyGroupId) {

        return em.createQuery("""
            select new com.ty.study_with_be.study_group.query.dto.StudyGroupDetailRes(
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
    public boolean existsMember(Long studyGroupId, Long memberId) {

        Long count = em.createQuery("""
            select count(sm)
            from StudyMember sm
            where sm.studyGroup.studyGroupId = :groupId
              and sm.member.memberId = :memberId
        """, Long.class)
                .setParameter("groupId", studyGroupId)
                .setParameter("memberId", memberId)
                .getSingleResult();

        return count > 0;
    }

    @Override
    public boolean existsPendingJoin(Long studyGroupId, Long memberId) {

        Long count = em.createQuery("""
            select count(jr)
            from JoinRequest jr
            where jr.studyGroupId = :studyGroupId
              and jr.requesterId = :memberId
              and jr.status = :status
        """, Long.class)
                .setParameter("studyGroupId", studyGroupId)
                .setParameter("memberId", memberId)
                .setParameter("status", JoinRequestStatus.PENDING)
                .getSingleResult();

        return count > 0;
    }
}
