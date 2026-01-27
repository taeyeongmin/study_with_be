package com.ty.study_with_be.study_notice.infra.query;

import com.ty.study_with_be.study_notice.application.query.StudyNoticeQueryRepository;
import com.ty.study_with_be.study_notice.presentation.query.dto.StudyNoticeItem;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class StudyNoticeQueryRepositoryImpl implements StudyNoticeQueryRepository {

    private final EntityManager em;

    @Override
    public List<StudyNoticeItem> findRecent(Long groupId, int limit) {
        return em.createQuery("""
            select new com.ty.study_with_be.study_notice.presentation.query.dto.StudyNoticeItem(
                n.noticeId,
                n.title,
                n.content,
                n.writerId,
                m.nickname,
                n.pinned,
                n.createdAt
            )
            from StudyNotice n
            join Member m
              on m.memberId = n.writerId
            where n.studyGroupId = :groupId
            order by n.pinned desc, n.createdAt desc
        """, StudyNoticeItem.class)
                .setParameter("groupId", groupId)
                .setMaxResults(limit)
                .getResultList();
    }

    @Override
    public List<StudyNoticeItem> findAll(Long groupId) {
        return em.createQuery("""
            select new com.ty.study_with_be.study_notice.presentation.query.dto.StudyNoticeItem(
                n.noticeId,
                n.title,
                n.content,
                n.writerId,
                m.nickname,
                n.pinned,
                n.createdAt
            )
            from StudyNotice n
            join Member m
              on m.memberId = n.writerId
            where n.studyGroupId = :groupId
            order by n.pinned desc, n.createdAt desc
        """, StudyNoticeItem.class)
                .setParameter("groupId", groupId)
                .getResultList();
    }
}
