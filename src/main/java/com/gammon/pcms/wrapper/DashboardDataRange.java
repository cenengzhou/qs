package com.gammon.pcms.wrapper;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class DashboardDataRange implements Serializable{

	private static final long serialVersionUID = 1L;

	private Date start;
	private Date end;

	public DashboardDataRange() {
	}

	public DashboardDataRange(String item) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		if (item.equals("Latest")) {
			this.end = cal.getTime();
			cal.add(Calendar.MONTH, -11);
			cal.set(Calendar.DAY_OF_MONTH, 1);
			cal.set(Calendar.HOUR_OF_DAY, 0);
			cal.clear(Calendar.MINUTE);
			cal.clear(Calendar.SECOND);
			cal.clear(Calendar.MILLISECOND);
			this.start = cal.getTime();
		} else {
			cal.set(Calendar.YEAR, Integer.parseInt(item));
			cal.set(Calendar.DAY_OF_YEAR, 1);
			this.start = cal.getTime();
			cal.set(Calendar.MONTH, 11);
			cal.set(Calendar.DAY_OF_MONTH, 31);
			this.end = cal.getTime();
		}
	}

	public DashboardDataRange(Date start, Date end) {
		this.start = start;
		this.end = end;
	}

	public String getStartYear() {
		return this.getStartDateFormat("yyyy");
	}

	public String getStartMonth() {
		return this.getStartDateFormat("MM");
	}

	public String getEndYear() {
		return this.getEndDateFormat("yyyy");
	}

	public String getEndMonth() {
		return this.getEndDateFormat("MM");
	}

	public Date getStartDate() {
		return this.start;
	}

	public Date getEndDate() {
		return this.end;
	}

	public String getStartDateFormat(String format) {
		SimpleDateFormat f = new SimpleDateFormat(format);
		return f.format(this.start);
	}

	public String getEndDateFormat(String format) {
		SimpleDateFormat f = new SimpleDateFormat(format);
		return f.format(this.end);
	}

	public BigDecimal getStartYYMM() {
		return new BigDecimal(this.getStartDateFormat("yyMM"));
	}

	public BigDecimal getEndYYMM() {
		return new BigDecimal(this.getEndDateFormat("yyMM"));
	}

	public List<String> toMonthList() {
		List<String> result = new ArrayList<>();
		for (Date current=this.start; current.before(this.end);) {
			SimpleDateFormat f = new SimpleDateFormat("MMM");
			result.add(f.format(current));

			// iterate to next month
			Calendar cal = Calendar.getInstance();
			cal.setTime(current);
			cal.add(Calendar.MONTH, 1);
			current = cal.getTime();
		}
		return result;
	}
}
