package com.gammon.qs.shared;

import java.util.Map;

import com.gammon.qs.domain.MainCert;
import com.gammon.qs.domain.Subcontract;
import com.gammon.qs.domain.AppSubcontractStandardTerms;


public class GlobalParameter {
	
	
	//UI config
	public static final String NULL_REPLACEMENT = "N/A";
	public static final String SEARCHING_MSG = "Searching...";
	public static final String LOADING_MSG = "Loading...";
	public static final String POSTING_MSG = "Posting...";
	public static final String SAVING_MSG = "Saving...";
	public static final String CALCULATING_MSG ="Calculating...";
	public static final String RECALCULATING_MSG = "Recalculating...";
	public static final String RESULT_OVERFLOW_MESSAGE = "The search result is more than 100 rows, please specifiy more searching criteria.";
	public static final String RESULT_OVERFLOW_800ROWS_MESSAGE = "The search result is more than 800 rows, please specifiy more searching criteria.";
	public static final String APPROVAL_LIST_OVERFLOW_800ROWS_MESSAGE = "Approval list is more than 800 rows, please specifiy subcontract number";
	
	//Parameter
	public static final String MAIN_SECTION_PANEL_ID = "main-section-panel";
	public static final String TREEVIEW_SECTION_PANEL_ID = "treeview-section-panel";
	public static final String DETAIL_SECTION_PANEL_ID ="detail-section-panel";
	public static final String MAIN_PANEL_ID = "main-panel";
	public static final String MASTER_LIST_TAB_PANEL_ID= "masterListTabPanel";
	
	//Setting	
	public final static String DATE_FORMAT = "dd/MM/yyyy";
    public final static String DATETIME_FORMAT = "dd/MM/yyyy hh:mm";
    /**
     *  for UI 
     */
    public final static String DATEFIELD_DATEFORMAT = "d/m/Y";
	public static final String DATEFIELD_ALTDATEFORMAT = "dmY";
    
	//COLOR
	public static final String RED_COLOR = "#FF0000";
	public static final String ORANGE_COLOR = "#e68550";
	public static final String GREEN_COLOR = "#007D00";
	public static final String GREY_COLOR = "#707070";
	
	
    // ROLE
    public final static String ROLE_READ = "ROLE_READ";
    public final static String ROLE_WRITE = "ROLE_WRITE";
    
    //PAGINATION
    public final static int PAGE_SIZE = 100;
    
    //RETRY INTERVERAL
    public final static long RETRY_INTERVERAL = 30000;
    
    //Error Message of Validate Cost Type and Cost Code
    public final static String ERROR_OBJ_SUB = "Invalid Combination of Cost Code and Cost Type";
	
    //Transit Import Types
    public final static String TRANSIT_BQ = "BQ";
    public final static String TRANSIT_RESOURCE = "Resource";
    public final static String TRANSIT_CODE_MATCHING = "Resource Code Matching";
    public final static String TRANSIT_UOM_MATCHING = "Unit Code Matching";
    public final static String TRANSIT_ERROR = "TERROR";
    public final static String TRANSIT_SUCCESS_WITH_WARNING = "SUCCESS_WITH_WARNING"; // added by brian on 20110225

	public static String[][] getContraChargeLineType(boolean withDefault){
		if(withDefault)
			return new String[][]{
					new String[]{"","All"},
					new String[]{"C1","C1"},
					new String[]{"C2","C2"},
		};
		else
		return new String[][]{
				new String[]{"C1","C1"},
				new String[]{"C2","C2"},
		};
	}

	
	public static String[][] getPaymentTerms(boolean withDefault){
		if(withDefault)
			return new String[][]{
					new String[]{"",AppSubcontractStandardTerms.CONST_ALL},
					new String[]{"QS0", "QS0 - Manual Input Due Date"},
					new String[]{"QS1", "QS1 - Pay when Paid + 7 days"},
					new String[]{"QS2", "QS2 - Pay when Paid + 14 days"},
					new String[]{"QS3", "QS3 - Pay when IPA Received + 56 days"},
					new String[]{"QS4", "QS4 - Pay when Invoice Received + 28 days"},
					new String[]{"QS5", "QS5 - Pay when Invoice Received + 30 days"},
					new String[]{"QS6", "QS6 - Pay when Invoice Received + 45 days"},
					new String[]{"QS7", "QS7 - Pay when Invoice Received + 60 days"}
		};	
		else
		return new String[][]{
				new String[]{"QS0", "QS0 - Manual Input Due Date"},
				new String[]{"QS1", "QS1 - Pay when Paid + 7 days"},
				new String[]{"QS2", "QS2 - Pay when Paid + 14 days"},
				new String[]{"QS3", "QS3 - Pay when IPA Received + 56 days"},
				new String[]{"QS4", "QS4 - Pay when Invoice Received + 28 days"},
				new String[]{"QS5", "QS5 - Pay when Invoice Received + 30 days"},
				new String[]{"QS6", "QS6 - Pay when Invoice Received + 45 days"},
				new String[]{"QS7", "QS7 - Pay when Invoice Received + 60 days"}
		};
	}
	
