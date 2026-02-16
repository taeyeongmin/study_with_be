package com.ty.study_with_be.study_group.domain.model.enums;

public enum StudyRole {

    LEADER {
        @Override
        public boolean canKick(StudyRole targetRole) {
            // 방장은 모두 강퇴 가능
            return true;
        }
    },
    MANAGER {
        @Override
        public boolean canKick(StudyRole targetRole) {
            // 매니저는 일반 멤버만 강퇴 가능
            return targetRole == MEMBER;
        }
    },
    MEMBER {
        @Override
        public boolean canKick(StudyRole targetRole) {
            // 일반 멤버는 강퇴 권한 없음
            return false;
        }
    },
    NONE {
        @Override
        public boolean canKick(StudyRole targetRole) {

            return false;
        }
    };

    public abstract boolean canKick(StudyRole targetRole);
}