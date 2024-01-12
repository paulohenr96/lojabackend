package com.paulo.estudandoconfig.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.paulo.estudandoconfig.model.Metrics;

@Repository
public interface MetricRepository extends JpaRepository<Metrics, Long> {

}
