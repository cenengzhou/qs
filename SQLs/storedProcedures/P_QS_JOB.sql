create or replace
PROCEDURE            P_QS_JOB IS
/*==============================*/
/*Objective: Update Job Information (QS_JOB) in QS System if there are changes on Job Information (F0006) in JDE*/
/*Store Procedure ID: QSSPROC01 */
/*Version: 1.0 by Vincent Mok - 09 May 2011 */
/*==============================*/
  v_commitcount  NUMBER(5,0);
  v_count1_JDE   NUMBER(5,0);
  /*Variable for QS_APP_USER*/
  v_id           NUMBER(19,0);
  v_jobno        VARCHAR2(12);
  v_description  VARCHAR2(256);
  v_company      VARCHAR2(12);
  v_employer     VARCHAR2(12);
  v_contracttype VARCHAR2(10);
  v_division     VARCHAR2(10);
  v_department   VARCHAR2(10);
  v_internaljob  VARCHAR2(10);
  v_solojv       VARCHAR2(10);
  v_complestatus VARCHAR2(10);
  v_insurcar     VARCHAR2(10);
  v_insurecl     VARCHAR2(10);
  v_insurtpl     VARCHAR2(10);
  v_ultclient    VARCHAR2(12);
  v_natureOfWork VARCHAR2(3);
  v_clientType   VARCHAR2(50);
  v_tenderNo     VARCHAR2(20);
  v_location     VARCHAR2(200);
  /*Variable for F0006*/
  v_description_MCDL01  VARCHAR2(30);
  v_company_MCCO        VARCHAR2(12);
  v_employer_AN8O       VARCHAR2(12);
  v_contracttype_MCCT   VARCHAR2(10);
  v_division_MCRP03     VARCHAR2(10);
  v_department_MCRP04   VARCHAR2(10);
  v_internaljob_MCRP06  VARCHAR2(10);
  v_solojv_MCRP10       VARCHAR2(10);
  v_complestatus_MCRP12 VARCHAR2(10);
  v_insurcar_MCRP13     VARCHAR2(10);
  v_insurecl_MCRP14     VARCHAR2(10);
  v_insurtpl_MCRP15     VARCHAR2(10);
  v_ultclient_MCANPA    VARCHAR2(12);
  v_natureOfWork_MCRP18 VARCHAR2(3);
  v_clientType_MCRP07   VARCHAR2(50);
  v_tenderNo_MCDL04     VARCHAR2(20);
  v_location_MCDL03     VARCHAR2(30);
  
