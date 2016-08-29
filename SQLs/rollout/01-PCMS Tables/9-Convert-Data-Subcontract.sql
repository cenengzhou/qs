----------------------------------------------------------------------------------------------------------------
-- Data Conversion from SUBCONTRACT_WORKSCOPE to SUBCONTRACT --
/* 1. To fill in SUBCONTRACT.WORK_SCOPE column with SUBCONTRACT_WORKSCOPE.WORKSCOPE, 
		join with SUBCONTRACT.ID and SUBCONTRACT_WORKSCOPE.SUBCONTRACT_ID
	2. Drop SUBCONTRACT_WORKSCOPE table
*/

update PCMSDATAUAT.subcontract s set work_scope = 
(select min(w.workscope) from PCMSDATAUAT.subcontract_workscope w 
where s.id = w.subcontract_id and w.workscope != ' ' group by w.SUBCONTRACT_ID);
drop table PCMSDATAUAT.SUBCONTRACT_WORKSCOPE;
----------------------------------------------------------------------------------------------------------------


----------------------------------------------------------------------------------------------------------------
-- Data Conversion from DIM_ADDRESS_BOOK to SUBCONTRACT --
/*
	1. To fill SUBCONTRACT.NAME_SUBCONTRACTOR column with DIM_ADDRESS_BOOK.ADDRESS_BOOK_NAME, 
		join with SUBCONTRACT.VENDORNO and DIM_ADDRESS_BOOK._ADDRESS_BOOK_NUMBER
*/
update PCMSDATAUAT.subcontract s set NAME_SUBCONTRACTOR = 
(select a.ADDRESS_BOOK_NAME from ADLUAT.DIM_ADDRESS_BOOK a
where s.VENDORNO = a.ADDRESS_BOOK_NUMBER);
----------------------------------------------------------------------------------------------------------------