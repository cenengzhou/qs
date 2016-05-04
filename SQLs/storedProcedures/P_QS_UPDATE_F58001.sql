create or replace
PROCEDURE P_QS_UPDATE_F58001  AS
	Type Qscurtype Is Ref Cursor; 
	v_mcu	CHAR(12);
	v_dc07	NUMBER;
	v_an8	NUMBER;
	V_Rilt	Char(1);
	V_Mplt	Char(1);
	V_Tme	Char(1);
	V_Conts	Char(1);
	V_Tpfm	Char(1);
	V_Lgl1	Nvarchar2(50);
	V_Tipp	Number;
	v_mppr	NUMBER;
	V_Gnum4	Number;
	V_Scn	Char(10);
	v_tots	NUMBER;
	V_Aaa	Number;
	v_alla	NUMBER;
	v_cuma	NUMBER;
	v_arel	NUMBER;
	v_crcd	CHAR(3);
	v_crr	NUMBER;
	v_sg	CHAR(12);
	v_pmts	CHAR(1);
	v_pnc	NUMBER;
	v_bctr	NUMBER;
	v_fiyr	CHAR(2);
	v_ptc	CHAR(3);
	v_urog	NUMBER;
	V_Urrc	Char(2);
	v_upmj	NUMBER;
	V_Upmt	Number;											
  
	C_Qs_Sc_Package_Insert Qscurtype;
  C_Qs_Sc_Package_UPDATE Qscurtype;
 
  V_Sql Varchar2(4000) := 
	'SELECT lpad(to_char(qj.jobno),12, '' '') as jobno,
          to_number(qsp.packageno) as packageno,
          to_number(decode(qsp.vendorno,null,0,qsp.vendorno)) as vendorno,
          to_char(decode(qsp.labourincluded,null,''0'',qsp.labourincluded)) as labourincluded,
          to_char(decode(qsp.plantincluded,null,''0'',qsp.plantincluded)) as plantincluded,
          to_char(decode(qsp.materialincluded,null,''0'',qsp.materialincluded)) as materialincluded,
          Decode(Upper(Substr(Qsp.Scterm,0,2)||Substr(Qsp.Cpfcalculation,0,2)),''LUNO'',''1'',''RESU'',''2'',''RENO'',''3'',''LUSU'',''4'','' '') As Conts,
          Decode(Upper(Substr(Qsp.Formofsubcontract,0,2)),''MA'',''1'',''MI'',''2'',''CO'',''3'',''IN'',''4'','' '') As Formofsubcontract,
          decode(Qsp.Description, null, '' '' , (decode(sign(50-Lengthb(Qsp.Description)), -1,  SUBSTRb(Qsp.Description,0,50),  Rpad((Qsp.Description), 50, '' '')))) as description,
          Round(To_Number(Decode(Qsp.Maxretpercent, Null, 0, Qsp.Maxretpercent*100)), 0) As Maxpercent,
          round(to_number(decode(qsp.interimretperent, null, 0, qsp.interimretperent*100)), 0) as interimretperent,
          Round(To_Number(Decode(Qsp.Mosretperent, Null, 0, Qsp.Mosretperent*10000)), 0) As Mosretperent,
          decode(qsp.scnature,null,'' '',rpad(qsp.scnature,10, '' '')) as scnature,
          to_number(decode(qsp.remeasuredscsum, null, 0, qsp.remeasuredscsum*100)) as remeasuredscsum,
          to_number(decode(qsp.approvedvoamount, null, 0, qsp.approvedvoamount*100)) as approvedvoamount,
          to_number(decode(qsp.retamount, null, 0, qsp.retamount*100)) as retamount,
          To_Number(Decode(Qsp.Accumlatedret, Null, 0, Qsp.Accumlatedret*100)) As Accumlatedret,
          To_Number(Decode(Qsp.Retrelease, Null, 0, Qsp.Retrelease*100)) As Retrelease,
          Decode(Qsp.Paymentcurrency,Null,Lpad('' '',3,'' ''),Lpad(To_Char(Qsp.Paymentcurrency),3,'' '')) As Paymentcurrency,
          Qsp.Exchangerate,          
          Decode(Qsp.Internaljobno,Null,Lpad('' '',12,'' ''),Lpad(To_Char(Qsp.Internaljobno),12,'' '')) As Internaljobno,
          Decode(Trim(Qsp.Paymentstatus),Null,'' '',Qsp.Paymentstatus) As Paymentstatus,
          Decode(Qsp.Cpfbaseperiod,Null,0,Qsp.Cpfbaseperiod) As Cpfbaseperiod,
          Decode(Qsp.Cpfbaseyear,Null,0,(Qsp.Cpfbaseyear-Mod(Qsp.Cpfbaseyear,2000))/100) As Bctr,
          Decode(Qsp.Cpfbaseyear,Null,Lpad('' '',2,'' ''),Lpad(To_Char((Mod(Qsp.Cpfbaseyear,2000))),2,''0'')) As Fiyr,
          Decode( Qsp.Paymentterms,Null,Lpad('' '',3,'' ''),Lpad(To_Char( Qsp.Paymentterms),3,'' '')) As  Paymentterms,
          To_Number(Decode(Qsp.Originalscsum, Null, 0, Qsp.Originalscsum*10000)) As Originalscsum,
          decode(upper(substr(qsp.retentionterms,0,2)),''LU'',''LS'',''PE'',decode(instr(qsp.retentionterms,''O''),14,''PO'',''PR''),Lpad('' '',2,'' '')) as retentionterms
    FROM qsdatadev.qs_sc_package qsp, qsdatadev.qs_job qj
      Where Not Exists (Select 1
                        From Crystalusr_Dev.F58001 Jde
                        Where  Jde.Chmcu = Lpad(To_Char(Qj.Jobno),12, '' '')
                        And Jde.Chdc07 = Qsp.Packageno)
      and qsp.job_id = qj.id and qsp.system_status = ''ACTIVE'' and qsp.scstatus = ''500''';

	 V_Sql_Update Varchar2(4000) := 
    ' SELECT lpad(to_char(qj.jobno),12, '' '') as jobno,
          to_number(qsp.packageno) as packageno,
          to_number(decode(qsp.vendorno,null,0,qsp.vendorno)) as vendorno,
          to_char(decode(qsp.labourincluded,null,''0'',qsp.labourincluded)) as labourincluded,
          to_char(decode(qsp.plantincluded,null,''0'',qsp.plantincluded)) as plantincluded,
          to_char(decode(qsp.materialincluded,null,''0'',qsp.materialincluded)) as materialincluded,
          Decode(Upper(Substr(Qsp.Scterm,0,2)||Substr(Qsp.Cpfcalculation,0,2)),''LUNO'',''1'',''RESU'',''2'',''RENO'',''3'',''LUSU'',''4'','' '') As Conts,
          Decode(Upper(Substr(Qsp.Formofsubcontract,0,2)),''MA'',''1'',''MI'',''2'',''CO'',''3'',''IN'',''4'','' '') As Formofsubcontract,
          decode(Qsp.Description, null, '' '' , (decode(sign(50-Lengthb(Qsp.Description)), -1,  SUBSTRb(Qsp.Description,0,50),  Rpad((Qsp.Description), 50, '' '')))) as description,
          Round(To_Number(Decode(Qsp.Maxretpercent, Null, 0, Qsp.Maxretpercent*100)), 0) As Maxpercent,
          round(to_number(decode(qsp.interimretperent, null, 0, qsp.interimretperent*100)), 0) as interimretperent,
          Round(To_Number(Decode(Qsp.Mosretperent, Null, 0, Qsp.Mosretperent*10000)), 0) As Mosretperent,
          decode(qsp.scnature,null,'' '',rpad(qsp.scnature,10, '' '')) as scnature,
          to_number(decode(qsp.remeasuredscsum, null, 0, qsp.remeasuredscsum*100)) as remeasuredscsum,
          to_number(decode(qsp.approvedvoamount, null, 0, qsp.approvedvoamount*100)) as approvedvoamount,
          to_number(decode(qsp.retamount, null, 0, qsp.retamount*100)) as retamount,
          To_Number(Decode(Qsp.Accumlatedret, Null, 0, Qsp.Accumlatedret*100)) As Accumlatedret,
          To_Number(Decode(Qsp.Retrelease, Null, 0, Qsp.Retrelease*100)) As Retrelease,
          Decode(Qsp.Paymentcurrency,Null,Lpad('' '',3,'' ''),Lpad(To_Char(Qsp.Paymentcurrency),3,'' '')) As Paymentcurrency,
          Qsp.Exchangerate,          
          Decode(Qsp.Internaljobno,Null,Lpad('' '',12,'' ''),Lpad(To_Char(Qsp.Internaljobno),12,'' '')) As Internaljobno,
          Decode(Trim(Qsp.Paymentstatus),Null,'' '',Qsp.Paymentstatus) As Paymentstatus,
          Decode(Qsp.Cpfbaseperiod,Null,0,Qsp.Cpfbaseperiod) As Cpfbaseperiod,
          Decode(Qsp.Cpfbaseyear,Null,0,(Qsp.Cpfbaseyear-Mod(Qsp.Cpfbaseyear,2000))/100) As Bctr,
          Decode(Qsp.Cpfbaseyear,Null,Lpad('' '',2,'' ''),Lpad(To_Char((Mod(Qsp.Cpfbaseyear,2000))),2,''0'')) As Fiyr,
          Decode( Qsp.Paymentterms,Null,Lpad('' '',3,'' ''),Lpad(To_Char( Qsp.Paymentterms),3,'' '')) As  Paymentterms,
          To_Number(Decode(Qsp.Originalscsum, Null, 0, Qsp.Originalscsum*10000)) As Originalscsum,
          decode(upper(substr(qsp.retentionterms,0,2)),''LU'',''LS'',''PE'',decode(instr(qsp.retentionterms,''O''),14,''PO'',''PR''),Lpad('' '',2,'' '')) as retentionterms  
      From Qsdatadev.Qs_Sc_Package Qsp, Qsdatadev.Qs_Job Qj
      WHERE qsp.job_id = qj.id and qsp.system_status = ''ACTIVE''and qsp.scstatus = ''500''
      MINUS
      SELECT  CHMCU,
              CHDC07,
              Chan8,
              Chrilt,
              CHMPLT,
              Chtme,
              Chconts,
              Chtpfm,
              Chlgl1,
              Chtipp,
              CHMPPR,
              Chgnum4,
              CHSCN,
              CHTOTS,
              CHAAA,
              Challa,
              Chcuma,
              CHAREL,
              Chcrcd,
              Chcrr,
              Chsg,
              Chpmts,
              Chpnc,
              CHBCTR,
              Chfiyr,
              Chptc,
              CHUROG,
              Churrc
      From Crystalusr_Dev.F58001
      
      ';
	
Begin
  Open C_Qs_Sc_Package_Insert For V_Sql;
  Open C_Qs_Sc_Package_UPDATE For V_Sql_Update;
	
	
	SELECT to_number(to_char(sysdate,'yyddd')) + 100000 INTO v_upmj FROM dual;
	Select To_Number(To_Char(Sysdate,'hh24miss')) Into V_Upmt From Dual;
  Dbms_Output.Enable(1000000);
		Loop
			Fetch C_Qs_Sc_Package_UPDATE
			Into V_Mcu, V_Dc07, V_An8, V_Rilt, V_Mplt, V_Tme, V_Conts, V_Tpfm, V_Lgl1, 
      V_Tipp, V_Mppr, V_Gnum4, V_Scn, V_Tots, V_Aaa, V_Alla, V_Cuma, V_Arel, V_Crcd, V_Crr, V_Sg, V_Pmts, V_Pnc, V_Bctr, V_Fiyr, V_Ptc, V_Urog, V_Urrc;
			EXIT WHEN C_Qs_Sc_Package_UPDATE%NOTFOUND;
      DBMS_OUTPUT.PUT_LINE('Update Job:' || V_Mcu || ' ' || V_Dc07);
			Begin				
				Update Crystalusr_Dev.F58001
				Set
					Chan8 = V_An8,
					Chrilt = V_Rilt,
					CHMPLT = v_mplt,
					Chtme = V_Tme,
					Chconts = V_Conts,
					Chtpfm = V_Tpfm,
					Chlgl1 = V_Lgl1,
					Chtipp = V_Tipp,
					CHMPPR = v_mppr,
					CHGNUM4 = v_gnum4,
					CHSCN = v_scn,
					CHTOTS = v_tots,
					CHAAA = v_aaa,
					CHALLA = v_alla,
					CHCUMA = v_cuma,
					CHAREL = v_arel,
					CHCRCD = v_crcd,
					CHCRR = v_crr,
					CHSG = v_sg,
					CHPMTS = v_pmts,
					CHPNC = v_pnc,
					CHBCTR = v_bctr,
					CHFIYR = v_fiyr,
					CHPTC = v_ptc,
					CHUROG = v_urog,
					Churrc = V_Urrc,
          Chtorg = 'QSSPROC02',
					Chpid = 'QS58001',
					Chuser = 'QSSPROC02',
					CHJOBN = 'ERPWLS02',
          CHUPMJ = v_upmj,
					CHUPMT = v_upmt
				WHERE CHMCU = v_mcu and CHDC07 = v_dc07;
			EXCEPTION
				WHEN OTHERS THEN
					DBMS_OUTPUT.PUT_LINE('Error ' || v_mcu || ' ' || v_dc07);
					CONTINUE;
			END;
		End Loop;
    Close C_Qs_Sc_Package_UPDATE;

  Loop
			Fetch C_Qs_Sc_Package_Insert
			Into V_Mcu, V_Dc07, V_An8, V_Rilt, V_Mplt, V_Tme, V_Conts, V_Tpfm, V_Lgl1, 
      V_Tipp, V_Mppr, V_Gnum4, V_Scn, V_Tots, V_Aaa, V_Alla, V_Cuma, V_Arel, V_Crcd, V_Crr, V_Sg, V_Pmts, V_Pnc, V_Bctr, V_Fiyr, V_Ptc, V_Urog, V_Urrc;
			Exit When C_Qs_Sc_Package_Insert%Notfound;
      DBMS_OUTPUT.PUT_LINE('Insert Job:' || V_Mcu || ' ' || V_Dc07);
			Begin				
				Insert Into Crystalusr_Dev.F58001 (
        CHMCU,	CHDC07,	CHAN8,	CHRILT,	CHMPLT,	CHTME,	CHCONTS, CHTPFM, CHLGL1, CHLGL2, 			CHREQR,	CHTIPP,	CHMPPR,	CHSMSF,	CHQSSCST,	CHCSCU,				CHSCN,	CHCT,	CHTYPT,	CHTOTS,	CHAAA,	CHARTG,				CHNOROWS,	CHALLA,	CHCUMA,	CHAREL,	CHCRCD,	CHCRR,	CHNKEYS,	CHCLO,	CHM001,	CHM002,	CHM003,	CHM004,	CHSG,	CHPMTS,	CHPNC,	CHBCTR,	CHFIYR,	CHEV01,	CHWARB,	CHAPPN,	CHEV02,	CHBDDT,	CHARPN,	CHEV03,	CHRCDJ,	CHPNF,	CHEV04,	CHEV05,	CHPTC,	CHEV06,	CHEV07,	CHGNUM4, CHURAB,	CHURAT,	CHURCD, CHURDT,	CHUROG,	CHURQT,	CHURRC,	CHURRF, CHTORG, CHUSER, CHPID, CHJOBN, CHUPMJ,	CHUPMT
        )
        Values (
        v_mcu, v_dc07, V_An8, 	V_Rilt, v_mplt, V_Tme, 	V_Conts, V_Tpfm, V_Lgl1, ' ', 0, 	V_Tipp, v_mppr, ' ', 	'500', 		' ', 	v_scn, 'FF', 	' ', 	v_tots, v_aaa, 	' ', 	0, 			v_alla, v_cuma, v_arel, v_crcd, v_crr, 0,			0, 		0, 		0, 		0, 		0,		v_sg,	v_pmts, v_pnc, v_bctr, v_fiyr, 	' ',	0,		0, 		' ',	0,		0,		' ',	0, 		0, 		' ', 	' ', 	v_ptc, 	' ', 	' ',	v_gnum4, 0, 		0, ' ', 0, 		v_urog, 0, 		v_urrc, ' ', 'QSSPROC02', 'QSSPROC02', 'QS58001', 'ERPWLS02',	v_upmj, v_upmt
        );
			EXCEPTION
				WHEN OTHERS THEN
					DBMS_OUTPUT.PUT_LINE('Error ' || v_mcu || ' ' || v_dc07);
					CONTINUE;
			END;
		End Loop;
    Close C_Qs_Sc_Package_Insert;
  
      DBMS_OUTPUT.PUT_LINE('Duration:  ' || ((To_Number(To_Char(Sysdate,'hh24miss'))) - V_Upmt));
	COMMIT;
	<<Exit_Here>>
  Null;
END P_QS_UPDATE_F58001;