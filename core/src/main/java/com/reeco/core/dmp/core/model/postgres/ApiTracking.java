package com.reeco.core.dmp.core.model.postgres;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name="api_tracking")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ApiTracking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "api_endpoint")
    private String apiEndpoint;

    @Column(name = "status_code")
    private Integer statusCode;

    @Column(name = "response_time_ms")
    private Long responseTimeMilliseconds;

    @Column(name = "timestamp")
    private LocalDateTime timestamp;

}
