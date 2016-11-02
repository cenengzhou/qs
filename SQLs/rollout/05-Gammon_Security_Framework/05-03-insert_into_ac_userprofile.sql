--------------------------------------------------------
--  File created - Nov-1-2016   
--------------------------------------------------------
SET SERVEROUTPUT ON
DECLARE
  AC_SCHEMA           varchar(50):='ACDATAUAT';
  QA_SCHEMA           varchar(50):='QSADMIN';
  SQL_INSERT_PREFIX   varchar(512):=
      'insert into ' || AC_SCHEMA || '.AC_USERPROFILE(
      CREATIONDATE,
      EMAILADDRESS,
      GAMMONUSER,
      LASTMODIFYDATE,
      LASTMODIFYUSER,
      STAFFID,
      STATUS,
      TEMPLATE,
      USERADACCOUNT,
      USERNAME)';
  SQL_QUERY_QAUSER    varchar(1024):=
      '(select
      CREATED_DATE as CREATIONDATE,
      EMAIL as EMAILADDRESS,
      ''Y'' as GAMMONUSER,
      LAST_MODIFIED_DATE as LASTMODIFYDATE,
      case when LAST_MODIFIED_USER = ''SYSTEM'' then LAST_MODIFIED_USER
      else ''gamska\'' || LAST_MODIFIED_USER end as LASTMODIFYUSER,
      case
        when ADDRESS_BOOK_NUMBER is not null then ADDRESS_BOOK_NUMBER
        else 0 END as STAFFID,
      ''A'' as STATUS,
      ''N'' as TEMPLATE,
      ''gamska\'' || USERNAME as USERADACCOUNT,
      case 
        when FULLNAME is not null then FULLNAME 
        else USERNAME END as USERNAME
      from ' || QA_SCHEMA || '.QS_APP_USER
      where
      SYSTEM_STATUS = ''ACTIVE''
      and ''gamska\''||username not in (select useradaccount from ' || AC_SCHEMA || '.AC_USERPROFILE)
      )';
  SQL_QUERY_ACUSER        varchar(512):='select count(*) as BEFORE_INSERT_USER_AC from ' || AC_SCHEMA || '.AC_USERPROFILE';    
  QA_USER_TO_INSERT       NUMBER;    
  AC_BEFORE_INSERT_USER   NUMBER;
  AC_AFTER_INSERT_USER    NUMBER;
BEGIN
    execute immediate 'select count(*) from ' || SQL_QUERY_QAUSER INTO QA_USER_TO_INSERT;
    execute immediate SQL_QUERY_ACUSER INTO AC_BEFORE_INSERT_USER;
    --------------------------------------------------------------------------------------------------------------------------
    execute immediate SQL_INSERT_PREFIX || SQL_QUERY_QAUSER;
    --------------------------------------------------------------------------------------------------------------------------
    execute immediate SQL_QUERY_ACUSER INTO AC_AFTER_INSERT_USER;
    dbms_output.put_line('05-03-insert_into_ac_userprofile.sql:');
    dbms_output.put_line('Copy user profile from ' || QA_SCHEMA || '.QS_APP_USER to ' || AC_SCHEMA || '.AC_USERPROFILE');
    dbms_output.put_line('-------------------------------');
    dbms_output.put_line('Source:'||QA_USER_TO_INSERT);
    dbms_output.put_line('Before:'||AC_BEFORE_INSERT_USER);
    dbms_output.put_line('-------------------------------');
    dbms_output.put_line('After :'||AC_AFTER_INSERT_USER);
    dbms_output.put_line('-------------------------------');
    dbms_output.put_line('Expect:'||(AC_BEFORE_INSERT_USER + QA_USER_TO_INSERT));
    if(AC_AFTER_INSERT_USER = (AC_BEFORE_INSERT_USER + QA_USER_TO_INSERT)) THEN
    	dbms_output.put_line('Result:'|| 'Pass');
	ELSE
		dbms_output.put_line('Result:'|| 'Fail');
    END IF;
END;
