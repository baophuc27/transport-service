CREATE KEYSPACE IF NOT exists reecotech
WITH REPLICATION = {
'class' :'SimpleStrategy',
'replication_factor':1
};

CREATE TABLE reecotech.indicators (
  indicator_id bigint PRIMARY KEY,
  created_at timestamp,
  group_id bigint,
  indicator_name text,
  indicator_name_vi text,
  standard_unit text,
  updated_at timestamp,
  value_type text
) WITH bloom_filter_fp_chance = 0.01
AND caching = {'keys': 'ALL', 'rows_per_partition': 'ALL'}
AND comment = ''
AND compaction = {'class': 'SizeTieredCompactionStrategy'}
AND compression = {'sstable_compression': 'org.apache.cassandra.io.compress.LZ4Compressor'}
AND crc_check_chance = 1.0
AND dclocal_read_repair_chance = 0.0
AND default_time_to_live = 0
AND gc_grace_seconds = 864000
AND max_index_interval = 2048
AND memtable_flush_period_in_ms = 0
AND min_index_interval = 128
AND read_repair_chance = 0.0
AND speculative_retry = '99.0PERCENTILE';

CREATE TABLE reecotech.alarm (
  organization_id bigint,
  param_id bigint,
  alarm_id bigint,
  alarm_type text,
  frequence bigint,
  frequence_type text,
  indicator_id bigint,
  maintain_type text,
  max_value text,
  min_value text,
  num_of_match bigint,
  updated_at timestamp,
  PRIMARY KEY (organization_id, param_id, alarm_id)
) WITH CLUSTERING ORDER BY (param_id ASC, alarm_id ASC)
AND bloom_filter_fp_chance = 0.01
AND caching = {'keys': 'ALL', 'rows_per_partition': 'ALL'}
AND comment = ''
AND compaction = {'class': 'SizeTieredCompactionStrategy'}
AND compression = {'sstable_compression': 'org.apache.cassandra.io.compress.LZ4Compressor'}
AND crc_check_chance = 1.0
AND dclocal_read_repair_chance = 0.0
AND default_time_to_live = 0
AND gc_grace_seconds = 864000
AND max_index_interval = 2048
AND memtable_flush_period_in_ms = 0
AND min_index_interval = 128
AND read_repair_chance = 0.0
AND speculative_retry = '99.0PERCENTILE';

CREATE TABLE reecotech.params_by_organization (
  organization_id bigint,
  param_id bigint,
  connection_id bigint,
  display_type text,
  english_name text,
  format text,
  indicator_id bigint,
  last_agg_time timestamp,
  param_name text,
  parameter_type text,
  station_id bigint,
  unit text,
  vietnamese_name text,
  workspace_id bigint,
  PRIMARY KEY (organization_id, param_id)
) WITH CLUSTERING ORDER BY (param_id ASC)
AND bloom_filter_fp_chance = 0.01
AND caching = {'keys': 'ALL', 'rows_per_partition':'ALL'}
AND comment = ''
AND compaction = {'class':'SizeTieredCompactionStrategy'}
AND compression = {'sstable_compression':'org.apache.cassandra.io.compress.LZ4Compressor'}
AND crc_check_chance = 1.0
AND dclocal_read_repair_chance = 0.0
AND default_time_to_live = 0
AND gc_grace_seconds = 864000
AND max_index_interval = 2048
AND memtable_flush_period_in_ms = 0
AND min_index_interval = 128
AND read_repair_chance = 0.0
AND speculative_retry = '99.0PERCENTILE';


CREATE TABLE reecotech.numeric_series_by_organization (
  organization_id bigint,
  param_id bigint,
  event_time timestamp,
  alarm_id bigint,
  alarm_type text,
  connection_id bigint,
  date date,
  indicator_id bigint,
  indicator_name text,
  is_alarm boolean,
  lat double,
  lon double,
  max_value text,
  min_value text,
  param_name text,
  received_at timestamp,
  sent_at timestamp,
  station_id bigint,
  value double,
  workspace_id bigint,
  PRIMARY KEY (organization_id, param_id, event_time)
) WITH CLUSTERING ORDER BY (param_id ASC, event_time DESC)
AND bloom_filter_fp_chance = 0.01
AND caching = {'keys': 'ALL', 'rows_per_partition': 'ALL'}
AND comment = ''
AND compaction = {'class': 'SizeTieredCompactionStrategy'}
AND compression = {'sstable_compression': 'org.apache.cassandra.io.compress.LZ4Compressor'}
AND crc_check_chance = 1.0
AND dclocal_read_repair_chance = 0.0
AND default_time_to_live = 0
AND gc_grace_seconds = 864000
AND max_index_interval = 2048
AND memtable_flush_period_in_ms = 0
AND min_index_interval = 128
AND read_repair_chance = 0.0
AND speculative_retry = '99.0PERCENTILE';

