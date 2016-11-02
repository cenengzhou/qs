--------------------------------------------------------
--  File created - Nov-1-2016   
--------------------------------------------------------
SET SERVEROUTPUT ON
DECLARE
    AC_SCHEMA 				        varchar(50):='ACDATAUAT';
    QA_SCHEMA                 varchar(50):='QSADMIN';
    SQL_DELETE_ACUSERSECURITY varchar(512):='DELETE from ' || AC_SCHEMA || '.AC_USERSECURITY where APPLICATIONCODE=''QS''';
    SQL_DELETE_ADM            varchar(512):='DELETE from ' || AC_SCHEMA || '.AC_USERSECURITY where ROLENAME LIKE ''ROLE%'' and APPLICATIONCODE=''QS'' and USERADACCOUNT in (''gamska\tikywong'', ''gamska\koeyyeung'', ''gamska\paulnpyiu'', ''gamska\vincentmok'')';
    SQL_QUERY_ACUSERSECURITY  varchar(512):='select count(*) from ' || AC_SCHEMA || '.AC_USERSECURITY';
    SQL_INSERT_PREFIX         varchar(512):='Insert into ' || AC_SCHEMA || '.AC_USERSECURITY (USERADACCOUNT,APPLICATIONCODE,LASTMODIFYDATE,LASTMODIFYUSER,ROLENAME) ';
    SQL_QUERY_QAUSER          varchar(4000):='
    select 
    QS.USERADACCOUNT,
    MAX(QS.APPLICATIONCODE) as APPLICATIONCODE, 
    MAX(QS.LASTMODIFYDATE) as LASTMODIFYDATE, 
    MAX(QS.LASTMODIFYUSER) LASTMODIFYUSER, 
    NEW_ROLE.QS2_ROLE as ROLENAME
    from 
    (select
      ''gamska\''|| u.username as USERADACCOUNT, 
      ''QS'' as APPLICATIONCODE,
      ua.name,
      ua.LAST_MODIFIED_DATE as LASTMODIFYDATE,
      case 
        when ua.LAST_MODIFIED_USER = ''SYSTEM'' then ua.LAST_MODIFIED_USER
        else ''gamska\'' || ua.LAST_MODIFIED_USER end as LASTMODIFYUSER
    from 
    ' || QA_SCHEMA || '.QS_APP_USER u left join ' || QA_SCHEMA || '.QS_LINK_USER_AUTHORITY lua on u.id = lua.user_id 
    left join ' || QA_SCHEMA || '.QS_APP_AUTHORITY ua on lua.AUTHORITY_ID = ua.id
    where 
    u.SYSTEM_STATUS = ''ACTIVE'' and
    ua.name like ''JOB%'' or
    ua.name in(''ROLE_QS_ENQUIRY'', ''ROLE_QS_QS'', ''ROLE_QS_APPROVER'', ''ROLE_QS_IMS_ADM'', ''ROLE_QS_ADMIN'')
    ) QS
    left join (
      SELECT ''ROLE_QS_ENQUIRY'' as QS_ROLE , ''ROLE_QS_ENQ'' as QS2_ROLE FROM DUAL UNION
      SELECT ''ROLE_QS_QS'' as QS_ROLE , ''ROLE_QS_ENQ'' as QS2_ROLE FROM DUAL UNION
      SELECT ''ROLE_QS_QS'' as QS_ROLE , ''ROLE_QS_QS'' as QS2_ROLE FROM DUAL UNION
      SELECT ''ROLE_QS_APPROVER'' as QS_ROLE , ''ROLE_QS_ENQ'' as QS2_ROLE FROM DUAL UNION
      SELECT ''ROLE_QS_APPROVER'' as QS_ROLE , ''ROLE_QS_REVIEWER'' as QS2_ROLE FROM DUAL UNION
      SELECT ''ROLE_QS_ADMIN'' as QS_ROLE , ''ROLE_QS_ENQ'' as QS2_ROLE FROM DUAL UNION
      SELECT ''ROLE_QS_ADMIN'' as QS_ROLE , ''ROLE_QS_QS_ENQ'' as QS2_ROLE FROM DUAL UNION
      SELECT ''ROLE_QS_ADMIN'' as QS_ROLE , ''ROLE_QS_IMS_ENQ'' as QS2_ROLE FROM DUAL UNION
      SELECT ''ROLE_QS_ADMIN'' as QS_ROLE , ''ROLE_QS_QS_ADM'' as QS2_ROLE FROM DUAL UNION
      SELECT ''ROLE_QS_IMS_ADM'' as QS_ROLE , ''ROLE_QS_ENQ'' as QS2_ROLE FROM DUAL UNION
      SELECT ''ROLE_QS_IMS_ADM'' as QS_ROLE , ''ROLE_QS_QS_ENQ'' as QS2_ROLE FROM DUAL UNION
      SELECT ''ROLE_QS_IMS_ADM'' as QS_ROLE , ''ROLE_QS_IMS_ENQ'' as QS2_ROLE FROM DUAL UNION
      SELECT ''ROLE_QS_IMS_ADM'' as QS_ROLE , ''ROLE_QS_IMS_ADM'' as QS2_ROLE FROM DUAL UNION
      SELECT ''JOB_ALL'' as QS_ROLE , ''JOB_ALL'' as QS2_ROLE FROM DUAL UNION
      SELECT ''JOB_SGP'' as QS_ROLE , ''JOB_SGP'' as QS2_ROLE FROM DUAL UNION
      SELECT ''JOB_TAMAR'' as QS_ROLE , ''JOB_TAMAR'' as QS2_ROLE FROM DUAL UNION
      SELECT ''JOB_TAMAR_EM'' as QS_ROLE , ''JOB_TAMAR_EM'' as QS2_ROLE FROM DUAL UNION
      SELECT ''JOB_EM_CLP'' as QS_ROLE , ''JOB_EM_CLP'' as QS2_ROLE FROM DUAL UNION
      SELECT ''JOB_HKG'' as QS_ROLE , ''JOB_HKG'' as QS2_ROLE FROM DUAL UNION
      SELECT ''JOB_CX'' as QS_ROLE , ''JOB_CX'' as QS2_ROLE FROM DUAL UNION
      SELECT ''JOB_GLJV'' as QS_ROLE , ''JOB_GLJV'' as QS2_ROLE FROM DUAL UNION
      SELECT ''JOB_SCL1111'' as QS_ROLE , ''JOB_SCL1111'' as QS2_ROLE FROM DUAL UNION
      SELECT ''JOB_CHN'' as QS_ROLE, ''JOB_CHN'' as QS2_ROLE FROM DUAL
    ) NEW_ROLE on QS.NAME = NEW_ROLE.QS_ROLE
    group by QS.USERADACCOUNT, NEW_ROLE.QS2_ROLE
    ';
    SOURCE_QAUSER           NUMBER;
    BEFORE_ACUSERSECURITY   NUMBER;
    AFTER_ACUSERSECURITY    NUMBER;
    INSERTED_COUNT          NUMBER:=0;
    DELETED_COUNT           NUMBER:=0;
    DELETED_ADM_COUNT       NUMBER:=0;
