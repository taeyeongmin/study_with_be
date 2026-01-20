package com.ty.study_with_be.study_group.applicaiton.query;

import com.ty.study_with_be.study_group.domain.GroupRepository;
import com.ty.study_with_be.study_group.domain.model.StudyGroup;
import com.ty.study_with_be.study_group.query.dto.MyStudyGroupStatusRes;
import com.ty.study_with_be.study_group.query.dto.StudyGroupDetailRes;
import com.ty.study_with_be.study_group.query.repository.StudyGroupQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StudyGroupQueryService {

    private final StudyGroupQueryRepository groupQueryRepository;
    private final GroupRepository groupRepository;

    public StudyGroupDetailRes getDetail(Long studyGroupId) {

        StudyGroup studyGroup = groupRepository.findById(studyGroupId).orElseThrow(RuntimeException::new);
        Set<DayOfWeek> schedules = studyGroup.getSchedules();

        StudyGroupDetailRes studyGroupDetailRes = groupQueryRepository.findDetail(studyGroupId)
                .orElseThrow(RuntimeException::new);

        studyGroupDetailRes.setSchedules(schedules);

        return studyGroupDetailRes;
    }


    public MyStudyGroupStatusRes getMyStatus(Long groupId, Long memberId) {

        if (groupQueryRepository.existsMember(groupId, memberId)) {
            return MyStudyGroupStatusRes.joined();
        }

        if (groupQueryRepository.existsPendingJoin(groupId, memberId)) {
            return MyStudyGroupStatusRes.pending();
        }

        return MyStudyGroupStatusRes.none();
    }

}
