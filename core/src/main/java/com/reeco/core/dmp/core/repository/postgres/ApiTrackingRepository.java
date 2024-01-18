package com.reeco.core.dmp.core.repository.postgres;

import com.reeco.core.dmp.core.model.postgres.ApiTracking;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApiTrackingRepository extends JpaRepository<ApiTracking, Long> {
}
