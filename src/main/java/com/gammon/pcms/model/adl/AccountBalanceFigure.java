package com.gammon.pcms.model.adl;

import java.io.Serializable;
import java.math.BigDecimal;

public class AccountBalanceFigure implements Serializable {

    private static final long serialVersionUID = 1L;

    private BigDecimal year;
    private BigDecimal month;
    private BigDecimal amount;
    private BigDecimal yearMonth;

    public AccountBalanceFigure(BigDecimal year, BigDecimal month, BigDecimal amount, BigDecimal yearMonth) {
        this.year = year;
        this.month = month;
        this.amount = amount;
        this.yearMonth = yearMonth;
    }

    public AccountBalanceFigure() {
    }

    public BigDecimal getYear() {
        return year;
    }

    public void setYear(BigDecimal year) {
        this.year = year;
    }

    public BigDecimal getMonth() {
        return month;
    }

    public void setMonth(BigDecimal month) {
        this.month = month;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getYearMonth() {
        return yearMonth;
    }

    public void setYearMonth(BigDecimal yearMonth) {
        this.yearMonth = yearMonth;
    }
}
