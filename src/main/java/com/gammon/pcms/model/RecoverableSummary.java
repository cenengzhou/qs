package com.gammon.pcms.model;

import java.math.BigDecimal;

public class RecoverableSummary {
    private BigDecimal recoverableAmount;
    private BigDecimal nonRecoverableAmount;
    private BigDecimal unclassifiedAmt;

    public RecoverableSummary() {
    }


    public RecoverableSummary(BigDecimal recoverableAmount, BigDecimal nonRecoverableAmount, BigDecimal unclassifiedAmt) {
        this.recoverableAmount = recoverableAmount;
        this.nonRecoverableAmount = nonRecoverableAmount;
        this.unclassifiedAmt = unclassifiedAmt;
    }

    public BigDecimal getRecoverableAmount() {
        return recoverableAmount;
    }

    public void setRecoverableAmount(BigDecimal recoverableAmount) {
        this.recoverableAmount = recoverableAmount;
    }

    public BigDecimal getNonRecoverableAmount() {
        return nonRecoverableAmount;
    }

    public void setNonRecoverableAmount(BigDecimal nonRecoverableAmount) {
        this.nonRecoverableAmount = nonRecoverableAmount;
    }

    public BigDecimal getUnclassifiedAmt() {
        return unclassifiedAmt;
    }

    public void setUnclassifiedAmt(BigDecimal unclassifiedAmt) {
        this.unclassifiedAmt = unclassifiedAmt;
    }
}
