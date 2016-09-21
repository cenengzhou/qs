--------------------------------------------------------
--  File created - Monday-August-22-2016   
--------------------------------------------------------
REM INSERTING into ACDATAUAT.AC_ROLE
SET DEFINE OFF;
Insert into ACDATAUAT.AC_ROLE (APPLICATIONCODE,ROLENAME,JOB,ROLEDESCRIPTION,TYPE,TEMPLATE,LASTMODIFYDATE,LASTMODIFYUSER) values ('PCMS','JOB_ALL','NA','All jobs available in Gammon','J','N', sysdate,'GAMSKA\tikywong');
Insert into ACDATAUAT.AC_ROLE (APPLICATIONCODE,ROLENAME,JOB,ROLEDESCRIPTION,TYPE,TEMPLATE,LASTMODIFYDATE,LASTMODIFYUSER) values ('PCMS','JOB_CX','NA','All CX jobs of Gammon','J','N', sysdate,'GAMSKA\tikywong');
Insert into ACDATAUAT.AC_ROLE (APPLICATIONCODE,ROLENAME,JOB,ROLEDESCRIPTION,TYPE,TEMPLATE,LASTMODIFYDATE,LASTMODIFYUSER) values ('PCMS','JOB_EM_CLP','NA','E&M CLP job only','J','N', sysdate,'GAMSKA\tikywong');
Insert into ACDATAUAT.AC_ROLE (APPLICATIONCODE,ROLENAME,JOB,ROLEDESCRIPTION,TYPE,TEMPLATE,LASTMODIFYDATE,LASTMODIFYUSER) values ('PCMS','JOB_GLJV','NA','All Gammon Leighton Joint Venture jobs','J','N', sysdate,'GAMSKA\tikywong');
Insert into ACDATAUAT.AC_ROLE (APPLICATIONCODE,ROLENAME,JOB,ROLEDESCRIPTION,TYPE,TEMPLATE,LASTMODIFYDATE,LASTMODIFYUSER) values ('PCMS','JOB_HKG','NA','All Hong Kong, China & Macau jobs of Gammon','J','N', sysdate,'GAMSKA\tikywong');
Insert into ACDATAUAT.AC_ROLE (APPLICATIONCODE,ROLENAME,JOB,ROLEDESCRIPTION,TYPE,TEMPLATE,LASTMODIFYDATE,LASTMODIFYUSER) values ('PCMS','JOB_SCL1111','NA','SCL1111 jobs only','J','N', sysdate,'GAMSKA\tikywong');
Insert into ACDATAUAT.AC_ROLE (APPLICATIONCODE,ROLENAME,JOB,ROLEDESCRIPTION,TYPE,TEMPLATE,LASTMODIFYDATE,LASTMODIFYUSER) values ('PCMS','JOB_SGP','NA','All Singapore jobs of Gammon','J','N', sysdate,'GAMSKA\tikywong');
Insert into ACDATAUAT.AC_ROLE (APPLICATIONCODE,ROLENAME,JOB,ROLEDESCRIPTION,TYPE,TEMPLATE,LASTMODIFYDATE,LASTMODIFYUSER) values ('PCMS','JOB_TAMAR','NA','Tamar job only','J','N', sysdate,'GAMSKA\tikywong');
Insert into ACDATAUAT.AC_ROLE (APPLICATIONCODE,ROLENAME,JOB,ROLEDESCRIPTION,TYPE,TEMPLATE,LASTMODIFYDATE,LASTMODIFYUSER) values ('PCMS','JOB_TAMAR_EM','NA','Tamar E&M job only','J','N', sysdate,'GAMSKA\tikywong');
Insert into ACDATAUAT.AC_ROLE (APPLICATIONCODE,ROLENAME,JOB,ROLEDESCRIPTION,TYPE,TEMPLATE,LASTMODIFYDATE,LASTMODIFYUSER) values ('PCMS','ROLE_PCMS_ENQ','NA','Any user who has to access PCMS has to be granted with this role','F','N', sysdate,'GAMSKA\tikywong');
Insert into ACDATAUAT.AC_ROLE (APPLICATIONCODE,ROLENAME,JOB,ROLEDESCRIPTION,TYPE,TEMPLATE,LASTMODIFYDATE,LASTMODIFYUSER) values ('PCMS','ROLE_PCMS_IMS_ADMIN','NA','IMS who can handle PCMS application settings','F','N', sysdate,'GAMSKA\tikywong');
Insert into ACDATAUAT.AC_ROLE (APPLICATIONCODE,ROLENAME,JOB,ROLEDESCRIPTION,TYPE,TEMPLATE,LASTMODIFYDATE,LASTMODIFYUSER) values ('PCMS','ROLE_PCMS_IMS_ENQ','NA','IMS who can view PCMS applications settings','F','N', sysdate,'GAMSKA\tikywong');
Insert into ACDATAUAT.AC_ROLE (APPLICATIONCODE,ROLENAME,JOB,ROLEDESCRIPTION,TYPE,TEMPLATE,LASTMODIFYDATE,LASTMODIFYUSER) values ('PCMS','ROLE_PCMS_QS','NA','QS who can handle daily operations','F','N', sysdate,'GAMSKA\tikywong');
Insert into ACDATAUAT.AC_ROLE (APPLICATIONCODE,ROLENAME,JOB,ROLEDESCRIPTION,TYPE,TEMPLATE,LASTMODIFYDATE,LASTMODIFYUSER) values ('PCMS','ROLE_PCMS_QS_ENQ','NA','QS Power User who can view administrative tasks','F','N', sysdate,'GAMSKA\tikywong');
Insert into ACDATAUAT.AC_ROLE (APPLICATIONCODE,ROLENAME,JOB,ROLEDESCRIPTION,TYPE,TEMPLATE,LASTMODIFYDATE,LASTMODIFYUSER) values ('PCMS','ROLE_PCMS_QS_ADMIN','NA','QS Power User who can handle administrative tasks','F','N', sysdate,'GAMSKA\tikywong');
Insert into ACDATAUAT.AC_ROLE (APPLICATIONCODE,ROLENAME,JOB,ROLEDESCRIPTION,TYPE,TEMPLATE,LASTMODIFYDATE,LASTMODIFYUSER) values ('PCMS','ROLE_PCMS_QS_REVIEWER','NA','QS Reviewer who review daily operations that are done by QSs','F','N', sysdate,'GAMSKA\tikywong');