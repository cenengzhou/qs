ALTER TABLE PCMSDATAPROD.TENDER ADD AMT_BUYING_GAIN_LOSS NUMBER(19, 2) DEFAULT 0;
ALTER TABLE PCMSDATAPROD.TENDER ADD REMARKS VARCHAR2(500 CHAR) DEFAULT ' ';
ALTER TABLE PCMSDATAPROD.TENDER ADD STATUS_CHANGE_EXECUTION_OF_SC VARCHAR2(10 CHAR) DEFAULT 'N/A';
ALTER TABLE PCMSDATAPROD.TENDER ADD USERNAME_PREPARED VARCHAR2(20 CHAR);
ALTER TABLE PCMSDATAPROD.TENDER ADD DATE_PREPARED DATE;
ALTER TABLE PCMSDATAPROD.TENDER ADD NAME_SUBCONTRACTOR VARCHAR2(500 CHAR) DEFAULT ' ';
