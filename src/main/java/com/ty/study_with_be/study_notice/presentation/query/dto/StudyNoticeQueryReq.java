package com.ty.study_with_be.study_notice.presentation.query.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import net.minidev.json.annotate.JsonIgnore;

@Getter
@NoArgsConstructor
public class StudyNoticeQueryReq {


    private StudyNoticeQueryType type = StudyNoticeQueryType.ALL;

    /**
     * RECENT일 때만 사용
     * 기본값: 3
     */
    private Integer limit;

    public int resolveLimit() {
        if (type == StudyNoticeQueryType.RECENT) {
            return (limit == null || limit <= 0) ? 3 : limit;
        }
        return 0; // ALL일 경우 limit 미사용
    }

    @JsonIgnore
    public boolean isRecent() {
        return type == StudyNoticeQueryType.RECENT;
    }
}