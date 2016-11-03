spool c:\9.log;
set echo on;

-- WHENEVER SQLERROR EXIT FAILURE ROLLBACK;
----------------------------------------------------------------------------------------------------------------
-- Data Conversion from SUBCONTRACT_WORKSCOPE to SUBCONTRACT --
/* 1. To fill in SUBCONTRACT.WORK_SCOPE column with SUBCONTRACT_WORKSCOPE.WORKSCOPE, 
		join with SUBCONTRACT.ID and SUBCONTRACT_WORKSCOPE.SUBCONTRACT_ID
	2. Drop SUBCONTRACT_WORKSCOPE table
*/

update PCMSDATAPROD.subcontract s set work_scope = 
(select min(w.workscope) from PCMSDATAPROD.subcontract_workscope w 
where s.id = w.subcontract_id and w.workscope != ' ' group by w.SUBCONTRACT_ID);
drop table PCMSDATAPROD.SUBCONTRACT_WORKSCOPE;
----------------------------------------------------------------------------------------------------------------

spool off;