BEGIN
    execute immediate SQL_QUERY_ACUSERSECURITY INTO BEFORE_ACUSERSECURITY;
    execute immediate 'select count(*) from (' || SQL_QUERY_QAUSER || ')' INTO SOURCE_QAUSER;
    execute immediate SQL_DELETE_ACUSERSECURITY;
    DELETED_COUNT:=SQL%ROWCOUNT;
    BEFORE_ACUSERSECURITY:=BEFORE_ACUSERSECURITY-DELETED_COUNT;
    --------------------------------------------------------------------------------------------------------------------------
    execute immediate SQL_INSERT_PREFIX || SQL_QUERY_QAUSER;
    INSERTED_COUNT:=SQL%ROWCOUNT;
    
    execute immediate SQL_DELETE_ADM;
    DELETED_ADM_COUNT:=SQL%ROWCOUNT;
    SOURCE_QAUSER:=SOURCE_QAUSER-DELETED_ADM_COUNT;
    INSERTED_COUNT:=INSERTED_COUNT-DELETED_ADM_COUNT;
    execute immediate SQL_INSERT_PREFIX || ' values (''gamska\tikywong'',''QS'',sysdate,''SYSTEM'',''ROLE_QS_ENQ'')';
    execute immediate SQL_INSERT_PREFIX || ' values (''gamska\koeyyeung'',''QS'',sysdate,''SYSTEM'',''ROLE_QS_ENQ'')';
    execute immediate SQL_INSERT_PREFIX || ' values (''gamska\paulnpyiu'',''QS'',sysdate,''SYSTEM'',''ROLE_QS_ENQ'')';
    execute immediate SQL_INSERT_PREFIX || ' values (''gamska\vincentmok'',''QS'',sysdate,''SYSTEM'',''ROLE_QS_ENQ'')';
  
    execute immediate SQL_INSERT_PREFIX || ' values (''gamska\tikywong'',''QS'',sysdate,''SYSTEM'',''ROLE_QS_QS_ENQ'')';
    execute immediate SQL_INSERT_PREFIX || ' values (''gamska\koeyyeung'',''QS'',sysdate,''SYSTEM'',''ROLE_QS_QS_ENQ'')';
    execute immediate SQL_INSERT_PREFIX || ' values (''gamska\paulnpyiu'',''QS'',sysdate,''SYSTEM'',''ROLE_QS_QS_ENQ'')';
    execute immediate SQL_INSERT_PREFIX || ' values (''gamska\vincentmok'',''QS'',sysdate,''SYSTEM'',''ROLE_QS_QS_ENQ'')';
  
    execute immediate SQL_INSERT_PREFIX || ' values (''gamska\tikywong'',''QS'',sysdate,''SYSTEM'',''ROLE_QS_IMS_ENQ'')';
    execute immediate SQL_INSERT_PREFIX || ' values (''gamska\koeyyeung'',''QS'',sysdate,''SYSTEM'',''ROLE_QS_IMS_ENQ'')';
    execute immediate SQL_INSERT_PREFIX || ' values (''gamska\paulnpyiu'',''QS'',sysdate,''SYSTEM'',''ROLE_QS_IMS_ENQ'')';
    execute immediate SQL_INSERT_PREFIX || ' values (''gamska\vincentmok'',''QS'',sysdate,''SYSTEM'',''ROLE_QS_IMS_ENQ'')';
 
    SOURCE_QAUSER:=SOURCE_QAUSER + 12;
    INSERTED_COUNT:=INSERTED_COUNT+12;
    --------------------------------------------------------------------------------------------------------------------------
    execute immediate SQL_QUERY_ACUSERSECURITY INTO AFTER_ACUSERSECURITY;
    dbms_output.put_line('05-04-insert_into_ac_usersecurity.sql:');
    dbms_output.put_line('Copy user from ' || QA_SCHEMA || '.QS_APP_AUTHORITY to ' || AC_SCHEMA || '.AC_USERSECURITY');
    dbms_output.put_line('CLEANUP:'||DELETED_COUNT);
    dbms_output.put_line('-------------------------------');
    dbms_output.put_line('Source:'||SOURCE_QAUSER);
    dbms_output.put_line('Before:'||BEFORE_ACUSERSECURITY);
    dbms_output.put_line('-------------------------------');
    dbms_output.put_line('After :'||AFTER_ACUSERSECURITY);
    dbms_output.put_line('-------------------------------');
    dbms_output.put_line('Expect:'||(BEFORE_ACUSERSECURITY + SOURCE_QAUSER));
    if(INSERTED_COUNT = (SOURCE_QAUSER)) THEN
    	dbms_output.put_line('Result:'|| 'Pass');
    ELSE
      dbms_output.put_line('Result:'|| 'Fail');
    END IF;
END;
/