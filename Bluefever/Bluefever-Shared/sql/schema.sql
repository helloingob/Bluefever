CREATE TABLE IF NOT EXISTS thermostats
  (
     pk           BIGINT PRIMARY KEY auto_increment NOT NULL,
     name         VARCHAR(50) NOT NULL,
     address      VARCHAR(50) NOT NULL UNIQUE,
     pin          INT(9) NOT NULL,
     firmware     VARCHAR(50),
     software     VARCHAR(50),
     manufacturer VARCHAR(50),
     devicename   VARCHAR(50)
  );  

CREATE TABLE IF NOT EXISTS thermostat_assets
  (
     pk                 BIGINT PRIMARY KEY auto_increment NOT NULL,
     thermostat_pk      BIGINT NOT NULL,
     date               TIMESTAMP NOT NULL,
     currenttemperature DOUBLE,
     manualtemperature  DOUBLE,
     hightemperature    DOUBLE,
     lowtemperature     DOUBLE,
     offsettemperature  DOUBLE
  );  
  
CREATE TABLE IF NOT EXISTS thermostat_jobs
  (
     pk                 BIGINT PRIMARY KEY auto_increment NOT NULL,
     active 			TINYINT(1) DEFAULT '1',
     thermostat_pk      BIGINT NOT NULL,     
     temperature 		DOUBLE NOT NULL,
     day_of_week        INT(1),
     time				TIME,
     date               TIMESTAMP
  );       
  
  ALTER TABLE thermostat_jobs ADD CONSTRAINT thermostat_jobs_thermostats_FK FOREIGN KEY (thermostat_pk) REFERENCES thermostats(pk) ON DELETE CASCADE;
  ALTER TABLE thermostat_assets ADD CONSTRAINT thermostat_assets_thermostats_FK FOREIGN KEY (thermostat_pk) REFERENCES thermostats(pk) ON DELETE CASCADE;
  
  