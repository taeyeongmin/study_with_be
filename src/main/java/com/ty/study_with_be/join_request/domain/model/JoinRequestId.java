package com.ty.study_with_be.join_request.domain.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Embeddable
@Getter
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class JoinRequestId implements Serializable {

    @Column(name = "join_request_id")
    private Long value;

    private JoinRequestId(Long value) {
        this.value = value;
    }

    public static JoinRequestId of(Long value) {
        if (value == null) {
            throw new IllegalArgumentException("JoinRequestId cannot be null");
        }
        return new JoinRequestId(value);
    }
}