	public static String[][] getRetentionType(boolean withDefault){
		if(withDefault)
			return new String[][]{
					new String[]{"",AppSubcontractStandardTerms.CONST_ALL},
					new String[]{Subcontract.RETENTION_LUMPSUM,Subcontract.RETENTION_LUMPSUM},
					new String[]{Subcontract.RETENTION_ORIGINAL,Subcontract.RETENTION_ORIGINAL},
					new String[]{Subcontract.RETENTION_REVISED,Subcontract.RETENTION_REVISED}
		};
		else
		return new String[][]{
				new String[]{Subcontract.RETENTION_LUMPSUM,Subcontract.RETENTION_LUMPSUM},
				new String[]{Subcontract.RETENTION_ORIGINAL,Subcontract.RETENTION_ORIGINAL},
				new String[]{Subcontract.RETENTION_REVISED,Subcontract.RETENTION_REVISED}
		};
	}

	public static String[][] getReviewedByFinance(boolean withDefault){
		if(withDefault)
			return new String[][]{
					new String[]{"",AppSubcontractStandardTerms.CONST_ALL},
					new String[]{AppSubcontractStandardTerms.FINQS0REVIEW_Y,AppSubcontractStandardTerms.FINQS0REVIEW_Y},
					new String[]{AppSubcontractStandardTerms.FINQS0REVIEW_N,AppSubcontractStandardTerms.FINQS0REVIEW_N}
		};
		else
		return new String[][]{
				new String[]{AppSubcontractStandardTerms.FINQS0REVIEW_Y,AppSubcontractStandardTerms.FINQS0REVIEW_Y},
				new String[]{AppSubcontractStandardTerms.FINQS0REVIEW_N,AppSubcontractStandardTerms.FINQS0REVIEW_N}
		};
	}

	/*
	public final static String[][] getTerms(){
		return new String[][]{
				new String[]{"QS0", "QS0 - Manual Input Due Date"},
				new String[]{"QS1", "QS1 - Pay when Paid + 7 days"},
				new String[]{"QS2", "QS2 - Pay when Paid + 14 days"},
				new String[]{"QS3", "QS3 - Pay when IPA Received + 56 days"},
				new String[]{"QS4", "QS4 - Pay when Invoice Received + 28 days"},
				new String[]{"QS5", "QS5 - Pay when Invoice Received + 30 days"},
				new String[]{"QS6", "QS6 - Pay when Invoice Received + 45 days"},
				new String[]{"QS7", "QS7 - Pay when Invoice Received + 60 days"}
		};
	}
	*/

	
	private static Map<String, String> performanceGroupMap;

	public static Map<String, String> getPerformanceGroupMap() {
		return performanceGroupMap;
	}

	public static void setPerformanceGroupMap(Map<String, String> performanceGroupMapArgument) {
		performanceGroupMap = performanceGroupMapArgument;
	}
	
	private static Map<String, String> scStatusCodeMap;

	public static Map<String, String> getScStatusCodeMap() {
		return scStatusCodeMap;
	}

	public static void setScStatusCodeMap(Map<String, String> scStatusCodeMap) {
		GlobalParameter.scStatusCodeMap = scStatusCodeMap;
	}
	
	
	public final static String[][] getMainCertficateStatuses(){
		return new String[][]{
				new String[]{MainCert.CERT_CREATED, 					MainCert.CERT_CREATED_DESC},
				new String[]{MainCert.IPA_SENT, 						MainCert.IPA_SENT_DESC},
				new String[]{MainCert.CERT_CONFIRMED, 				MainCert.CERT_CONFIRMED_DESC},
				new String[]{MainCert.CERT_WAITING_FOR_APPROVAL, 	MainCert.CERT_WAITING_FOR_APPROVAL_DESC},
				new String[]{MainCert.CERT_POSTED, 					MainCert.CERT_POSTED_DESC}
		};
	}
	
	public final static String[][] getMainCertficateStatusesFull(boolean withOptions){
		if(withOptions){
			return new String[][]{
					new String[]{"", 													"All"},
					new String[]{MainCert.CERT_CREATED, 					MainCert.CERT_CREATED_DESC},
					new String[]{MainCert.IPA_SENT, 						MainCert.IPA_SENT_DESC},
					new String[]{MainCert.CERT_CONFIRMED, 				MainCert.CERT_CONFIRMED_DESC},
					new String[]{MainCert.CERT_WAITING_FOR_APPROVAL, 	MainCert.CERT_WAITING_FOR_APPROVAL_DESC},
					new String[]{MainCert.CERT_POSTED, 					MainCert.CERT_POSTED_DESC},
					new String[]{"400", 												"Payment Received"}
			};
		}
		else
			return new String[][]{
					new String[]{MainCert.CERT_CREATED, 					MainCert.CERT_CREATED_DESC},
					new String[]{MainCert.IPA_SENT, 						MainCert.IPA_SENT_DESC},
					new String[]{MainCert.CERT_CONFIRMED, 				MainCert.CERT_CONFIRMED_DESC},
					new String[]{MainCert.CERT_WAITING_FOR_APPROVAL, 	MainCert.CERT_WAITING_FOR_APPROVAL_DESC},
					new String[]{MainCert.CERT_POSTED, 					MainCert.CERT_POSTED_DESC},
					new String[]{"400", 												"Payment Received"}
			};
	}
	
