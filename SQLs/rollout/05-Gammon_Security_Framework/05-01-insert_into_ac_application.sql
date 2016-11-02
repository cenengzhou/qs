--------------------------------------------------------
--  File created - Nov-1-2016   
--------------------------------------------------------
SET SERVEROUTPUT ON
DECLARE
    AC_SCHEMA 				varchar(50) := 'ACDATAPROD';
    SQL_QUERY_AC_APPCODE 	varchar(512):='select (select count(*) as APPCODE from ' || AC_SCHEMA || '.AC_APPLICATION) FROM dual';
    SQL_INSERT_PREFIX 		varchar(512):='Insert into ' || AC_SCHEMA || '.AC_APPLICATION (APPLICATIONCODE,APPLICATIONNAME,STATUS,AUTHENTICATIONMETHOD,SECURITYTYPE,MAINTENANCEWINDOW,MESSAGE,HYPERLINK,STAFFIDREQUIRED,ADDITIONALINFORMATION,LASTMODIFYDATE,LASTMODIFYUSER) values ';
    SOURCE_APPCODE			NUMBER:=1;
    BEFORE_AC_APPCODE	 	NUMBER;
    AFTER_AC_APPCODE	 	NUMBER;
BEGIN
    execute immediate SQL_QUERY_AC_APPCODE INTO BEFORE_AC_APPCODE;
    --------------------------------------------------------------------------------------------------------------------------
    execute immediate SQL_INSERT_PREFIX || '(''QS'',''QS 2.0'',''A'',''WIN'',''MIXED'',null,null,''http://qs'',''Y'',null,sysdate,''GAMSKA\tikywong'')';
    --------------------------------------------------------------------------------------------------------------------------
    execute immediate SQL_QUERY_AC_APPCODE INTO AFTER_AC_APPCODE;
    dbms_output.put_line('05-01-insert_into_ac_application.sql:');
    dbms_output.put_line('Create QS application code at ' || AC_SCHEMA || '.AC_APPLICATION');
    dbms_output.put_line('-------------------------------');
    dbms_output.put_line('Source:'||SOURCE_APPCODE);
    dbms_output.put_line('Before:'||BEFORE_AC_APPCODE);
    dbms_output.put_line('-------------------------------');
    dbms_output.put_line('After :'||AFTER_AC_APPCODE);
    dbms_output.put_line('-------------------------------');
    dbms_output.put_line('Expect:'||(BEFORE_AC_APPCODE + SOURCE_APPCODE));
    if(AFTER_AC_APPCODE = (BEFORE_AC_APPCODE + SOURCE_APPCODE)) THEN
    	dbms_output.put_line('Result:'|| 'Pass');
	ELSE
		dbms_output.put_line('Result:'|| 'Fail');
    END IF;
END;
/