package com.reeco.http.infrastructure.persistence.postgresql.entity;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name="http_connection_metadata")
public class HTTPConnectionMetadata {

    @Id
    private Long id;

    @Column(name = "organization_id")
    private Long organizationId;

    @Column(name = "station_id")
    private Long stationId;

    @Column(name = "workspace_id")
    private Long workspaceId;

    @Column(name = "access_token")
    private String accessToken;

    @Column(name = "maximum_timeout")
    private Integer maximumTimeout;

    @Column(name = "maximum_attachment")
    private Integer maximumAttachment;

    @Column(name = "notification_type")
    private String notificationType;

    @Column(name = "remove_after_days")
    private Long removeAfterDays;

    @Column(name = "active")
    private Boolean active;

    @Column(name = "last_active")
    private LocalDateTime lastActive = LocalDateTime.now();

    @Column(name = "is_logged_out")
    private Boolean isLoggedOut = true;
}
