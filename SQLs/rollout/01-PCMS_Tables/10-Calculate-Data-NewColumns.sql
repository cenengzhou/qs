--------------------------------------------------------
--  Calculate Subcontract Detail new columns
--------------------------------------------------------
-- select * from SUBCONTRACT_DETAIL;
UPDATE PCMSDATATST.SUBCONTRACT_DETAIL set AMT_SUBCONTRACT = round(round(QUANTITY, 4) * round(SCRATE, 4), 2) where SCRATE is not null;
UPDATE PCMSDATATST.SUBCONTRACT_DETAIL set AMT_SUBCONTRACT_NEW = AMT_SUBCONTRACT where SCRATE is not null;
UPDATE PCMSDATATST.SUBCONTRACT_DETAIL set AMT_BUDGET = round(round(QUANTITY, 4) * round(COSTRATE, 4), 2) where COSTRATE is not null;
UPDATE PCMSDATATST.SUBCONTRACT_DETAIL set AMT_SUBCONTRACT_TBA = round(round(TOBEAPPROVEDQTY, 4) * round(TOBEAPPROVEDRATE, 4), 2) where TOBEAPPROVEDRATE is not null;
UPDATE PCMSDATATST.SUBCONTRACT_DETAIL set AMT_CUMULATIVE_CERT = round(round(CUMCERTQTY, 4) * round(SCRATE, 4), 2) where SCRATE is not null;
UPDATE PCMSDATATST.SUBCONTRACT_DETAIL set AMT_POSTED_CERT = round(round(POSTEDCERTQTY, 4) * round(SCRATE, 4), 2) where SCRATE is not null;
UPDATE PCMSDATATST.SUBCONTRACT_DETAIL set AMT_CUMULATIVE_WD = round(round(CUMWDQTY, 4) * round(SCRATE, 4), 2) where COSTRATE is not null;
UPDATE PCMSDATATST.SUBCONTRACT_DETAIL set AMT_POSTED_WD = round(round(POSTEDWDQTY, 4) * round(SCRATE, 4), 2) where COSTRATE is not null;

--------------------------------------------------------
--  Calculate Resource Summary new columns
--------------------------------------------------------
 -- select * from PCMSDATATST.RESOURCE_SUMMARY;
UPDATE PCMSDATATST.RESOURCE_SUMMARY set AMT_BUDGET = round(round(QUANTITY, 4) * round(RATE, 4), 2) where RATE is not null;

--------------------------------------------------------
--  Calculate BPI Item Resource new columns
--------------------------------------------------------
-- select * from PCMSDATATST.BPI_ITEM_RESOURCE;
UPDATE PCMSDATATST.BPI_ITEM_RESOURCE set AMT_BUDGET = round(round(QUANTITY, 4) * round(COSTRATE, 4) * round(REMEASUREDFACTOR, 4), 2) where COSTRATE is not null;

--------------------------------------------------------
--  Calculate BPI Item new columns
--------------------------------------------------------
-- select * from PCMSDATATST.BPI_ITEM;
UPDATE PCMSDATATST.BPI_ITEM set AMT_BUDGET = round(round(REMEASUREDQTY, 4) * round(COSTRATE, 4), 2) where COSTRATE is not null;
UPDATE PCMSDATATST.BPI_ITEM set AMT_SELLING = round(round(REMEASUREDQTY, 4) * round(SELLINGRATE, 4), 2) where SELLINGRATE is not null and REMEASUREDQTY * SELLINGRATE > -99999999999999999 and  REMEASUREDQTY * SELLINGRATE < 99999999999999999;

--------------------------------------------------------
--  Calculate Tender Detail new columns
--------------------------------------------------------
-- select * from PCMSDATATST.TENDER_DETAIL;
UPDATE PCMSDATATST.TENDER_DETAIL set AMT_BUDGET = round(round(QUANTITY, 4) * round(RATE_BUDGET, 4), 2) where RATE_BUDGET is not null;
UPDATE PCMSDATATST.TENDER_DETAIL set AMT_SUBCONTRACT = round(round(QUANTITY, 4) * round(RATE_SUBCONTRACT, 4), 2) where RATE_SUBCONTRACT is not null;