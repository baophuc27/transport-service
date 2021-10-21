//CREATE TABLE organization(
//  organization_id BIGINT,
//  station_id BIGINT,
//  connection_id BIGINT,
//  param_id BIGINT,
//  lat DOUBLE,
//  lon DOUBLE,
//  status BOOLEAN,
//  PRIMARY KEY((organization_id), station_id, connection_id)
//) WITH CLUSTERING ORDER BY(station_id ASC, connection_id DESC);

CREATE TABLE IF NOT EXISTS indicators(
  indicator_id BIGINT,
  group_id BIGINT,
  group_name TEXT,
  indicator_name TEXT,
  value_type TEXT,
  unit TEXT,
  standard_unit TEXT,
  PRIMARY KEY(indicator_id)
);

CREATE TABLE IF NOT EXISTS params_by_organization(
  organization_id BIGINT,
  param_id BIGINT,
  indicator_id BIGINT,
  indicator_name TEXT,
  param_name TEXT,
  station_id BIGINT,
  connection_id BIGINT,
  PRIMARY KEY((organization_id), param_id)
) WITH CLUSTERING ORDER BY (param_id ASC);


CREATE TABLE IF NOT EXISTS numeric_series_by_organization(
  organization_id BIGINT,
  date DATE,
  param_id BIGINT,
  indicator_name TEXT,
  param_name TEXT,
  station_id BIGINT,
  connection_id BIGINT,
  value DOUBLE,
  received_at TIMESTAMP,
  event_time TIMESTAMP,
  lat DOUBLE,
  lon DOUBLE,
  PRIMARY KEY((organization_id, date), event_time, param_id)
) WITH CLUSTERING ORDER BY (event_time DESC, param_id ASC);

CREATE TABLE IF NOT EXISTS categorical_series_by_organization(
  organization_id BIGINT,
  date DATE,
  param_id BIGINT,
  station_id BIGINT,
  connection_id BIGINT,
  value TEXT,
  indicator_name TEXT,
  param_name TEXT,
  received_at TIMESTAMP,
  event_time TIMESTAMP,
  lat DOUBLE,
  lon DOUBLE,
  PRIMARY KEY((organization_id, date), event_time, param_id, value)
) WITH CLUSTERING ORDER BY (event_time DESC, param_id ASC, value ASC);

CREATE TABLE IF NOT EXISTS numeric_stats_by_organization(
  organization_id BIGINT,
  param_id BIGINT,
  date DATE,
  min DOUBLE,
  max DOUBLE,
  median DOUBLE,
  mean DOUBLE,
  acc DOUBLE,
  cnt BIGINT,
  std DOUBLE,
  last_updated TIMESTAMP,
  PRIMARY KEY((organization_id, date), param_id)
) WITH CLUSTERING ORDER BY (param_id ASC);

CREATE TABLE IF NOT EXISTS categorical_stats_by_organization(
  organization_id BIGINT,
  date DATE,
  param_id BIGINT,
  value TEXT,
  cnt BIGINT,
  last_updated TIMESTAMP,
  PRIMARY KEY((organization_id, date), param_id, value)
) WITH CLUSTERING ORDER BY (param_id ASC, value ASC);


