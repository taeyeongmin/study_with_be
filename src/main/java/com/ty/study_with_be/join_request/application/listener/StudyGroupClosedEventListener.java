package com.ty.study_with_be.join_request.application.listener;

import com.ty.study_with_be.join_request.application.command.RejectAllJoinRequestUseCase;
import com.ty.study_with_be.study_group.domain.event.RecruitmentEndEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

/**
 * 그룹의 운영 종료 이벤트를 수신하여 가입 신청에 대한 처리를 위한 리스너
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class StudyGroupClosedEventListener {

    private final RejectAllJoinRequestUseCase useCase;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void on(RecruitmentEndEvent event) {

        try {
            useCase.rejectAll(event.getStudyGroupId(),event.getProcessorMemberId());
        } catch (Exception e) {
            log.error("운영 종료에 따른 거절 처리 중 문제 발생");
            log.error(e.getMessage(), e);
        }
    }
}
