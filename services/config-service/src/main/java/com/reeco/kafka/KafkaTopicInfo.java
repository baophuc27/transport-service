package com.reeco.kafka;

import java.util.Objects;

public class KafkaTopicInfo {

    private final String topic;
    private final Integer partition;

    public KafkaTopicInfo(String topic, Integer partition) {
        this.topic = topic;
        this.partition = partition;
    }

    public String getTopic(){return topic;}

    public Integer getPartition() {
        return partition;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        KafkaTopicInfo that = (KafkaTopicInfo) o;
        return topic.equals(that.topic) &&
                Objects.equals(partition, that.partition);
    }

    @Override
    public int hashCode() {
        return Objects.hash(topic, getPartition());
    }
}