CREATE TABLE reecotech.categorical_series_by_organization (
  organization_id bigint,
  param_id bigint,
  event_time timestamp,
  value text,
  alarm_id bigint,
  alarm_type text,
  connection_id bigint,
  date date,
  indicator_id bigint,
  indicator_name text,
  is_alarm boolean,
  lat double,
  lon double,
  max_value text,
  min_value text,
  param_name text,
  received_at timestamp,
  sent_at timestamp,
  station_id bigint,
  workspace_id bigint,
  PRIMARY KEY (organization_id, param_id, event_time, value)
) WITH CLUSTERING ORDER BY (param_id ASC, event_time DESC, value ASC)
AND bloom_filter_fp_chance = 0.01
AND caching = {'keys': 'ALL', 'rows_per_partition': 'ALL'}
AND comment = ''
AND compaction = {'class': 'SizeTieredCompactionStrategy'}
AND compression = {'sstable_compression': 'org.apache.cassandra.io.compress.LZ4Compressor'}
AND crc_check_chance = 1.0
AND dclocal_read_repair_chance = 0.0
AND default_time_to_live = 0
AND gc_grace_seconds = 864000
AND max_index_interval = 2048
AND memtable_flush_period_in_ms = 0
AND min_index_interval = 128
AND read_repair_chance = 0.0
AND speculative_retry = '99.0PERCENTILE';

CREATE TABLE reecotech.numeric_stats_by_organization (
  organization_id bigint,
  date date,
  param_id bigint,
  acc double,
  alarm_id bigint,
  alarm_type text,
  count bigint,
  is_alarm boolean,
  last_updated timestamp,
  max double,
  max_value text,
  mean double,
  median double,
  min double,
  min_value text,
  std double,
  PRIMARY KEY (organization_id, date, param_id)
) WITH CLUSTERING ORDER BY (date DESC, param_id ASC)
AND bloom_filter_fp_chance = 0.01
AND caching = {'keys': 'ALL', 'rows_per_partition': 'ALL'}
AND comment = ''
AND compaction = {'class': 'SizeTieredCompactionStrategy'}
AND compression = {'sstable_compression': 'org.apache.cassandra.io.compress.LZ4Compressor'}
AND crc_check_chance = 1.0
AND dclocal_read_repair_chance = 0.0
AND default_time_to_live = 0
AND gc_grace_seconds = 864000
AND max_index_interval = 2048
AND memtable_flush_period_in_ms = 0
AND min_index_interval = 128
AND read_repair_chance = 0.0
AND speculative_retry = '99.0PERCENTILE';


CREATE TABLE reecotech.categorical_stats_by_organization (
  organization_id bigint,
  date date,
  param_id bigint,
  value text,
  alarm_id bigint,
  alarm_type text,
  count bigint,
  is_alarm boolean,
  last_updated timestamp,
  max_value text,
  min_value text,
  PRIMARY KEY (organization_id, date, param_id, value)
) WITH CLUSTERING ORDER BY (date DESC, param_id ASC, value ASC)
AND bloom_filter_fp_chance = 0.01
AND caching = {'keys': 'ALL', 'rows_per_partition': 'ALL'}
AND comment = ''
AND compaction = {'class': 'SizeTieredCompactionStrategy'}
AND compression = {'sstable_compression': 'org.apache.cassandra.io.compress.LZ4Compressor'}
AND crc_check_chance = 1.0
AND dclocal_read_repair_chance = 0.0
AND default_time_to_live = 0
AND gc_grace_seconds = 864000
AND max_index_interval = 2048
AND memtable_flush_period_in_ms = 0
AND min_index_interval = 128
AND read_repair_chance = 0.0
AND speculative_retry = '99.0PERCENTILE';

CREATE TABLE reecotech.event_statistics_meta_info (
  organization_id bigint,
  param_id bigint,
  last_agg_time timestamp,
  updated_at timestamp,
  PRIMARY KEY (organization_id, param_id)
) WITH CLUSTERING ORDER BY (param_id ASC)
AND bloom_filter_fp_chance = 0.01
AND caching = {'keys': 'ALL', 'rows_per_partition': 'ALL'}
AND comment = ''
AND compaction = {'class': 'SizeTieredCompactionStrategy'}
AND compression = {'sstable_compression': 'org.apache.cassandra.io.compress.LZ4Compressor'}
AND crc_check_chance = 1.0
AND dclocal_read_repair_chance = 0.0
AND default_time_to_live = 0
AND gc_grace_seconds = 864000
AND max_index_interval = 2048
AND memtable_flush_period_in_ms = 0
AND min_index_interval = 128
AND read_repair_chance = 0.0
AND speculative_retry = '99.0PERCENTILE';

CREATE TABLE reecotech.device_connection_by_organization (
  organization_id bigint,
  connection_id bigint,
  access_token text,
  created_at timestamp,
  expired_time timestamp,
  name text,
  name_vi text,
  station_id bigint,
  transport_type text,
  updated_at timestamp,
  workspace_id bigint,
  PRIMARY KEY (organization_id, connection_id)
) WITH CLUSTERING ORDER BY (connection_id ASC)
AND bloom_filter_fp_chance = 0.01
AND caching = {'keys': 'ALL', 'rows_per_partition': 'ALL'}
AND comment = ''
AND compaction = {'class': 'SizeTieredCompactionStrategy'}
AND compression = {'sstable_compression': 'org.apache.cassandra.io.compress.LZ4Compressor'}
AND crc_check_chance = 1.0
AND dclocal_read_repair_chance = 0.0
AND default_time_to_live = 0
AND gc_grace_seconds = 864000
AND max_index_interval = 2048
AND memtable_flush_period_in_ms = 0
AND min_index_interval = 128
AND read_repair_chance = 0.0
AND speculative_retry = '99.0PERCENTILE';