/*Cursor to retrieve data from QS_JOB*/
CURSOR c_QS_JOB IS
select ID, JOBNO, DESCRIPTION, COMPANY, EMPLOYER, CONTRACTTYPE, DIVISION, DEPARTMENT, INTERNALJOB, SOLOJV, COMPLETIONSTATUS, INSURANCECAR, INSURANCEECI, INSURANCETPL, CLIENT_ULT, NATURE_OF_WORK_GAMMON, CLIENT_TYPE, NO_TENDER, PROJECT_LOCATION
from PCMSDATADEV.JOB_INFO;
BEGIN
  v_commitcount := 0;
  v_description :=' ';
  v_company :=' ';
  v_employer :=' ';
  v_contracttype :=' ';
  v_division :=' ';
  v_department :=' ';
  v_internaljob :=' ';
  v_solojv :=' ';
  v_complestatus :=' ';
  v_insurcar :=' ';
  v_insurecl :=' ';
  v_insurtpl :=' ';
  v_description_MCDL01 :=' ';
  v_company_MCCO :=' ';
  v_employer_AN8O :=' ';
  v_contracttype_MCCT :=' ';
  v_division_MCRP03 :=' ';
  v_department_MCRP04 :=' ';
  v_internaljob_MCRP06 :=' ';
  v_solojv_MCRP10 :=' ';
  v_complestatus_MCRP12 :=' ';
  v_insurcar_MCRP13 :=' ';
  v_insurecl_MCRP14 :=' ';
  v_insurtpl_MCRP15 :=' ';
  v_ultclient_MCANPA := ' ';
  v_natureOfWork_MCRP18 := ' ';
  v_clientType_MCRP07 := ' ';
  v_tenderNo_MCDL04 := ' ';
  v_location_MCDL03 := ' ';
 /*Update Job Information in QS System (QS_JOB) from JDE (F0006)*/
 OPEN c_QS_JOB;
 LOOP
  FETCH c_QS_JOB
  INTO v_id, v_jobno, v_description, v_company, v_employer, v_contracttype, v_division, v_department, v_internaljob, v_solojv, v_complestatus, v_insurcar, v_insurecl, v_insurtpl, v_ultclient, v_natureOfWork, v_clientType, v_tenderNo, v_location;
  EXIT WHEN c_QS_JOB%NOTFOUND;
  /*Check count of Job Information from JDE*/
  select count(1) into v_count1_JDE from TESTDTA.F0006 where trim(MCMCU)=v_jobno;
  IF v_count1_JDE > 0 THEN
    /*Retrieve Job Information from JDE*/
	select trim(MCDL01), trim(MCCO), to_char(MCAN8O), 
	decode(trim(MCCT),null,' ',trim(MCCT)), 
	decode(trim(MCRP03),null,' ',trim(MCRP03)), 
	decode(trim(MCRP04),null,' ',trim(MCRP04)), 
	decode(trim(MCRP06),null,' ',trim(MCRP06)), 
	decode(trim(MCRP10),null,' ',trim(MCRP10)), 
	decode(trim(MCRP12),null,' ',trim(MCRP12)), 
	decode(trim(MCRP13),null,' ',trim(MCRP13)), 
	decode(trim(MCRP14),null,' ',trim(MCRP14)), 
	decode(trim(MCRP15),null,' ',trim(MCRP15)),
 	decode(trim(MCANPA),null,' ',trim(MCANPA)),
  decode(trim(MCRP18),null,' ',trim(MCRP18)),
  decode(trim(MCRP07),'PRI','Private','PUB', 'Public', 'QUA', 'Quasi-Public'),
  decode(trim(MCDL04),null,' ',(decode(sign(20-Lengthb(trim(MCDL04))), -1,  SUBSTRb(trim(MCDL04),0,20), trim(MCDL04)))),
  decode(trim(MCDL03),null,' ',trim(MCDL03))
  
	into v_description_MCDL01, v_company_MCCO, v_employer_AN8O, v_contracttype_MCCT, v_division_MCRP03, v_department_MCRP04, v_internaljob_MCRP06, v_solojv_MCRP10,
       v_complestatus_MCRP12, v_insurcar_MCRP13, v_insurecl_MCRP14, v_insurtpl_MCRP15, v_ultclient_MCANPA, v_natureOfWork_MCRP18, v_clientType_MCRP07, v_tenderNo_MCDL04,
       v_location_MCDL03
	from TESTDTA.F0006 where trim(MCMCU)=v_jobno;
    /*Check if Job Information should be updated*/
 
  
 -- THEN
      v_commitcount := v_commitcount + 1;
      --DBMS_OUTPUT.PUT_LINE('Update Job:' || v_jobno || ' ' || v_location_MCDL03);
      /*Update Job Information in QS System*/
	  update PCMSDATADEV.JOB_INFO 
	  set DESCRIPTION=v_description_MCDL01, COMPANY=v_company_MCCO, EMPLOYER=v_employer_AN8O, CONTRACTTYPE=v_contracttype_MCCT, 
	  DIVISION=v_division_MCRP03, DEPARTMENT=v_department_MCRP04, INTERNALJOB=v_internaljob_MCRP06, SOLOJV=v_solojv_MCRP10, 
	  COMPLETIONSTATUS=v_complestatus_MCRP12, INSURANCECAR=v_insurcar_MCRP13, INSURANCEECI=v_insurecl_MCRP14, INSURANCETPL=v_insurtpl_MCRP15, CLIENT_ULT=v_ultclient_MCANPA,
    NATURE_OF_WORK_GAMMON = v_natureOfWork_MCRP18, CLIENT_TYPE = v_clientType_MCRP07, NO_TENDER = v_tenderNo_MCDL04, PROJECT_LOCATION = v_location_MCDL03,
	  LAST_MODIFIED_DATE=sysdate, LAST_MODIFIED_USER='QSSPROC01' 
	  where JOBNO=v_jobno;
      /*Commit for every 100 records*/
	  IF v_commitcount >= 100 THEN
        v_commitcount := 0;
        COMMIT;
      END IF;
    --END IF;
  END IF;
 END LOOP;
 CLOSE c_QS_JOB;
COMMIT;
END;