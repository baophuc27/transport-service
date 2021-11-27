
CREATE KEYSPACE IF NOT EXISTS reecotech
WITH REPLICATION = {
'class' : 'SimpleStrategy',
'replication_factor' : 1
};

CREATE TABLE IF NOT EXISTS reecotech.indicators(
  indicator_id BIGINT,
  group_id BIGINT,
  indicator_name TEXT,
  indicator_name_vi TEXT,
  value_type TEXT,
  standard_unit TEXT,
  created_at TIMESTAMP,
  updated_at TIMESTAMP,
  PRIMARY KEY(indicator_id)
);

CREATE TABLE IF NOT EXISTS reecotech.alarm(
  organization_id BIGINT,
  param_id BIGINT,
  alarm_id BIGINT,
  alarm_type TEXT,
  min_value TEXT,
  max_value TEXT,
  num_of_match INT,
  frequency BIGINT,
  maintain_type TEXT,
  frequency_type TEXT,
  updated_at TIMESTAMP,
  PRIMARY KEY((organization_id), param_id, alarm_id)
)WITH CLUSTERING ORDER BY (param_id ASC, alarm_id ASC);

CREATE TABLE IF NOT EXISTS reecotech.params_by_organization(
  organization_id BIGINT,
  param_id BIGINT,
  indicator_id BIGINT,
  station_id BIGINT,
  param_name TEXT,
  unit TEXT,
  connection_id BIGINT,
  PRIMARY KEY((organization_id), param_id)
) WITH CLUSTERING ORDER BY (param_id ASC);


CREATE TABLE IF NOT EXISTS reecotech.numeric_series_by_organization(
  organization_id BIGINT,
  date DATE,
  param_id BIGINT,
  indicator_name TEXT,
  param_name TEXT,
  station_id BIGINT,
  connection_id BIGINT,
  value DOUBLE,
  received_at TIMESTAMP,
  is_alarm BOOLEAN,
  min_value DOUBLE,
  max_value DOUBLE,
  alarm_type TEXT,
  alarm_id BIGINT,
  event_time TIMESTAMP,
  lat DOUBLE,
  lon DOUBLE,
PRIMARY KEY((organization_id), param_id, event_time)
) WITH CLUSTERING ORDER BY (param_id ASC, event_time DESC);


CREATE TABLE IF NOT EXISTS reecotech.categorical_series_by_organization(
  organization_id BIGINT,
  date DATE,
  param_id BIGINT,
  station_id BIGINT,
  connection_id BIGINT,
  value TEXT,
  indicator_name TEXT,
  param_name TEXT,
  is_alarm BOOLEAN,
  alarm_type TEXT,
  alarm_id BIGINT,
  received_at TIMESTAMP,
  event_time TIMESTAMP,
  lat DOUBLE,
  lon DOUBLE,
  PRIMARY KEY((organization_id), param_id, event_time, value)
) WITH CLUSTERING ORDER BY (param_id ASC, event_time DESC, value ASC);

CREATE TABLE IF NOT EXISTS reecotech.numeric_stats_by_organization(
  organization_id BIGINT,
  param_id BIGINT,
  date DATE,
  min DOUBLE,
  max DOUBLE,
  median DOUBLE,
  mean DOUBLE,
  acc DOUBLE,
  count BIGINT,
  std DOUBLE,
  last_updated TIMESTAMP,
  PRIMARY KEY((organization_id), date, param_id)
) WITH CLUSTERING ORDER BY (date DESC, param_id ASC);

CREATE TABLE IF NOT EXISTS reecotech.categorical_stats_by_organization(
  organization_id BIGINT,
  date DATE,
  param_id BIGINT,
  value TEXT,
  count BIGINT,
  last_updated TIMESTAMP,
  PRIMARY KEY((organization_id), date, param_id, value)
) WITH CLUSTERING ORDER BY (date DESC, param_id ASC, value ASC);


