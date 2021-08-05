-- 1. Add Type Recoverable

alter table ADDENDUM_DETAIL
    add TYPE_RECOVERABLE VARCHAR2(2 char)
/
alter table ADDENDUM_DETAIL_AUDIT
    add TYPE_RECOVERABLE VARCHAR2(2 char)
/
alter table SUBCONTRACT_DETAIL
    add TYPE_RECOVERABLE VARCHAR2(2 char)
/
alter table SUBCONTRACT_DETAIL_AUDIT
    add TYPE_RECOVERABLE varchar2(2 char)
/

-- 2. Add View to show addendum detail before update
create view V_ADDENDUM_DETAIL_DIFF as
select a.*,
       LAG(TYPE_RECOVERABLE, 1, null) over (partition by NO_JOB, NO_SUBCONTRACT, BPI order by NO)
           as PREV_TYPE_RECOVERABLE,
        LAG(AMT_ADDENDUM, 1, 0) over (partition by NO_JOB, NO_SUBCONTRACT, BPI order by NO)
           as PREV_AMT_ADDENDUM,
        case TYPE_ACTION
            when 'DELETE' then -AMT_ADDENDUM
            when 'UPDATE' then
                    AMT_ADDENDUM - LAG(AMT_ADDENDUM, 1, 0) over (partition by NO_JOB, NO_SUBCONTRACT, BPI order by NO)
            else AMT_ADDENDUM end
            as AMT_ADDENDUM_DIFF
from ADDENDUM_DETAIL a;

grant select, insert, update, delete on PCMSDATADEV.V_ADDENDUM_DETAIL_DIFF to PCMSUSER_ROLE;

-- 3. Add view to calculate recoverable amount in recoverable, non-recoverable and unclassified amount
create view V_ADDENDUM_DETAIL_RECOVERABLE as
select a.*,
       (case
            when TYPE_ACTION='ADD' and TYPE_RECOVERABLE='R' then AMT_ADDENDUM
            when TYPE_ACTION='DELETE' and TYPE_RECOVERABLE='R' then -AMT_ADDENDUM
            when TYPE_ACTION='UPDATE' and PREV_TYPE_RECOVERABLE='R' and TYPE_RECOVERABLE='R' then (AMT_ADDENDUM-PREV_AMT_ADDENDUM)
            when TYPE_ACTION='UPDATE' and PREV_TYPE_RECOVERABLE='R' and (TYPE_RECOVERABLE<>'R' or TYPE_RECOVERABLE is null) then -PREV_AMT_ADDENDUM
            when TYPE_ACTION='UPDATE' and (PREV_TYPE_RECOVERABLE<>'R' or PREV_TYPE_RECOVERABLE is null) and TYPE_RECOVERABLE='R' then AMT_ADDENDUM
            else 0
           end) as RECOVERABLE_AMT,
       (case
            when TYPE_ACTION='ADD' and TYPE_RECOVERABLE='NR' then AMT_ADDENDUM
            when TYPE_ACTION='DELETE' and TYPE_RECOVERABLE='NR' then -AMT_ADDENDUM
            when TYPE_ACTION='UPDATE' and PREV_TYPE_RECOVERABLE='NR' and TYPE_RECOVERABLE='NR' then (AMT_ADDENDUM-PREV_AMT_ADDENDUM)
            when TYPE_ACTION='UPDATE' and PREV_TYPE_RECOVERABLE='NR' and (TYPE_RECOVERABLE<>'NR' or TYPE_RECOVERABLE is null) then -PREV_AMT_ADDENDUM
            when TYPE_ACTION='UPDATE' and (PREV_TYPE_RECOVERABLE<>'NR' or PREV_TYPE_RECOVERABLE is null) and TYPE_RECOVERABLE='NR' then AMT_ADDENDUM
            else 0
           end) as NON_RECOVERABLE_AMT,
       (case
            when TYPE_ACTION='ADD' and TYPE_RECOVERABLE is null then AMT_ADDENDUM
            when TYPE_ACTION='DELETE' and TYPE_RECOVERABLE is null then -AMT_ADDENDUM
            when TYPE_ACTION='UPDATE' and PREV_TYPE_RECOVERABLE is null and TYPE_RECOVERABLE is null then (AMT_ADDENDUM-PREV_AMT_ADDENDUM)
            when TYPE_ACTION='UPDATE' and PREV_TYPE_RECOVERABLE is null and TYPE_RECOVERABLE is not null then -PREV_AMT_ADDENDUM
            when TYPE_ACTION='UPDATE' and PREV_TYPE_RECOVERABLE is not null and TYPE_RECOVERABLE is null then AMT_ADDENDUM
            else 0
           end) as UNCLASSIFIED_AMT
from V_ADDENDUM_DETAIL_DIFF a;

grant select, insert, update, delete on PCMSDATADEV.V_ADDENDUM_DETAIL_RECOVERABLE to PCMSUSER_ROLE;
