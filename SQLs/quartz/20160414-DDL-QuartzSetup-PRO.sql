--
-- A hint submitted by a user: Oracle DB MUST be created as "shared" and the 
-- job_queue_processes parameter  must be greater than 2
-- However, these settings are pretty much standard after any
-- Oracle install, so most users need not worry about this.
--
-- Many other users (including the primary author of Quartz) have had success
-- runing in dedicated mode, so only consider the above as a hint ;-)
--

-- delete all data of Quartz in QSADMIN
delete from QSADMIN.QRTZ3_TRIGGER_LISTENERS;
delete from QSADMIN.QRTZ3_TRIGGERS;
delete from QSADMIN.QRTZ3_SIMPLE_TRIGGERS;
delete from QSADMIN.QRTZ3_SCHEDULER_STATE;
delete from QSADMIN.QRTZ3_PAUSED_TRIGGER_GRPS;
delete from QSADMIN.QRTZ3_LOCKS;
delete from QSADMIN.QRTZ3_JOB_LISTENERS;
delete from QSADMIN.QRTZ3_JOB_DETAILS;
delete from QSADMIN.QRTZ3_FIRED_TRIGGERS;
delete from QSADMIN.QRTZ3_CRON_TRIGGERS;
delete from QSADMIN.QRTZ3_CALENDARS;
delete from QSADMIN.QRTZ3_BLOB_TRIGGERS;

-- drop all tables of Quartz from QSADMIN
drop table QSADMIN.QRTZ3_TRIGGER_LISTENERS;
drop table QSADMIN.QRTZ3_TRIGGERS;
drop table QSADMIN.QRTZ3_SIMPLE_TRIGGERS;
drop table QSADMIN.QRTZ3_SCHEDULER_STATE;
drop table QSADMIN.QRTZ3_PAUSED_TRIGGER_GRPS;
drop table QSADMIN.QRTZ3_LOCKS;
drop table QSADMIN.QRTZ3_JOB_LISTENERS;
drop table QSADMIN.QRTZ3_JOB_DETAILS;
drop table QSADMIN.QRTZ3_FIRED_TRIGGERS;
drop table QSADMIN.QRTZ3_CRON_TRIGGERS;
drop table QSADMIN.QRTZ3_CALENDARS;
drop table QSADMIN.QRTZ3_BLOB_TRIGGERS;

