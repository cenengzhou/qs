--select * from QSDATAUAT.qs_retentionrel where 
--(RELEASEPERCENT = -binary_double_infinity or RELEASEPERCENT = binary_double_infinity)
--order by JOBNO;

-- 5 records are found
select * from QSDATAUAT.QS_RETENTIONREL where
JOBNO in ('13259', '13268', '13421') and
(RELEASEPERCENT = -binary_double_infinity or RELEASEPERCENT = binary_double_infinity);
-- 1 column of 5 records are updated
update QSDATAUAT.QS_RETENTIONREL set RELEASEPERCENT = 100 where
JOBNO in ('13259', '13268', '13421') and
(RELEASEPERCENT = -binary_double_infinity or RELEASEPERCENT = binary_double_infinity);