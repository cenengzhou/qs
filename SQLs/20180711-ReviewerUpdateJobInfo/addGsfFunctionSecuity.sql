--JobController.updateJobInfo
insert into ACDATADEV.AC_FUNCTIONSECURITY ( APPLICATIONCODE, ACCESSRIGHT, STATUS, LASTMODIFYDATE, LASTMODIFYUSER, ROLENAME, FUNCTIONNAME ) 
values ( 'QS', 'ENABLE', 'A', sysdate, 'IMS', 'ROLE_QS_REVIEWER', 'F02040301' );
--JobController.updateJobDates
insert into ACDATADEV.AC_FUNCTIONSECURITY ( APPLICATIONCODE, ACCESSRIGHT, STATUS, LASTMODIFYDATE, LASTMODIFYUSER, ROLENAME, FUNCTIONNAME ) 
values ( 'QS', 'ENABLE', 'A', sysdate, 'IMS', 'ROLE_QS_REVIEWER', 'F02040302' );
--JobController.updateJobInfoAndDates
insert into ACDATADEV.AC_FUNCTIONSECURITY ( APPLICATIONCODE, ACCESSRIGHT, STATUS, LASTMODIFYDATE, LASTMODIFYUSER, ROLENAME, FUNCTIONNAME ) 
values ( 'QS', 'ENABLE', 'A', sysdate, 'IMS', 'ROLE_QS_REVIEWER', 'F02040303' );