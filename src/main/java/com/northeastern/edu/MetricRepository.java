package com.northeastern.edu;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

import com.northeastern.edu.models.Metric;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

@Component("metricRepository")
public interface MetricRepository  extends JpaRepository<Metric,Integer>{
    public List<Metric> findByUserIdAndDate(int id, Date date);
}
