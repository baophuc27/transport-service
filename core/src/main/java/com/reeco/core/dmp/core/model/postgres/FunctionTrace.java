package com.reeco.core.dmp.core.model.postgres;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "function_trace")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class FunctionTrace {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "function_name")
    private String functionName;

    @Column(name = "execution_time_ms")
    private Long executionTimeMilliseconds;

    @Column(name = "timestamp")
    private LocalDateTime timestamp;
}