-- create new tables of Quartz in QSDATAPROD
CREATE TABLE QSDATAPROD.qrtz_job_details
  (
    SCHED_NAME VARCHAR2(120) NOT NULL,
    JOB_NAME  VARCHAR2(200) NOT NULL,
    JOB_GROUP VARCHAR2(200) NOT NULL,
    DESCRIPTION VARCHAR2(250) NULL,
    JOB_CLASS_NAME   VARCHAR2(250) NOT NULL, 
    IS_DURABLE VARCHAR2(1) NOT NULL,
    IS_NONCONCURRENT VARCHAR2(1) NOT NULL,
    IS_UPDATE_DATA VARCHAR2(1) NOT NULL,
    REQUESTS_RECOVERY VARCHAR2(1) NOT NULL,
    JOB_DATA BLOB NULL,
    CONSTRAINT QRTZ_JOB_DETAILS_PK PRIMARY KEY (SCHED_NAME,JOB_NAME,JOB_GROUP)
);
CREATE TABLE QSDATAPROD.qrtz_triggers
  (
    SCHED_NAME VARCHAR2(120) NOT NULL,
    TRIGGER_NAME VARCHAR2(200) NOT NULL,
    TRIGGER_GROUP VARCHAR2(200) NOT NULL,
    JOB_NAME  VARCHAR2(200) NOT NULL, 
    JOB_GROUP VARCHAR2(200) NOT NULL,
    DESCRIPTION VARCHAR2(250) NULL,
    NEXT_FIRE_TIME NUMBER(13) NULL,
    PREV_FIRE_TIME NUMBER(13) NULL,
    PRIORITY NUMBER(13) NULL,
    TRIGGER_STATE VARCHAR2(16) NOT NULL,
    TRIGGER_TYPE VARCHAR2(8) NOT NULL,
    START_TIME NUMBER(13) NOT NULL,
    END_TIME NUMBER(13) NULL,
    CALENDAR_NAME VARCHAR2(200) NULL,
    MISFIRE_INSTR NUMBER(2) NULL,
    JOB_DATA BLOB NULL,
    CONSTRAINT QRTZ_TRIGGERS_PK PRIMARY KEY (SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP),
    CONSTRAINT QRTZ_TRIGGER_TO_JOBS_FK FOREIGN KEY (SCHED_NAME,JOB_NAME,JOB_GROUP) 
      REFERENCES QSDATAPROD.QRTZ_JOB_DETAILS(SCHED_NAME,JOB_NAME,JOB_GROUP) 
);
CREATE TABLE QSDATAPROD.qrtz_simple_triggers
  (
    SCHED_NAME VARCHAR2(120) NOT NULL,
    TRIGGER_NAME VARCHAR2(200) NOT NULL,
    TRIGGER_GROUP VARCHAR2(200) NOT NULL,
    REPEAT_COUNT NUMBER(7) NOT NULL,
    REPEAT_INTERVAL NUMBER(12) NOT NULL,
    TIMES_TRIGGERED NUMBER(10) NOT NULL,
    CONSTRAINT QRTZ_SIMPLE_TRIG_PK PRIMARY KEY (SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP),
    CONSTRAINT QRTZ_SIMPLE_TRIG_TO_TRIG_FK FOREIGN KEY (SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP) 
	REFERENCES QSDATAPROD.QRTZ_TRIGGERS(SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP)
);
CREATE TABLE QSDATAPROD.qrtz_cron_triggers
  (
    SCHED_NAME VARCHAR2(120) NOT NULL,
    TRIGGER_NAME VARCHAR2(200) NOT NULL,
    TRIGGER_GROUP VARCHAR2(200) NOT NULL,
    CRON_EXPRESSION VARCHAR2(120) NOT NULL,
    TIME_ZONE_ID VARCHAR2(80),
    CONSTRAINT QRTZ_CRON_TRIG_PK PRIMARY KEY (SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP),
    CONSTRAINT QRTZ_CRON_TRIG_TO_TRIG_FK FOREIGN KEY (SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP) 
      REFERENCES QSDATAPROD.QRTZ_TRIGGERS(SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP)
);
CREATE TABLE QSDATAPROD.qrtz_simprop_triggers
  (          
    SCHED_NAME VARCHAR2(120) NOT NULL,
    TRIGGER_NAME VARCHAR2(200) NOT NULL,
    TRIGGER_GROUP VARCHAR2(200) NOT NULL,
    STR_PROP_1 VARCHAR2(512) NULL,
    STR_PROP_2 VARCHAR2(512) NULL,
    STR_PROP_3 VARCHAR2(512) NULL,
    INT_PROP_1 NUMBER(10) NULL,
    INT_PROP_2 NUMBER(10) NULL,
    LONG_PROP_1 NUMBER(13) NULL,
    LONG_PROP_2 NUMBER(13) NULL,
    DEC_PROP_1 NUMERIC(13,4) NULL,
    DEC_PROP_2 NUMERIC(13,4) NULL,
    BOOL_PROP_1 VARCHAR2(1) NULL,
    BOOL_PROP_2 VARCHAR2(1) NULL,
    CONSTRAINT QRTZ_SIMPROP_TRIG_PK PRIMARY KEY (SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP),
    CONSTRAINT QRTZ_SIMPROP_TRIG_TO_TRIG_FK FOREIGN KEY (SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP) 
      REFERENCES QSDATAPROD.QRTZ_TRIGGERS(SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP)
);
CREATE TABLE QSDATAPROD.qrtz_blob_triggers
  (
    SCHED_NAME VARCHAR2(120) NOT NULL,
    TRIGGER_NAME VARCHAR2(200) NOT NULL,
    TRIGGER_GROUP VARCHAR2(200) NOT NULL,
    BLOB_DATA BLOB NULL,
    CONSTRAINT QRTZ_BLOB_TRIG_PK PRIMARY KEY (SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP),
    CONSTRAINT QRTZ_BLOB_TRIG_TO_TRIG_FK FOREIGN KEY (SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP) 
        REFERENCES QSDATAPROD.QRTZ_TRIGGERS(SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP)
);
CREATE TABLE QSDATAPROD.qrtz_calendars
  (
    SCHED_NAME VARCHAR2(120) NOT NULL,
    CALENDAR_NAME  VARCHAR2(200) NOT NULL, 
    CALENDAR BLOB NOT NULL,
    CONSTRAINT QRTZ_CALENDARS_PK PRIMARY KEY (SCHED_NAME,CALENDAR_NAME)
);
CREATE TABLE QSDATAPROD.qrtz_paused_trigger_grps
  (
    SCHED_NAME VARCHAR2(120) NOT NULL,
    TRIGGER_GROUP  VARCHAR2(200) NOT NULL, 
    CONSTRAINT QRTZ_PAUSED_TRIG_GRPS_PK PRIMARY KEY (SCHED_NAME,TRIGGER_GROUP)
);
CREATE TABLE QSDATAPROD.qrtz_fired_triggers 
  (
    SCHED_NAME VARCHAR2(120) NOT NULL,
    ENTRY_ID VARCHAR2(95) NOT NULL,
    TRIGGER_NAME VARCHAR2(200) NOT NULL,
    TRIGGER_GROUP VARCHAR2(200) NOT NULL,
    INSTANCE_NAME VARCHAR2(200) NOT NULL,
    FIRED_TIME NUMBER(13) NOT NULL,
    SCHED_TIME NUMBER(13) NOT NULL,
    PRIORITY NUMBER(13) NOT NULL,
    STATE VARCHAR2(16) NOT NULL,
    JOB_NAME VARCHAR2(200) NULL,
    JOB_GROUP VARCHAR2(200) NULL,
    IS_NONCONCURRENT VARCHAR2(1) NULL,
    REQUESTS_RECOVERY VARCHAR2(1) NULL,
    CONSTRAINT QRTZ_FIRED_TRIGGER_PK PRIMARY KEY (SCHED_NAME,ENTRY_ID)
);
CREATE TABLE QSDATAPROD.qrtz_scheduler_state 
  (
    SCHED_NAME VARCHAR2(120) NOT NULL,
    INSTANCE_NAME VARCHAR2(200) NOT NULL,
    LAST_CHECKIN_TIME NUMBER(13) NOT NULL,
    CHECKIN_INTERVAL NUMBER(13) NOT NULL,
    CONSTRAINT QRTZ_SCHEDULER_STATE_PK PRIMARY KEY (SCHED_NAME,INSTANCE_NAME)
);
CREATE TABLE QSDATAPROD.qrtz_locks
  (
    SCHED_NAME VARCHAR2(120) NOT NULL,
    LOCK_NAME  VARCHAR2(40) NOT NULL, 
    CONSTRAINT QRTZ_LOCKS_PK PRIMARY KEY (SCHED_NAME,LOCK_NAME)
);

