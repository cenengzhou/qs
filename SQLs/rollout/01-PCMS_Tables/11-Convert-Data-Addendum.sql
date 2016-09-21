----------------------------------------------------------------------------------------------------------------
--  Grouping existing addendum items in SUBCONTRACT_DETAIL to ADDENDUM & ADDENDUM_DETAIL
/*
	1. Create Addendum 0 for all awarded subcontract at ADDENDUM
	2. Copy a set of Addendum records from SUBCONTRACT_DETAIL to ADDENDUM_DETAIL and associate them to Addendum 0
	3. Copy a set of Attachment records from ATTACH_SUBCONTRACT_DETAIL to ATTACHMENT setting ID_TABLE = ADDENDUM
	4. Calculate and Summarize figures and info at ADDENDUM
*/
----------------------------------------------------------------------------------------------------------------
WHENEVER SQLERROR EXIT FAILURE ROLLBACK;
--1. Create Addendum 0 for all awarded subcontract at ADDENDUM
delete from PCMSDATAUAT.ADDENDUM where no = 0;
insert into PCMSDATAUAT.ADDENDUM (
ID,
ID_SUBCONTRACT,
NO_JOB,
NO_SUBCONTRACT,
DESCRIPTION_SUBCONTRACT,
NO_SUBCONTRACTOR,
NAME_SUBCONTRACTOR,
NO,
TITLE,
AMT_SUBCONTRACT_REMEASURED,
AMT_SUBCONTRACT_REVISED,
AMT_ADDENDUM_TOTAL,
AMT_ADDENDUM_TOTAL_TBA,
AMT_ADDENDUM,
AMT_SUBCONTRACT_REVISED_TBA,
DATE_SUBMISSION,
DATE_APPROVAL,
STATUS,
STATUS_APPROVAL,
USERNAME_PREPARED_BY,
REMARKS,
USERNAME_CREATED,
DATE_CREATED,
USERNAME_LAST_MODIFIED,
DATE_LAST_MODIFIED,
NO_ADDENDUM_DETAIL_NEXT)
(select
PCMSDATAUAT.ADDENDUM_SEQ.nextval as ID,
s.ID as ID_SUBCONTRACT,
j.JOBNO as NO_JOB,
s.PACKAGENO as NO_SUBCONTRACT,
s.DESCRIPTION as DESCRIPTION_SUBCONTRACT,
case 
	when s.VENDORNO is NULL then '-1'
	else s.VENDORNO end as NO_SUBCONTRACTOR,
s.NAME_SUBCONTRACTOR as NAME_SUBCONTRACTOR,
0 as NO,
'Addendum Title' as TITLE,
s.REMEASUREDSCSUM as AMT_SUBCONTRACT_REMEASURED,
s.REMEASUREDSCSUM + s.APPROVEDVOAMOUNT as AMT_SUBCONTRACT_REVISED,
s.APPROVEDVOAMOUNT as AMT_ADDENDUM_TOTAL,
s.APPROVEDVOAMOUNT as AMT_ADDENDUM_TOTAL_TBA,
0 as AMT_ADDENDUM,
s.REMEASUREDSCSUM + s.APPROVEDVOAMOUNT as AMT_SUBCONTRACT_REVISED_TBA,
null as DATE_SUBMISSION,
null as DATE_APPROVAL,
'APPROVED' as STATUS,
'APPROVED' as STATUS_APPROVAL,
'SYSTEM' as USERNAME_PREPARED_BY,
'Generated by system' as REMARKS,
'SYSTEM' as USERNAME_CREATED,
sysdate as DATE_CREATED,
'SYSTEM' as USERNAME_LAST_MODIFIED,
sysdate as DATE_LAST_MODIFIED,
null as NO_ADDENDUM_DETAIL_NEXT
from PCMSDATAUAT.SUBCONTRACT s left join PCMSDATAUAT.JOB_INFO j 
on s.JOB_INFO_ID = j.ID
where j.JOBNO is not null
and s.SCSTATUS = '500'
and s.SYSTEM_STATUS = 'ACTIVE');
--2. Copy a set of Addendum records from SUBCONTRACT_DETAIL to ADDENDUM_DETAIL and associate them to Addendum 0
delete from PCMSDATAUAT.ADDENDUM_DETAIL where no = 0;
insert into PCMSDATAUAT.ADDENDUM_DETAIL (
ID,
ID_ADDENDUM,
DATE_LAST_MODIFIED,
USERNAME_LAST_MODIFIED,
DATE_CREATED,
USERNAME_CREATED,
RATE_BUDGET_TBA,
AMT_BUDGET_TBA,
NO,
ID_HEADER_REF,
TYPE_HD,
TYPE_ACTION,
ID_SUBCONTRACT_DETAIL,
NO_JOB,
ID_RESOURCE_SUMMARY,
BPI,
DESCRIPTION,
QUANTITY,
RATE_ADDENDUM,
CODE_OBJECT,
CODE_SUBSIDIARY,
TYPE_VO,
UNIT,
REMARKS,
NO_SUBCONTRACT,
RATE_BUDGET,
QUANTITY_TBA,
RATE_ADDENDUM_TBA,
NO_SUBCONTRACT_CHARGED_REF,
CODE_OBJECT_FOR_DAYWORK,
AMT_ADDENDUM,
AMT_BUDGET,
AMT_ADDENDUM_TBA
)
(select 
PCMSDATAUAT.ADDENDUM_DETAIL_SEQ.nextval as ID,
a.ID as ID_ADDENDUM,
sysdate as DATE_LAST_MODIFIED,
'SYSTEM' as USERNAME_LAST_MODIFIED,
sysdate as DATE_CREATED,
'SYSTEM' as USERNAME_CREATED,
0 as RATE_BUDGET_TBA,
0 as AMT_BUDGET_TBA,
0 as NO,
0 as ID_HEADER_REF,
'DETAIL' as TYPE_HD,
'ADD' as TYPE_ACTION,
sd.ID as ID_SUBCONTRACT_DETAIL,
sd.JOBNO as NO_JOB,
sd.RESOURCENO as ID_RESOURCE_SUMMARY,
sd.BILLITEM as BPI,
sd.DESCRIPTION as DESCRIPTION,
sd.QUANTITY as QUANTITY,
sd.SCRATE as RATE_ADDENDUM,
sd.OBJECTCODE as CODE_OBJECT,
sd.SUBSIDIARYCODE as CODE_SUBSIDIARY,
sd.LINETYPE as TYPE_VO,
sd.UNIT as UNIT,
sd.SCREMARK as REMARKS,
s.PACKAGENO as NO_SUBCONTRACT,
sd.COSTRATE as RATE_BUDGET,
sd.TOBEAPPROVEDQTY as QUANTITY_TBA,
sd.TOBEAPPROVEDRATE as RATE_ADDENDUM_TBA,
sd.CONTRACHARGESCNO as NO_SUBCONTRACT_CHARGED_REF,
sd.ALTOBJECTCODE as CODE_OBJECT_FOR_DAYWORK,
sd.AMT_SUBCONTRACT as AMT_ADDENDUM,
sd.AMT_BUDGET as AMT_BUDGET,
sd.AMT_SUBCONTRACT_TBA as AMT_ADDENDUM_TBA
from PCMSDATAUAT.SUBCONTRACT_DETAIL sd left join PCMSDATAUAT.SUBCONTRACT s
on sd.SUBCONTRACT_ID = s.id left join PCMSDATAUAT.ADDENDUM a
on sd.SUBCONTRACT_ID = a.ID_SUBCONTRACT 
where sd.LINETYPE in ('V1', 'V2', 'V3', 'L1', 'L2', 'D1', 'D2', 'CF')
and sd.SYSTEM_STATUS = 'ACTIVE'
and s.SCSTATUS = '500'
and a.no = 0
);
--3. Copy a set of Attachment records from ATTACH_SUBCONTRACT_DETAIL to ATTACHMENT setting ID_TABLE = ADDENDUM
delete from PCMSDATAUAT.ATTACHMENT where NAME_TABLE = 'ADDENDUM';
insert into PCMSDATAUAT.ATTACHMENT (
ID,
ID_TABLE,
NAME_TABLE,
NO_SEQUENCE,
TYPE_DOCUMENT,
NAME_FILE,
PATH_FILE,
TEXT,
USERNAME_CREATED,
DATE_CREATED,
USERNAME_LAST_MODIFIED,
DATE_LAST_MODIFIED
)
(select
PCMSDATAUAT.ATTACHMENT_SEQ.nextval as ID,
ad.ID_ADDENDUM as ID_TABLE,
'ADDENDUM' as NAME_TABLE,
(sd.SEQUENCENO * 10000) + asd.SEQUENCENO as NO_SEQUENCE,
asd.DOCUMENTTYPE as TYPE_DOCUMENT,
asd.FILENAME as NAME_FILE,
asd.FILELINK as PATH_FILE,
asd.TEXTATTACHMENT as TEXT,
asd.CREATED_USER as USERNAME_CREATED,
asd.CREATED_DATE as DATE_CREATED,
asd.LAST_MODIFIED_USER as USERNAME_LAST_MODIFIED,
asd.LAST_MODIFIED_DATE as DATE_LAST_MODIFIED
from PCMSDATAUAT.ATTACH_SUBCONTRACT_DETAIL asd left join PCMSDATAUAT.ADDENDUM_DETAIL ad
on asd.SUBCONTRACT_DETAIL_ID = ad.ID_SUBCONTRACT_DETAIL left join PCMSDATAUAT.SUBCONTRACT_DETAIL sd
on asd.SUBCONTRACT_DETAIL_ID = sd.ID
where ad.ID_ADDENDUM is not null
and ad.no = 0
and asd.SYSTEM_STATUS = 'ACTIVE'
);
--4. Calculate and Summarize figures and info at ADDENDUM
update PCMSDATAUAT.ADDENDUM a set AMT_ADDENDUM = (
  select sum(ad.AMT_BUDGET) from PCMSDATAUAT.ADDENDUM_DETAIL ad 
  where ad.ID_ADDENDUM = a.id group by ad.ID_ADDENDUM
);