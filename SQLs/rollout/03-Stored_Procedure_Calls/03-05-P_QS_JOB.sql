grant select, update On "CRPDTA"."F0006" to PCMSDATAUAT;

create or replace PROCEDURE PCMSDATAUAT.P_QS_JOB IS
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
/*Cursor to retrieve data from QS_JOB*/
CURSOR c_QS_JOB IS
select ID, JOBNO, DESCRIPTION, COMPANY, EMPLOYER, CONTRACTTYPE, DIVISION, DEPARTMENT, INTERNALJOB, SOLOJV, COMPLETIONSTATUS, INSURANCECAR, INSURANCEECI, INSURANCETPL
from PCMSDATAUAT.JOB_INFO;
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
 /*Update Job Information in QS System (QS_JOB) from JDE (F0006)*/
 OPEN c_QS_JOB;
 LOOP
  FETCH c_QS_JOB
  INTO v_id, v_jobno, v_description, v_company, v_employer, v_contracttype, v_division, v_department, v_internaljob, v_solojv, v_complestatus, v_insurcar, v_insurecl, v_insurtpl;
  EXIT WHEN c_QS_JOB%NOTFOUND;
  /*Check count of Job Information from JDE*/
  select count(1) into v_count1_JDE from CRPDTA.F0006 where trim(MCMCU)=v_jobno;
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
	decode(trim(MCRP15),null,' ',trim(MCRP15)) 
	into v_description_MCDL01, v_company_MCCO, v_employer_AN8O, v_contracttype_MCCT, v_division_MCRP03, v_department_MCRP04, 
	v_internaljob_MCRP06, v_solojv_MCRP10, v_complestatus_MCRP12, v_insurcar_MCRP13, v_insurecl_MCRP14, v_insurtpl_MCRP15
	from CRPDTA.F0006 where trim(MCMCU)=v_jobno;
    /*Check if Job Information should be updated*/
	IF (v_description_MCDL01<>v_description OR v_company_MCCO<>v_company OR v_employer_AN8O<>v_employer OR
	v_contracttype_MCCT<>v_contracttype OR v_division_MCRP03<>v_division OR v_department_MCRP04<>v_department OR 
	v_internaljob_MCRP06<>v_internaljob OR v_solojv_MCRP10<>v_solojv OR v_complestatus_MCRP12<>v_complestatus OR 
	v_insurcar_MCRP13<>v_insurcar OR v_insurecl_MCRP14<>v_insurecl OR v_insurtpl_MCRP15<>v_insurtpl OR
	v_contracttype is null OR v_division is null OR v_department is null OR 
	v_internaljob is null OR v_solojv is null OR v_complestatus is null OR 
	v_insurcar is null OR v_insurecl is null OR v_insurtpl is null) THEN
      v_commitcount := v_commitcount + 1;
      /*Update Job Information in QS System*/
	  update PCMSDATAUAT.JOB_INFO 
	  set DESCRIPTION=v_description_MCDL01, COMPANY=v_company_MCCO, EMPLOYER=v_employer_AN8O, CONTRACTTYPE=v_contracttype_MCCT, 
	  DIVISION=v_division_MCRP03, DEPARTMENT=v_department_MCRP04, INTERNALJOB=v_internaljob_MCRP06, SOLOJV=v_solojv_MCRP10, 
	  COMPLETIONSTATUS=v_complestatus_MCRP12, INSURANCECAR=v_insurcar_MCRP13, INSURANCEECI=v_insurecl_MCRP14, INSURANCETPL=v_insurtpl_MCRP15, 
	  LAST_MODIFIED_DATE=sysdate, LAST_MODIFIED_USER='QSSPROC01' 
	  where JOBNO=v_jobno;
      /*Commit for every 1000 records*/
	  IF v_commitcount >= 1000 THEN
        v_commitcount := 0;
        COMMIT;
      END IF;
    END IF;
  END IF;
 END LOOP;
 CLOSE c_QS_JOB;
COMMIT;
END;

grant EXECUTE on "PCMSDATAUAT"."P_QS_JOB" to "PCMSUSER_ROLE";