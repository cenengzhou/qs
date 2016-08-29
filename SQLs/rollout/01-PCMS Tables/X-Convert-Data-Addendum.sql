----------------------------------------------------------------------------------------------------------------
--  Grouping existing addendum items in SUBCONTRACT_DETAIL to ADDENDUM & ADDENDUM_DETAIL
/*
	1. Create Addendum 0 for all awarded subcontract at ADDENDUM
	2. Copy a set of Addendum records from SUBCONTRACT_DETAIL to ADDENDUM_DETAIL and associate them to Addendum 0
	3. Copy a set of Attachment records from ATTACH_SUBCONTRACT_DETAIL to ATTACHMENT setting ID_TABLE = ADDENDUM
	4. Calculate and Summarize figures and info at ADDENDUM
*/
----------------------------------------------------------------------------------------------------------------
