Select 
      ar.Division As Division,  
      ar.Company As Company, 
      ar.Jobno As Jobno, 
      ar.Description As Jobdescription,
      ar.employer as ClientNo,
      ar.Maxcert As Certno, 
      mc.clientCertNo as clientCertNo,
      ar.billingcurrency as Currency,
      decode(pc.Certno, null, To_Char((Mc.Certmaincontractoramt + Mc.Certnscndscamt + Mc.Certmosamt - Mc.Certmaincontractorret + Mc.Certmaincontractorretrel - Mc.Certretfornscndsc + Mc.Certretrelfornscndsc - Mc.Certmosret + Mc.Certmosretrel - Mc.Certccamt + Mc.Certadjustmentamt + Mc.Certadvancepayment + Mc.Certcpfamt), 'fm999,999,999,990.90'),
      To_Char(((Mc.Certmaincontractoramt + Mc.Certnscndscamt + Mc.Certmosamt - Mc.Certmaincontractorret + Mc.Certmaincontractorretrel - Mc.Certretfornscndsc + Mc.Certretrelfornscndsc - Mc.Certmosret + Mc.Certmosretrel - Mc.Certccamt + Mc.Certadjustmentamt + Mc.Certadvancepayment + Mc.Certcpfamt)
      -(pc.Certmaincontractoramt + pc.Certnscndscamt + pc.Certmosamt - pc.Certmaincontractorret + pc.Certmaincontractorretrel - pc.Certretfornscndsc + pc.Certretrelfornscndsc - pc.Certmosret + pc.Certmosretrel - pc.Certccamt + pc.Certadjustmentamt + pc.Certadvancepayment + pc.Certcpfamt)), 'fm999,999,999,990.90')
      )As ReceivableAmount, 
      mc.certificatestatus as CertStatus, 
      decode(mc.cert_as_at_date, Null, '-', to_char(mc.cert_as_at_date, 'DD/MM/YYYY')) as CertAsAtDate, 
      decode(mc.Ipa_Submission_Date, Null, '-',  To_Char(mc.Ipa_Submission_Date, 'DD/MM/YYYY')) As Ipadate, 
      decode(mc.cert_issue_date, Null, '-', to_char(mc.cert_issue_date, 'DD/MM/YYYY')) as CertIssueDate, 
      decode(mc.certduedate, Null, '-', to_char(mc.certduedate, 'DD/MM/YYYY')) as CertDueDate, 
      Decode(Mc.Actual_Receipt_Date, Null, '-', To_Char(Mc.Actual_Receipt_Date, 'DD/MM/YYYY')) As Actualreceiptdate,
      decode(mc.total_receipt_amount, NULL, '0.00', to_char(mc.total_receipt_amount, 'fm999,999,999,990.90')) as totalReceiptAmount,
      decode(ar.Benchmark1, Null, '-', ceil(ar.Benchmark1)) As Benchmark1,  
      decode(ar.Benchmark2, Null, '-', ceil(ar.Benchmark2)) As Benchmark2
  From 
  (Select 
      mainCert.Jobno, 
      mainCert.Maxcert, 
      (Select Max(Certno) From  Qsdatadev.Qs_Main_Contract_Certificate  Where Jobno = mainCert.Jobno and CertNo != mainCert.Maxcert) Prevcert,
      Avg(mainCert.Cert_Issue_Date - mainCert.Cert_As_At_Date) As Benchmark1, 
      Avg(mainCert.Certduedate - mainCert.Cert_Issue_Date) As Benchmark2,
      jb.company, 
      jb.Division, 
      jb.Description,
      jb.Employer,
      jb.Billingcurrency
     From
      (SELECT baseCert.*,
              (Select Max(Certno) From  Qsdatadev.Qs_Main_Contract_Certificate  Where Jobno = baseCert.Jobno) Maxcert,
              Row_Number() Over(Partition By baseCert.Jobno Order By baseCert.Certno Desc) As rownumber
        From 
            (Select cert.* 
                  From Qsdatadev.Qs_Main_Contract_Certificate cert Where cert.Certificatestatus >= 300) baseCert) mainCert,
  qsdatadev.qs_job jb
  Where mainCert.rownumber <= 6
  And mainCert.Jobno = jb.Jobno 
GROUP BY mainCert.Jobno, mainCert.Maxcert, jb.company, jb.Division, jb.Description, jb.Employer, jb.billingcurrency) ar 
inner join Qsdatadev.Qs_Main_Contract_Certificate mc on ar.Jobno = Mc.Jobno And ar.Maxcert = Mc.Certno
Left Join Qsdatadev.Qs_Main_Contract_Certificate Pc On Ar.Jobno = Pc.Jobno And Ar.Prevcert = Pc.Certno
order by ar.jobno;