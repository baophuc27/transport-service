package com.reeco.core.dmp.core.repository.postgres;

import com.reeco.core.dmp.core.model.postgres.FunctionTrace;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FunctionTraceRepository extends JpaRepository<FunctionTrace, Long> {
}
