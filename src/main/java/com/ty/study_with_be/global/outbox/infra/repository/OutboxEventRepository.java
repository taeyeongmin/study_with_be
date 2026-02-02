package com.ty.study_with_be.global.outbox.infra.repository;

import com.ty.study_with_be.global.outbox.domain.OutboxEvent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OutboxEventRepository extends JpaRepository<OutboxEvent, Long> {

}