create index idx_qrtz_j_req_recovery on QSDATAPROD.qrtz_job_details(SCHED_NAME,REQUESTS_RECOVERY);
create index idx_qrtz_j_grp on QSDATAPROD.qrtz_job_details(SCHED_NAME,JOB_GROUP);

create index idx_qrtz_t_j on QSDATAPROD.qrtz_triggers(SCHED_NAME,JOB_NAME,JOB_GROUP);
create index idx_qrtz_t_jg on QSDATAPROD.qrtz_triggers(SCHED_NAME,JOB_GROUP);
create index idx_qrtz_t_c on QSDATAPROD.qrtz_triggers(SCHED_NAME,CALENDAR_NAME);
create index idx_qrtz_t_g on QSDATAPROD.qrtz_triggers(SCHED_NAME,TRIGGER_GROUP);
create index idx_qrtz_t_state on QSDATAPROD.qrtz_triggers(SCHED_NAME,TRIGGER_STATE);
create index idx_qrtz_t_n_state on QSDATAPROD.qrtz_triggers(SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP,TRIGGER_STATE);
create index idx_qrtz_t_n_g_state on QSDATAPROD.qrtz_triggers(SCHED_NAME,TRIGGER_GROUP,TRIGGER_STATE);
create index idx_qrtz_t_next_fire_time on QSDATAPROD.qrtz_triggers(SCHED_NAME,NEXT_FIRE_TIME);
create index idx_qrtz_t_nft_st on QSDATAPROD.qrtz_triggers(SCHED_NAME,TRIGGER_STATE,NEXT_FIRE_TIME);
create index idx_qrtz_t_nft_misfire on QSDATAPROD.qrtz_triggers(SCHED_NAME,MISFIRE_INSTR,NEXT_FIRE_TIME);
create index idx_qrtz_t_nft_st_misfire on QSDATAPROD.qrtz_triggers(SCHED_NAME,MISFIRE_INSTR,NEXT_FIRE_TIME,TRIGGER_STATE);
create index idx_qrtz_t_nft_st_misfire_grp on QSDATAPROD.qrtz_triggers(SCHED_NAME,MISFIRE_INSTR,NEXT_FIRE_TIME,TRIGGER_GROUP,TRIGGER_STATE);

