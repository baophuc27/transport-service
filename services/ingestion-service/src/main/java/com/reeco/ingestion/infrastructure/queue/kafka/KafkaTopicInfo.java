package com.reeco.ingestion.infrastructure.queue.kafka;

import java.util.Objects;

public class KafkaTopicInfo {

    private final String topic;
    private final String prefix;
    private final Integer partition;

    public KafkaTopicInfo(String topic, String prefix, Integer partition) {
        this.topic = topic;
        this.partition = partition;
        this.prefix = prefix;
    }

    public String getFullTopicName() {
        if (prefix != null){
            return prefix + "_" + topic;
        }
        return topic;
    }


    public Integer getPartition() {
        return partition;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        KafkaTopicInfo that = (KafkaTopicInfo) o;
        return topic.equals(that.topic) &&
                Objects.equals(prefix, that.prefix) &&
                Objects.equals(partition, that.partition);
    }

    @Override
    public int hashCode() {
        return Objects.hash(topic, prefix, getPartition());
    }
}