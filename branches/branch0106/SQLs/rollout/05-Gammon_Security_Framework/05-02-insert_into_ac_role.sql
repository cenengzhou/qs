--------------------------------------------------------
--  File created - Nov-1-2016   
--------------------------------------------------------
SET SERVEROUTPUT ON
DECLARE
	AC_SCHEMA 				varchar(50):='ACDATAPROD';
	QA_SCHEMA 				varchar(50):='QSADMIN';
	SQL_QUERY_AC_ROLENAME 	varchar(512):='select (select count(rolename) as ACDATA_ROLE from ' || AC_SCHEMA || '.AC_ROLE where APPLICATIONCODE = ''QS'') FROM dual';
	SQL_QUERY_QA_ROLENAME 	varchar(512):='select (select count(name) as QSADMIN_ROLE from ' || QA_SCHEMA || '.QS_APP_AUTHORITY where name like ''ROLE_QS%'' or name like ''JOB%'')  FROM dual';
	SQL_INSERT_PREFIX 		varchar(512):='Insert into ' || AC_SCHEMA || '.AC_ROLE (APPLICATIONCODE,ROLENAME,JOB,ROLEDESCRIPTION,TYPE,TEMPLATE,LASTMODIFYDATE,LASTMODIFYUSER) values ';
    SOURCE_QSADMIN_ROLE 	NUMBER;
    BEFORE_ACDATA_ROLE 		NUMBER;
    AFTER_ACDATA_ROLE 		NUMBER;
BEGIN
    execute immediate SQL_QUERY_QA_ROLENAME INTO SOURCE_QSADMIN_ROLE;
    execute immediate SQL_QUERY_AC_ROLENAME INTO BEFORE_ACDATA_ROLE;
    --------------------------------------------------------------------------------------------------------------------------
    execute immediate SQL_INSERT_PREFIX || '(''QS'',''JOB_ALL'',''NA'',''All jobs available in Gammon'',''J'',''N'', sysdate,''GAMSKA\tikywong'')';
    execute immediate SQL_INSERT_PREFIX || '(''QS'',''JOB_CHN'',''NA'',''All China jobs'',''J'',''N'', sysdate,''GAMSKA\tikywong'')';
    execute immediate SQL_INSERT_PREFIX || '(''QS'',''JOB_CX'',''NA'',''All CX jobs of Gammon'',''J'',''N'', sysdate,''GAMSKA\tikywong'')';
    execute immediate SQL_INSERT_PREFIX || '(''QS'',''JOB_EM_CLP'',''NA'',''E&M CLP job only'',''J'',''N'', sysdate,''GAMSKA\tikywong'')';
    execute immediate SQL_INSERT_PREFIX || '(''QS'',''JOB_GLJV'',''NA'',''All Gammon Leighton Joint Venture jobs'',''J'',''N'', sysdate,''GAMSKA\tikywong'')';
    execute immediate SQL_INSERT_PREFIX || '(''QS'',''JOB_HKG'',''NA'',''All Hong Kong, China & Macau jobs of Gammon'',''J'',''N'', sysdate,''GAMSKA\tikywong'')';
    execute immediate SQL_INSERT_PREFIX || '(''QS'',''JOB_SCL1111'',''NA'',''SCL1111 jobs only'',''J'',''N'', sysdate,''GAMSKA\tikywong'')';
    execute immediate SQL_INSERT_PREFIX || '(''QS'',''JOB_SGP'',''NA'',''All Singapore jobs of Gammon'',''J'',''N'', sysdate,''GAMSKA\tikywong'')';
    execute immediate SQL_INSERT_PREFIX || '(''QS'',''JOB_TAMAR'',''NA'',''Tamar job only'',''J'',''N'', sysdate,''GAMSKA\tikywong'')';
    execute immediate SQL_INSERT_PREFIX || '(''QS'',''JOB_TAMAR_EM'',''NA'',''Tamar E&M job only'',''J'',''N'', sysdate,''GAMSKA\tikywong'')';
    execute immediate SQL_INSERT_PREFIX || '(''QS'',''ROLE_QS_ENQ'',''NA'',''Any user who has to access QS has to be granted with this role'',''F'',''N'', sysdate,''GAMSKA\tikywong'')';
    execute immediate SQL_INSERT_PREFIX || '(''QS'',''ROLE_QS_QS'',''NA'',''QS who can handle daily operations'',''F'',''N'', sysdate,''GAMSKA\tikywong'')';
    execute immediate SQL_INSERT_PREFIX || '(''QS'',''ROLE_QS_QS_ENQ'',''NA'',''QS Power User who can view administrative tasks'',''F'',''N'', sysdate,''GAMSKA\tikywong'')';
    execute immediate SQL_INSERT_PREFIX || '(''QS'',''ROLE_QS_QS_ADM'',''NA'',''QS Power User who can handle administrative tasks'',''F'',''N'', sysdate,''GAMSKA\tikywong'')';
    execute immediate SQL_INSERT_PREFIX || '(''QS'',''ROLE_QS_REVIEWER'',''NA'',''QS Reviewer who review daily operations that are done by QSs'',''F'',''N'', sysdate,''GAMSKA\tikywong'')';
    execute immediate SQL_INSERT_PREFIX || '(''QS'',''ROLE_QS_IMS_ENQ'',''NA'',''IMS who can view QS applications settings'',''F'',''N'', sysdate,''GAMSKA\tikywong'')';
    execute immediate SQL_INSERT_PREFIX || '(''QS'',''ROLE_QS_IMS_ADM'',''NA'',''IMS who can handle QS application settings'',''F'',''N'', sysdate,''GAMSKA\tikywong'')';
    --------------------------------------------------------------------------------------------------------------------------
    execute immediate SQL_QUERY_AC_ROLENAME INTO AFTER_ACDATA_ROLE;
    dbms_output.put_line('05-02-insert_into_ac_role.sql:');
    dbms_output.put_line('Create system role at ' || AC_SCHEMA || '.AC_ROLE');
    dbms_output.put_line('-------------------------------');
    dbms_output.put_line('Source:'||SOURCE_QSADMIN_ROLE);
    dbms_output.put_line('Before:'||BEFORE_ACDATA_ROLE);
    dbms_output.put_line('-------------------------------');
    dbms_output.put_line('After :'||AFTER_ACDATA_ROLE);
    dbms_output.put_line('-------------------------------');
    dbms_output.put_line('Expect:'||(BEFORE_ACDATA_ROLE + SOURCE_QSADMIN_ROLE));
    if(AFTER_ACDATA_ROLE = (BEFORE_ACDATA_ROLE + SOURCE_QSADMIN_ROLE)) THEN
    	dbms_output.put_line('Result:'|| 'Pass');
	ELSE
		dbms_output.put_line('Result:'|| 'Fail');
    END IF;
END;
/