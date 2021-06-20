CREATE TABLE temperature_record (
    id int(11) NOT NULL AUTO_INCREMENT,
  device_id int(11) DEFAULT NULL,
  recorded_timestamp datetime DEFAULT NULL,
  temp_fahrenheit double DEFAULT NULL,
  PRIMARY KEY (id),
  UNIQUE KEY UKoj3fjacrodd2c70cr0qaw97op (device_id,recorded_timestamp),
  KEY fn_index (recorded_timestamp)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
