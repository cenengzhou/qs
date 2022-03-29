
  CREATE OR REPLACE FORCE VIEW "PCMSDATAPROD"."V_CED_APPROVAL" ("ID", "JOB_NO", "PACKAGE_NO", "APPROVAL_AMOUNT") AS 
  select rownum as id, c."JOB_NO",c."ORDER_SC_NO",c."APPROVAL_AMOUNT" from 
(select Job_No, Order_SC_no,  round(sum(request_Approval_amount), 2) as approval_amount
from
(select  distinct approval_route.* from QSADMIN.ap_approval_route approval_route
join QSADMIN.ap_approval_type_master type_master on type_master.id = approval_route.approval_type_id
join QSADMIN.ap_approval approval on approval.approval_route_id = approval_route.id 
where approval_route.approval_status = 'APPROVED' and type_master.Name in ('SL', 'SM', 'AW', 'ST', 'V5', 'V6')
and approval.approver_id in (24592, 1035, 16450) and approval.approval_status = 'APPROVED') b
group by Job_No, Order_SC_no) c;


grant select on QSADMIN.ap_approval_route to PCMSDATAPROD with grant option;
grant select on QSADMIN.ap_approval_type_master to PCMSDATAPROD with grant option;
grant select on QSADMIN.ap_approval to PCMSDATAPROD with grant option;

grant select on PCMSDATAPROD.V_CED_APPROVAL to PCMSUSER_ROLE;
grant select on PCMSDATAPROD.V_CED_APPROVAL to IMS_SUPPORT_ROLE_QS;
