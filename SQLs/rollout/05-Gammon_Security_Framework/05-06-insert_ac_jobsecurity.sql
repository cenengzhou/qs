--------------------------------------------------------
--  File created - Nov-1-2016   
--------------------------------------------------------
SET SERVEROUTPUT ON
DECLARE
    AC_SCHEMA               varchar(50):='ACDATAUAT';
    SQL_INSERT_PREFIX       varchar(512):='Insert into ' || AC_SCHEMA || '.AC_JOBSECURITY (JOB_USER,APPLICATIONCODE,COMPANY,DIVISION,DEPARTMENT,JOBNO,STAFFID,DESCRIPTION,STATUS,TYPE,ACCESSRIGHT,LASTMODIFYDATE,LASTMODIFYUSER,SUBDIVISION) values ';
    SQL_QUERY_JOBSECURITY   varchar(512):='select count(*) from ' || AC_SCHEMA || '.AC_JOBSECURITY';
    SOURCE_JOBSECURITY      NUMBER:=23;
    AC_BEFORE_JOBSECURITY   NUMBER;
    AC_AFTER_JOBSECURITY    NUMBER;
BEGIN
    execute immediate SQL_QUERY_JOBSECURITY INTO AC_BEFORE_JOBSECURITY;
    --------------------------------------------------------------------------------------------------------------------------
    execute immediate SQL_INSERT_PREFIX || '(''JOB_ALL'',''ALL'',''NA'',''NA'',''NA'',''NA'',''NA'',''All jobs. '',''A'',''G'',''NA'',sysdate,''SYSTEM'',''NA'')';
    execute immediate SQL_INSERT_PREFIX || '(''JOB_CX'',''ALL'',''01017'',''NA'',''NA'',''NA'',''NA'',''Job security for CX JV (Co 1017)'',''A'',''G'',''NA'',sysdate,''SYSTEM'',''NA'')';
    execute immediate SQL_INSERT_PREFIX || '(''JOB_EM_CLP'',''ALL'',''01019'',''NA'',''NA'',''NA'',''NA'',''Co 1019 Gammon E&M Ltd-CLP'',''A'',''G'',''NA'',sysdate,''SYSTEM'',''NA'')';
    execute immediate SQL_INSERT_PREFIX || '(''JOB_GLJV'',''ALL'',''01018'',''NA'',''NA'',''NA'',''NA'',''Gammon-Leighton JV'',''A'',''G'',''NA'',sysdate,''SYSTEM'',''NA'')';
    execute immediate SQL_INSERT_PREFIX || '(''JOB_HKG'',''ALL'',''00031'',''NA'',''NA'',''NA'',''NA'',''Job security for HK solo job (Co 31)'',''A'',''G'',''NA'',sysdate,''SYSTEM'',''NA'')';
    execute immediate SQL_INSERT_PREFIX || '(''JOB_HKG'',''ALL'',''00001'',''NA'',''NA'',''NA'',''NA'',''HKG jobs for company 00001'',''A'',''G'',''NA'',sysdate,''SYSTEM'',''NA'')';
    execute immediate SQL_INSERT_PREFIX || '(''JOB_HKG'',''ALL'',''00011'',''NA'',''NA'',''NA'',''NA'',''Job security for HK solo job (Co 11)'',''A'',''G'',''NA'',sysdate,''SYSTEM'',''NA'')';
    execute immediate SQL_INSERT_PREFIX || '(''JOB_HKG'',''ALL'',''00021'',''NA'',''NA'',''NA'',''NA'',''Job security for HK solo job (Co 21)'',''A'',''G'',''NA'',sysdate,''SYSTEM'',''NA'')';
    execute immediate SQL_INSERT_PREFIX || '(''JOB_HKG'',''ALL'',''00007'',''NA'',''NA'',''NA'',''NA'',''Job security for HK solo job (Co 7)'',''A'',''G'',''NA'',sysdate,''SYSTEM'',''NA'')';
    execute immediate SQL_INSERT_PREFIX || '(''JOB_HKG'',''ALL'',''00002'',''NA'',''NA'',''NA'',''NA'',''Job security for HK solo job (Co 2)'',''A'',''G'',''NA'',sysdate,''SYSTEM'',''NA'')';
    execute immediate SQL_INSERT_PREFIX || '(''JOB_HKG'',''ALL'',''01022'',''NA'',''NA'',''NA'',''NA'',''Job security for HK solo job (Co 1022)'',''A'',''G'',''NA'',sysdate,''SYSTEM'',''NA'')';
    execute immediate SQL_INSERT_PREFIX || '(''JOB_HKG'',''ALL'',''01019'',''NA'',''NA'',''NA'',''NA'',''Job security for HK solo job (Co 1019)'',''A'',''G'',''NA'',sysdate,''SYSTEM'',''NA'')';
    execute immediate SQL_INSERT_PREFIX || '(''JOB_HKG'',''ALL'',''01016'',''NA'',''NA'',''NA'',''NA'',''Job security for HK solo job (Co 1016)'',''A'',''G'',''NA'',sysdate,''SYSTEM'',''NA'')';
    execute immediate SQL_INSERT_PREFIX || '(''JOB_HKG'',''ALL'',''01015'',''NA'',''NA'',''NA'',''NA'',''Job security for HK solo job (Co 1015)'',''A'',''G'',''NA'',sysdate,''SYSTEM'',''NA'')';
    execute immediate SQL_INSERT_PREFIX || '(''JOB_HKG'',''ALL'',''01003'',''NA'',''NA'',''NA'',''NA'',''Job security for HK solo job (Co 1003)'',''A'',''G'',''NA'',sysdate,''SYSTEM'',''NA'')';
    execute immediate SQL_INSERT_PREFIX || '(''JOB_HKG'',''ALL'',''01001'',''NA'',''NA'',''NA'',''NA'',''Job security for HK solo job (Co 1001)'',''A'',''G'',''NA'',sysdate,''SYSTEM'',''NA'')';
    execute immediate SQL_INSERT_PREFIX || '(''JOB_HKG'',''ALL'',''00067'',''NA'',''NA'',''NA'',''NA'',''Job security for HK solo job (Co 67)'',''A'',''G'',''NA'',sysdate,''SYSTEM'',''NA'')';
    execute immediate SQL_INSERT_PREFIX || '(''JOB_HKG'',''ALL'',''00062'',''NA'',''NA'',''NA'',''NA'',''Job security for HK solo job (Co 62)'',''A'',''G'',''NA'',sysdate,''SYSTEM'',''NA'')';
    execute immediate SQL_INSERT_PREFIX || '(''JOB_HKG'',''ALL'',''00061'',''NA'',''NA'',''NA'',''NA'',''Job security for HK solo job (Co 61)'',''A'',''G'',''NA'',sysdate,''SYSTEM'',''NA'')';
    execute immediate SQL_INSERT_PREFIX || '(''JOB_SCL1111'',''ALL'',''01021'',''NA'',''NA'',''NA'',''NA'',''Co 1021 Gammon-Kaden SCL 1111 JV'',''A'',''G'',''NA'',sysdate,''SYSTEM'',''NA'')';
    execute immediate SQL_INSERT_PREFIX || '(''JOB_SGP'',''ALL'',''06011'',''NA'',''NA'',''NA'',''NA'',''Co 6011 Gammon Construction Ltd (Sg-Br)'',''A'',''G'',''NA'',sysdate,''SYSTEM'',''NA'')';
    execute immediate SQL_INSERT_PREFIX || '(''JOB_SGP'',''ALL'',''06058'',''NA'',''NA'',''NA'',''NA'',''Co 6058 Gammon Pte. Limited'',''A'',''G'',''NA'',sysdate,''SYSTEM'',''NA'')';
    execute immediate SQL_INSERT_PREFIX || '(''JOB_SGP'',''ALL'',''06089'',''NA'',''NA'',''NA'',''NA'',''Co 6089 BBGJV'',''A'',''G'',''NA'',sysdate,''SYSTEM'',''NA'')';
    --------------------------------------------------------------------------------------------------------------------------
    execute immediate SQL_QUERY_JOBSECURITY INTO AC_AFTER_JOBSECURITY;
    dbms_output.put_line('05-06-insert_ac_jobsecurity.sql:');
    dbms_output.put_line('Create job security at ' || AC_SCHEMA || '.AC_JOBSECURITY');
    dbms_output.put_line('-------------------------------');
    dbms_output.put_line('Source:'||SOURCE_JOBSECURITY);
    dbms_output.put_line('Before:'||AC_BEFORE_JOBSECURITY);
    dbms_output.put_line('-------------------------------');
    dbms_output.put_line('After :'||AC_AFTER_JOBSECURITY);
    dbms_output.put_line('-------------------------------');
    dbms_output.put_line('Expect:'||(AC_BEFORE_JOBSECURITY + SOURCE_JOBSECURITY));
    if(AC_AFTER_JOBSECURITY = (AC_BEFORE_JOBSECURITY + SOURCE_JOBSECURITY)) THEN
    	dbms_output.put_line('Result:'|| 'Pass');
	ELSE
		dbms_output.put_line('Result:'|| 'Fail');
    END IF;
END;
