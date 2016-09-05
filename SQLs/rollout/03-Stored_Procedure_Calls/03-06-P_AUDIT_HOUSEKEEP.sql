	/**
	 * Function 
	 */
	create or replace FUNCTION PCMSDATAUAT.unix_ts_to_date( p_unix_ts IN NUMBER ) RETURN DATE AS
	  l_date DATE;
	BEGIN
	  l_date := date '1970-01-01' + p_unix_ts/60/60/24;
	  RETURN l_date;
	END;
	
	/**
	 * Procedure
	 */
	create or replace procedure PCMSDATAUAT.p_audit_Housekeep(tableName in varchar2, period in int, rcount out number) AS
		stmt varchar2(1000);
		l_date  DATE := SYSDATE - period;
	Begin
	  stmt := 'delete from '||tableName||' where REVTYPE != 2 and REV in (select r.id from pcmsdataUAT.revision r join '||tableName||' t on r.id = t.rev and unix_ts_to_date(r.timestamp/1000) < to_date('''|| l_date ||'''))' ;
	  dbms_output.put_line(stmt);
	  execute IMMEDIATE stmt;
	  rcount := SQL%ROWCOUNT;
	  dbms_output.put_line('SQL%ROWCOUNT:'||rcount);
	End;
	
grant EXECUTE on "PCMSDATAUAT"."P_AUDIT_HOUSEKEEP" to "PCMSUSER_ROLE";
grant EXECUTE on "PCMSDATAUAT"."UNIX_TS_TO_DATE" to "PCMSUSER_ROLE" ;