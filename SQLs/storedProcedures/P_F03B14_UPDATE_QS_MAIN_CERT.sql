create or replace
Procedure P_F03b14_Update_Qs_Main_Cert As 
  Type Qscurtype Is Ref Cursor; 
  Counter Number;
  C_Qs_Main_Cert Qscurtype;
  
  Job_No Char(5);
  Cert_No Number;
  Receipt_Date Date;
  Start_time	Number;	

  V_Sql Varchar2(2000) := 
  ' Select
          Trim(Rzmcu) As Jobno,
          To_Number(Trim(Leading ''0'' From Substr(Rzvr01, Length(Trim(Rzmcu))+1))) As Certno,
          To_Date(Decode(Sign(Length(Rzdmtj)-6), -1, Concat(''19'', Rzdmtj), Concat(''20'', Substr(Rzdmtj, 2))), ''yyyyddd'')As Receiptdate
          From testdta.F03b14 Where Length(Trim(Rzmcu))=5 and  Length(Trim(Rzvr01))>7 And  Regexp_Like(Trim(Rzvr01),''^[[:digit:]]+$'')
    Minus
          Select 
          Jobno,
          Certno,
          Actual_Receipt_Date
          from QSDATADEV.qs_main_contract_certificate
    ';

BEGIN
  Open C_Qs_Main_Cert For V_Sql;
  
	Select To_Number(To_Char(Sysdate,'hh24miss')) Into Start_time From Dual;
  Dbms_Output.Enable(10000000);
  counter:=0;
		Loop
			Fetch C_Qs_Main_Cert
			Into  Job_No,
            Cert_No,
            receipt_date
            ;
			Exit When C_Qs_Main_Cert%Notfound;
			Begin	
            Update QSDATADEV.Qs_Main_Contract_Certificate
            Set
            Actual_Receipt_Date = receipt_date
            Where Jobno = Job_No And Certno = Cert_No;
           
            DBMS_OUTPUT.PUT_LINE('Update:' || Job_No || ' ' || Cert_No  || ' ' || receipt_date);
            counter:=counter+1;
			EXCEPTION
          When Others Then
					DBMS_OUTPUT.PUT_LINE('Error ' || Job_No || ' ' || Cert_No || ' ' || receipt_date);
					CONTINUE;
			END;
		End Loop;
    Close C_Qs_Main_Cert;
  DBMS_OUTPUT.PUT_LINE('Number of records updated:  ' || counter);
  DBMS_OUTPUT.PUT_LINE('Duration:  ' || ((To_Number(To_Char(Sysdate,'hh24miss'))) - Start_time));
	COMMIT;
	<<Exit_Here>>
  Null;    
END P_F03B14_UPDATE_QS_MAIN_CERT;