	 /*
     * GWT-EXT Date format (i.e. DateField of Panels, Windows etc)
     * 
    Format  Description                                                               Example returned values
    ------  -----------------------------------------------------------------------   -----------------------
      d     Day of the month, 2 digits with leading zeros                             01 to 31
      D     A short textual representation of the day of the week                     Mon to Sun
      j     Day of the month without leading zeros                                    1 to 31
      l     A full textual representation of the day of the week                      Sunday to Saturday
      N     ISO-8601 numeric representation of the day of the week                    1 (for Monday) through 7 (for Sunday)
      S     English ordinal suffix for the day of the month, 2 characters             st, nd, rd or th. Works well with j
      w     Numeric representation of the day of the week                             0 (for Sunday) to 6 (for Saturday)
      z     The day of the year (starting from 0)                                     0 to 364 (365 in leap years)
      W     ISO-8601 week number of year, weeks starting on Monday                    01 to 53
      F     A full textual representation of a month, such as January or March        January to December
      m     Numeric representation of a month, with leading zeros                     01 to 12
      M     A short textual representation of a month                                 Jan to Dec
      n     Numeric representation of a month, without leading zeros                  1 to 12
      t     Number of days in the given month                                         28 to 31
      L     Whether it's a leap year                                                  1 if it is a leap year, 0 otherwise.
      o     ISO-8601 year number (identical to (Y), but if the ISO week number (W)    Examples: 1998 or 2004
            belongs to the previous or next year, that year is used instead)
      Y     A full numeric representation of a year, 4 digits                         Examples: 1999 or 2003
      y     A two digit representation of a year                                      Examples: 99 or 03
      a     Lowercase Ante meridiem and Post meridiem                                 am or pm
      A     Uppercase Ante meridiem and Post meridiem                                 AM or PM
      g     12-hour format of an hour without leading zeros                           1 to 12
      G     24-hour format of an hour without leading zeros                           0 to 23
      h     12-hour format of an hour with leading zeros                              01 to 12
      H     24-hour format of an hour with leading zeros                              00 to 23
      i     Minutes, with leading zeros                                               00 to 59
      s     Seconds, with leading zeros                                               00 to 59
      u     Milliseconds, with leading zeros                                          001 to 999
      O     Difference to Greenwich time (GMT) in hours and minutes                   Example: +1030
      P     Difference to Greenwich time (GMT) with colon between hours and minutes   Example: -08:00
      T     Timezone abbreviation of the machine running the code                     Examples: EST, MDT, PDT ...
      Z     Timezone offset in seconds (negative if west of UTC, positive if east)    -43200 to 50400
      c     ISO 8601 date                                                             2007-04-17T15:19:21+08:00
      U     Seconds since the Unix Epoch (January 1 1970 00:00:00 GMT)                1193432466 or -2138434463
      
      
      JAVA - java.text.SimpleDateFormat
		Letter	Date or Time Component		Presentation		Examples
		G		Era designator				Text				AD
		y		Year						Year				1996; 96
		M		Month in year				Month				July; Jul; 07
		w		Week in year				Number				27
		W		Week in month				Number				2
		D		Day in year					Number				189
		d		Day in month				Number				10
		F		Day of week in month		Number				2
		E		Day in week					Text				Tuesday; Tue
		a		Am/pm marker				Text				PM
		H		Hour in day (0-23)			Number				0
		k		Hour in day (1-24)			Number				24
		K		Hour in am/pm (0-11)		Number				0
		h		Hour in am/pm (1-12)		Number				12
		m		Minute in hour				Number				30
		s		Second in minute			Number				55
		S		Millisecond					Number				978
		z		Time zone					General time zone	Pacific Standard Time; PST; GMT-08:00
		Z		Time zone					RFC 822 time zone	-0800
      */
    
    /*
     * GWT - org.gwtwidgets.client.util.SimpleDateFormat
     * 
     	TOKEN_DAY_OF_WEEK = "E";
		TOKEN_DAY_OF_MONTH = "d";
		TOKEN_MONTH = "M";
		TOKEN_YEAR = "y";
		TOKEN_HOUR_12 = "h";
		TOKEN_HOUR_24 = "H";
		TOKEN_MINUTE = "m";
		TOKEN_SECOND = "s";
		TOKEN_MILLISECOND = "S";
		TOKEN_AM_PM = "a";
     */
}
