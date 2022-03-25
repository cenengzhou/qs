grant execute on PCMSDATAPROD.P_UPDATE_CED_APPROVAL_TO_QS to PCMSUSER_ROLE;

create or replace PROCEDURE PCMSDATAPROD.P_UPDATE_CED_APPROVAL_TO_QS  (P_JobNo Varchar2, P_PackageNo VARCHAR2) AS 
  Type Qscurtype Is Ref Cursor;
  Counter Number;
  C_Qs_AP Qscurtype;


  V_Job_id Number;
  V_Jobno Varchar2(5);
  V_PackageNo VARCHAR2(10);
  V_CEDApprovalAmount DOUBLE PRECISION;
  Start_Time Number;

   
  /* SQL Statement */
	V_Job_Sql  VARCHAR2(1000) := '';		-- select sql for specific job, package
	V_Sql      VARCHAR2(1000) := '';       -- select sql for all job
    
  

BEGIN

  IF P_JobNo IS NULL THEN
  
      V_Sql := 
      '	select job_id, Job_No, Order_SC_no,  round(sum(request_Approval_amount), 2) as approval_amount
        from
        (select  distinct approval_route.*, job_info.id as job_id from QSADMIN.ap_approval_route approval_route
        join QSADMIN.ap_approval_type_master type_master on type_master.id = approval_route.approval_type_id
        join QSADMIN.ap_approval approval on approval.approval_route_id = approval_route.id 
        join PCMSDATAPROD.job_info job_info on job_info.jobno = approval_route.job_no
        where approval_route.approval_status = ''APPROVED'' and type_master.Name in (''SL'', ''SM'', ''AW'', ''ST'', ''V5'', ''V6'')
        and approval.approver_id in (24592, 1035) and approval.approval_status = ''APPROVED'') b
        group by Job_No, Order_SC_no, job_id
      ';
  
        Open C_Qs_AP For V_Sql;
  ELSE
  
      V_Job_Sql :=
      '	select job_id, Job_No, Order_SC_no,  round(sum(request_Approval_amount), 2) as approval_amount
        from
        (select  distinct approval_route.*, job_info.id as job_id from QSADMIN.ap_approval_route approval_route
        join QSADMIN.ap_approval_type_master type_master on type_master.id = approval_route.approval_type_id
        join QSADMIN.ap_approval approval on approval.approval_route_id = approval_route.id 
        join PCMSDATAPROD.job_info job_info on job_info.jobno = approval_route.job_no
        where approval_route.approval_status = ''APPROVED'' and type_master.Name in (''SL'', ''SM'', ''AW'', ''ST'', ''V5'', ''V6'')
        and approval.approver_id in (24592, 1035) and approval.approval_status = ''APPROVED''
        and job_info.jobno = :1 and Order_SC_no = :2) b
        group by Job_No, Order_SC_no, job_id
      ';
  
        Open C_Qs_AP For V_Job_Sql Using P_JobNo, P_PackageNo;
  END IF;


  Select To_Number(To_Char(Sysdate,'hh24miss')) Into Start_time From Dual;
  Dbms_Output.Enable(10000000);
  counter:=0;
		Loop
			Fetch C_Qs_AP
			Into  
            V_Job_id,
            V_Jobno,
            V_PackageNo,
            V_CEDApprovalAmount
            ;
			Exit When C_Qs_AP%Notfound;
			Begin
            Update PCMSDATAPROD.subcontract
            Set
            CED_APPROVED_AMT = V_CEDApprovalAmount

            Where JOB_INFO_ID = V_Job_id And PACKAGENO = V_PackageNo;

            --DBMS_OUTPUT.PUT_LINE('Update:' || V_Jobno || ' ' || V_PackageNo  || ' '  || ' ' || V_CEDApprovalAmount);
            counter:=counter+1;
			EXCEPTION
          When Others Then
					DBMS_OUTPUT.PUT_LINE('Error ' || V_Jobno || ' ' || V_PackageNo  || ' '  || ' ' || V_CEDApprovalAmount);
					CONTINUE;
			END;
		End Loop;
    Close C_Qs_AP;
  DBMS_OUTPUT.PUT_LINE('Number of records updated:  ' || counter);
  DBMS_OUTPUT.PUT_LINE('Duration:  ' || ((To_Number(To_Char(Sysdate,'hh24miss'))) - Start_time));
	COMMIT;
	<<Exit_Here>>
  Null;
  
END P_UPDATE_CED_APPROVAL_TO_QS;