create index idx_qrtz_ft_trig_inst_name on QSDATAPROD.qrtz_fired_triggers(SCHED_NAME,INSTANCE_NAME);
create index idx_qrtz_ft_inst_job_req_rcvry on QSDATAPROD.qrtz_fired_triggers(SCHED_NAME,INSTANCE_NAME,REQUESTS_RECOVERY);
create index idx_qrtz_ft_j_g on QSDATAPROD.qrtz_fired_triggers(SCHED_NAME,JOB_NAME,JOB_GROUP);
create index idx_qrtz_ft_jg on QSDATAPROD.qrtz_fired_triggers(SCHED_NAME,JOB_GROUP);
create index idx_qrtz_ft_t_g on QSDATAPROD.qrtz_fired_triggers(SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP);
create index idx_qrtz_ft_tg on QSDATAPROD.qrtz_fired_triggers(SCHED_NAME,TRIGGER_GROUP);

--------------------------------------------------------
--  Grant privileges on tables & sequences
--------------------------------------------------------
/* GRANT PRIVILEGES ON TABLES */
BEGIN
   FOR r IN (SELECT owner, table_name FROM all_tables WHERE owner='QSDATAPROD') LOOP
      EXECUTE IMMEDIATE 'GRANT INSERT, SELECT, UPDATE, DELETE on '||r.owner||'.'||r.table_name||' to QSUSER3';
   END LOOP;
END;

BEGIN
   FOR r IN (SELECT owner, table_name FROM all_tables WHERE owner='QSDATAPROD') LOOP
      EXECUTE IMMEDIATE 'GRANT INSERT, SELECT, UPDATE, DELETE on '||r.owner||'.'||r.table_name||' to QSTEST';
   END LOOP;
END;

/* GRANT PRIVILEGES ON SEQUENCE */
BEGIN   
   FOR s IN (SELECT sequence_owner,sequence_name FROM DBA_SEQUENCES where sequence_owner='QSDATAPROD') LOOP
      EXECUTE IMMEDIATE 'GRANT select on ' || s.sequence_owner || '.' || s.SEQUENCE_NAME || ' to QSUSER3';
   END LOOP;
END;

BEGIN   
   FOR s IN (SELECT sequence_owner,sequence_name FROM DBA_SEQUENCES where sequence_owner='QSDATAPROD') LOOP
      EXECUTE IMMEDIATE 'GRANT select on ' || s.sequence_owner || '.' || s.SEQUENCE_NAME || ' to QSTEST';
   END LOOP;
END