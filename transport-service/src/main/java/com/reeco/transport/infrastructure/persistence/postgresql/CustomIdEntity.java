package com.reeco.transport.infrastructure.persistence.postgresql;

import javax.persistence.*;

@Entity
@Table(name = "customids")
public class CustomIdEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "custom_id")
    private String customId;

    @Column(name = "original_id")
    private Integer  originalId;

    @Column(name = "id_type")
    private String customIdType;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCustomId() {
        return customId;
    }

    public void setCustomId(String customId) {
        this.customId = customId;
    }

    public Integer getOriginalId() {
        return originalId;
    }

    public void setOriginalId(Integer originalId) {
        this.originalId = originalId;
    }

    public String getCustomIdType() {
        return customIdType;
    }

    public void setCustomIdType(String customIdType) {
        this.customIdType = customIdType;
    }

    @Override
    public String toString() {
        return "CustomIdEntity{" +
                "id=" + id +
                ", customId='" + customId + '\'' +
                ", originalId=" + originalId +
                ", customIdType='" + customIdType + '\'' +
                '}';
    }
}
