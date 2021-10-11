CREATE TABLE devices_by_station(
  station_id BIGINT,
  device_id BIGINT,
  decription TEXT,
  lat DOUBLE,
  lon DOUBLE,
  status BOOLEAN,
  PRIMARY KEY((station_id), device_id, status)
) WITH CLUSTERING ORDER BY(device_id DESC);

CREATE TABLE numeric_series_by_station(
  station_id BIGINT,
  device_id BIGINT,
  metric TEXT,
  value DOUBLE,
  event_ts TIMESTAMP,
  PRIMARY KEY((station_id, device_id), metric, event_ts)
) WITH CLUSTERING ORDER BY (metric DESC, event_ts DESC)

CREATE TABLE numeric_series_by_metric(
  station_id BIGINT,
  metric TEXT,
  value DOUBLE,
  event_ts TIMESTAMP,
  device_id BIGINT
  PRIMARY KEY((station_id, metric), event_ts, device_id)
) WITH CLUSTERING ORDER BY (metric DESC, event_ts DESC)

CREATE TABLE metrics_by_station(
  station_id BIGINT,
  device_id BIGINT,
  metric TEXT,
  value_type TEXT,
  unit TEXT,
  standard_unit TEXT,
  PRIMARY KEY((station_id), device_id, metric)
) WITH CLUSTERING ORDER BY (metric DESC)

CREATE TABLE numeric_statistics_by_device(
  device_id BIGINT,
  metric TEXT,
  date DATE,
  min DOUBLE,
  max DOUBLE,
  median DOUBLE
  mean DOUBLE,
  acc DOUBLE,
  last_updated TIMESTAMP,
  PRIMARY KEY((device_id, metric), date)
) WITH CLUSTERING ORDER BY (date DESC)

CREATE TABLE text_statistics_by_device(
  device_id BIGINT,
  metric TEXT,
  value TEXT,
  date DATE,
  value_count BIGINT,
  PRIMARY KEY((device_id, metric), date, value)
) WITH CLUSTERING ORDER BY (date ASC, value ASC)

CREATE TABLE text_series_by_device(
device_id BIGINT,
metric TEXT,
value TEXT,
date DATE,
value_count BIGINT,
PRIMARY KEY((device_id, metric), date, value)
) WITH CLUSTERING ORDER BY (date ASC, value ASC)


