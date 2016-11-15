create or replace PROCEDURE            PCMSDATAPROD.P_QS_UPDATE_F58011  AS
	Type Qscurtype Is Ref Cursor;

	v_mcu	CHAR(12);
	V_Dc07	Number;
	V_Algn	Number;
	V_Prst 	Char(3);
	V_Urab	Number;
	V_Urdt	Number;
	V_Apdj 	Number;
	V_Apd 	Number;
	V_Pipf	Char(1);
	V_Cuma	Number;
	v_Aaa	NUMBER;
	v_upmj	NUMBER;
	V_Upmt	Number;

	C_Qs_Scpayment_Cert Qscurtype;


   V_Sql Varchar2(2000) :=
    ' Select Lpad(To_Char(Qsp.Jobno),12, '' '') Jobno,
              To_Number(Qsp.Packageno) As Packageno,
              To_Number(Qsp.Paymentcertno) As Paymentcertno,
              Decode(Qsp.Paymentstatus, Null, Lpad('' '', 3, '' ''), Qsp.Paymentstatus) As Paymentstatus,
              To_Number(decode(Qsp.Maincontractcertno, null, 0, Qsp.Maincontractcertno)) As Maincertno,
              Decode(Sign(To_Number(To_Char(Qsp.Duedate,''yyyyddd''))-2000000), -1, To_Number(Substr(To_Char(Qsp.Duedate,''yyyyddd''), 3)), To_Number(Concat(''1'', (Substr(To_Char(Qsp.Duedate,''yyyyddd''), 3))))) As Duedate,
              Decode(sign(To_Number(To_Char(Qsp.Certissuedate,''yyyyddd''))-2000000), -1, To_Number(SUBSTR(To_Char(Qsp.Certissuedate,''yyyyddd''), 3)), to_number(concat(''1'', (SUBSTR(To_Char(Qsp.Certissuedate,''yyyyddd''), 3))))) as Certissuedate,
              Round(To_Number(Decode(Qsp.Certamount, Null, 0, Qsp.Certamount*100)), 0) As Certamount,
              Decode(qsp.paymenttype, Null, '' '', Qsp.paymenttype) As paymenttype,
              Round(To_Number(Decode(qsp.remeasuredscsum, Null, 0, Qsp.remeasuredscsum*100)), 0) As remeasuredscsum,
              Round(To_Number(Decode(qsp.addendumamount, Null, 0, Qsp.addendumamount*100)), 0) As addendumamount
      FROM PCMSDATAPROD.payment_Cert Qsp
      Where Not Exists (Select 1
                        From PRODDTA.F58011 Jde
                        Where  jde.Pcmcu = Lpad(To_Char(Qsp.Jobno),12, '' '')
                        And Jde.Pcdc07 = Qsp.Packageno
                        And Jde.Pcalgn = Qsp.Paymentcertno)
      And Qsp.System_Status = ''ACTIVE''
      And qsp.paymentstatus = ''APR''';

Begin
  Open C_Qs_Scpayment_Cert For V_Sql;

	SELECT to_number(to_char(sysdate,'yyddd')) + 100000 INTO v_upmj FROM dual;
	Select To_Number(To_Char(Sysdate,'hh24miss')) Into V_Upmt From Dual;
	Dbms_Output.Enable(10000000);

		Loop
			Fetch C_Qs_Scpayment_Cert
			Into  V_Mcu,
            V_Dc07,
            V_Algn,
            V_Prst,
            V_Urab,
            V_Urdt,
            V_Apdj,
            V_Apd,
            V_Pipf,
            V_Cuma,
            v_Aaa;

			Exit When C_Qs_Scpayment_Cert%Notfound;
			DBMS_OUTPUT.PUT_LINE('Insert Job:' || V_Mcu || ' ' || V_Dc07  || ' ' || V_Algn);
			Begin
            Insert Into PRODDTA.F58011(Pcmcu, Pcdc07, Pcalgn, Pcprst, Pcpipf, Pcdoc, Pcdctv, Pcapd, Pccuma, Pcaaa, Pciicu, Pcxicu, Pcldoc, Pcxdct,             Pcadtj, Pcapdj, Pcev01, Pcev02, Pcev03, Pcurab, Pcurat,   Pcurcd, Pcurdt, Pcurog, Pcurrc, 			      Pcurrf, 		        Pctorg,		  Pcuser,	 	   Pcpid,		    Pcjobn,	    Pcupmj, Pcupmt)
                                       Values(V_Mcu, V_Dc07, V_Algn, V_Prst, V_Pipf, 0, 	  'PS',   V_Apd, V_Cuma, V_Aaa, 0,		  0,		  0,	    Lpad(' ', 2, ' '),  0,		  V_Apdj, ' ', 	  ' ', 	  ' ', 	  V_Urab, 0, 		    0,      V_Urdt, 0,		  Lpad(' ', 2, ' '),	Lpad(' ', 4, ' '),  'QSSPROC03',  'QSSPROC03',	'QS58011', 'ERPWLS02', v_upmj, V_Upmt);

			EXCEPTION
				When Others Then
					DBMS_OUTPUT.PUT_LINE('Error ' || v_mcu || ' ' || v_dc07 || ' ' || V_Algn);
					CONTINUE;
			END;
		End Loop;
    CLOSE C_Qs_Scpayment_Cert;

	DBMS_OUTPUT.PUT_LINE('Duration:  ' || ((To_Number(To_Char(Sysdate,'hh24miss'))) - V_Upmt));
	COMMIT;
	<<Exit_Here>>
  Null;
END P_QS_UPDATE_F58011;


Grant Execute On PCMSDATAPROD.P_QS_UPDATE_F58011 To PCMSDATAPROD;
grant EXECUTE on PCMSDATAPROD.P_QS_UPDATE_F58011 to PCMSUSER_ROLE;
Grant select, Insert On PRODDTA.F58011 To PCMSDATAPROD;
Grant select, Insert On PRODDTA.F58011 To PCMSUSER_ROLE;