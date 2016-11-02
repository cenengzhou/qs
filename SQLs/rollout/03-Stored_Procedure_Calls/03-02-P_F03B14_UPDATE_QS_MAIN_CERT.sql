grant select On CRPDTA.F03b11 to PCMSDATAUAT;
grant select On CRPDTA.F03b14 to PCMSDATAUAT;

create or replace PROCEDURE PCMSDATAUAT.P_F03B14_UPDATE_QS_MAIN_CERT As
  Type Qscurtype Is Ref Cursor;
  Counter Number;
  C_Qs_Main_Cert Qscurtype;

  V_Jobno Varchar2(5);
  V_Certno Number;
  V_Docno Varchar2(10);
  V_Actualreceiptdate Date;
  v_TotalReceiptAmount DOUBLE PRECISION;
  Start_Time	Number;


   V_Sql Varchar2(2000) :=
  '	Select Datalist.* From
          (Select  Receiptdetail.Jobno,
                        To_Number(Trim(Leading ''0'' From Substr(header.Rpvr01, Length(Trim(header.Rpmcu))+1))) As Certno,
                        Receiptdetail.Ardocumentno,
                        Receiptdetail.Actual_Receipt_Date,
                        decode(Receiptdetail.Total_Receipt_Amount, NULL, 0.00, (Receiptdetail.Total_Receipt_Amount/100))
               From
                     (Select Detail.*,
                            (Select Sum(Rzpaap) From CRPDTA.F03b14 Where Trim(Rzmcu)= JobNo and Rzdoc = Detail.Ardocumentno) As total_receipt_amount
                       From
                            (Select
                                   Trim(Rzmcu) As Jobno,
                                   Rzdoc As Ardocumentno,
                                   Max(To_Date(Decode(Sign(Length(Rzdmtj)-6), -1, Concat(''19'', Rzdmtj), Concat(''20'', Substr(Rzdmtj, 2))), ''yyyyddd''))As Actual_Receipt_Date
                            From  CRPDTA.F03b14
                            Group By Trim(Rzmcu), Rzdoc
                            ) Detail
                      )Receiptdetail
                Inner Join CRPDTA.F03b11 header On Trim(header.Rpmcu) = Receiptdetail.Jobno and header.Rpdoc = Receiptdetail.Ardocumentno
                And  Length(Trim(header.Rpmcu))=5 And  Length(Trim(header.Rpvr01))>7 And  Regexp_Like(Trim(header.Rpvr01),''^[[:digit:]]+$'')
          Minus
                Select
                      Jobno,
                      Certno,
                      Ardocumentno,
                      Actual_Receipt_Date,
                      Total_Receipt_Amount
                From PCMSDATAUAT.Qs_Main_Contract_Certificate) Datalist, PCMSDATAUAT.Qs_Main_Contract_Certificate Maincertificate
    Where Datalist.Jobno = Maincertificate.Jobno
    and Datalist.certno = Maincertificate.certno
  ';




BEGIN
  Open C_Qs_Main_Cert For V_Sql;

	Select To_Number(To_Char(Sysdate,'hh24miss')) Into Start_time From Dual;
  Dbms_Output.Enable(10000000);
  counter:=0;
		Loop
			Fetch C_Qs_Main_Cert
			Into  V_Jobno,
            V_Certno,
            V_Docno,
            V_Actualreceiptdate,
            v_TotalReceiptAmount
            ;
			Exit When C_Qs_Main_Cert%Notfound;
			Begin
            Update PCMSDATAUAT.Main_Cert
            Set
            Ardocumentno = V_Docno,
            Actual_Receipt_Date = V_Actualreceiptdate,
            Total_Receipt_Amount = v_TotalReceiptAmount

            Where Jobno = V_Jobno And Certno = V_Certno;

            DBMS_OUTPUT.PUT_LINE('Update:' || V_Jobno || ' ' || V_Certno  || ' ' || V_Docno || ' ' || V_Actualreceiptdate);
            counter:=counter+1;
			EXCEPTION
          When Others Then
					DBMS_OUTPUT.PUT_LINE('Error ' || V_Jobno || ' ' || V_Certno  || ' ' || V_Docno || ' ' || V_Actualreceiptdate);
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
/

grant EXECUTE on PCMSDATAUAT.P_F03B14_UPDATE_QS_MAIN_CERT to PCMSUSER_ROLE;
/