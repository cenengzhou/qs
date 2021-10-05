package com.gammon.pcms.dto;

import java.util.ArrayList;
import java.util.List;

public class RocJasperWrapper {
	private String asAtDate;
	private String projectName;
	private String projectNumber;

	private RocCaseWrapper sumCaseTenderRisks;
	private RocCaseWrapper sumCaseTenderOpps;
	private RocCaseWrapper sumCaseTenderOther;
	private RocCaseWrapper sumCaseRisks;
	private RocCaseWrapper sumCaseOpps;

	private List<IRocDetailJasperWrapper> detailsTenderRisks = new ArrayList<>();
	private List<IRocDetailJasperWrapper> detailsTenderOpps = new ArrayList<>();
	private List<IRocDetailJasperWrapper> detailsTenderOther = new ArrayList<>();
	private List<IRocDetailJasperWrapper> detailsRisks = new ArrayList<>();
	private List<IRocDetailJasperWrapper> detailsOpps = new ArrayList<>();
	
	private List<IRocDetailJasperWrapper> detailsContingencies = new ArrayList<>();
	private List<IRocDetailJasperWrapper> detailsRisksOpps = new ArrayList<>();
	
	public RocJasperWrapper() {
		super();
	}

	public RocJasperWrapper(String asAtDate, String projectName, String projectNumber,
			RocCaseWrapper sumCaseTenderRisks, RocCaseWrapper sumCaseTenderOpps, RocCaseWrapper sumCaseTenderOther,
			RocCaseWrapper sumCaseRisks, RocCaseWrapper sumCaseOpps, List<IRocDetailJasperWrapper> detailsContingencies,
			List<IRocDetailJasperWrapper> detailsRisksOpps) {
		super();
		this.asAtDate = asAtDate;
		this.projectName = projectName;
		this.projectNumber = projectNumber;
		this.sumCaseTenderRisks = sumCaseTenderRisks;
		this.sumCaseTenderOpps = sumCaseTenderOpps;
		this.sumCaseTenderOther = sumCaseTenderOther;
		this.sumCaseRisks = sumCaseRisks;
		this.sumCaseOpps = sumCaseOpps;
		this.detailsContingencies = detailsContingencies;
		this.detailsRisksOpps = detailsRisksOpps;
	}

	public RocJasperWrapper(String asAtDate, String projectName, String projectNumber, RocCaseWrapper sumCaseTenderRisks,
			RocCaseWrapper sumCaseTenderOpps, RocCaseWrapper sumCaseTenderOther, RocCaseWrapper sumCaseRisks,
			RocCaseWrapper sumCaseOpps, List<IRocDetailJasperWrapper> detailsTenderRisks,
			List<IRocDetailJasperWrapper> detailsTenderOpps, List<IRocDetailJasperWrapper> detailsTenderOther,
			List<IRocDetailJasperWrapper> detailsRisks, List<IRocDetailJasperWrapper> detailsOpps) {
		this.asAtDate = asAtDate;
		this.projectName = projectName;
		this.projectNumber = projectNumber;
		this.sumCaseTenderRisks = sumCaseTenderRisks;
		this.sumCaseTenderOpps = sumCaseTenderOpps;
		this.sumCaseTenderOther = sumCaseTenderOther;
		this.sumCaseRisks = sumCaseRisks;
		this.sumCaseOpps = sumCaseOpps;
		this.detailsTenderRisks = detailsTenderRisks;
		this.detailsTenderOpps = detailsTenderOpps;
		this.detailsTenderOther = detailsTenderOther;
		this.detailsRisks = detailsRisks;
		this.detailsOpps = detailsOpps;
	}


	public String getAsAtDate() {
		return this.asAtDate;
	}

	public void setAsAtDate(String asAtDate) {
		this.asAtDate = asAtDate;
	}

	public String getProjectName() {
		return this.projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public String getProjectNumber() {
		return this.projectNumber;
	}

	public void setProjectNumber(String projectNumber) {
		this.projectNumber = projectNumber;
	}

	public RocCaseWrapper getSumCaseTenderRisks() {
		return this.sumCaseTenderRisks;
	}

	public void setSumCaseTenderRisks(RocCaseWrapper sumCaseTenderRisks) {
		this.sumCaseTenderRisks = sumCaseTenderRisks;
	}

	public RocCaseWrapper getSumCaseTenderOpps() {
		return this.sumCaseTenderOpps;
	}

	public void setSumCaseTenderOpps(RocCaseWrapper sumCaseTenderOpps) {
		this.sumCaseTenderOpps = sumCaseTenderOpps;
	}

	public RocCaseWrapper getSumCaseTenderOther() {
		return this.sumCaseTenderOther;
	}

	public void setSumCaseTenderOther(RocCaseWrapper sumCaseTenderOther) {
		this.sumCaseTenderOther = sumCaseTenderOther;
	}

	public RocCaseWrapper getSumCaseRisks() {
		return this.sumCaseRisks;
	}

	public void setSumCaseRisks(RocCaseWrapper sumCaseRisks) {
		this.sumCaseRisks = sumCaseRisks;
	}

	public RocCaseWrapper getSumCaseOpps() {
		return this.sumCaseOpps;
	}

	public void setSumCaseOpps(RocCaseWrapper sumCaseOpps) {
		this.sumCaseOpps = sumCaseOpps;
	}

	public List<IRocDetailJasperWrapper> getDetailsTenderRisks() {
		return this.detailsTenderRisks;
	}

	public void setDetailsTenderRisks(List<IRocDetailJasperWrapper> list) {
		this.detailsTenderRisks = list;
	}

	public List<IRocDetailJasperWrapper> getDetailsTenderOpps() {
		return this.detailsTenderOpps;
	}

	public void setDetailsTenderOpps(List<IRocDetailJasperWrapper> detailsTenderOpps) {
		this.detailsTenderOpps = detailsTenderOpps;
	}

	public List<IRocDetailJasperWrapper> getDetailsTenderOther() {
		return this.detailsTenderOther;
	}

	public void setDetailsTenderOther(List<IRocDetailJasperWrapper> detailsTenderOther) {
		this.detailsTenderOther = detailsTenderOther;
	}

	public List<IRocDetailJasperWrapper> getDetailsRisks() {
		return this.detailsRisks;
	}

	public void setDetailsRisks(List<IRocDetailJasperWrapper> detailsRisks) {
		this.detailsRisks = detailsRisks;
	}

	public List<IRocDetailJasperWrapper> getDetailsOpps() {
		return this.detailsOpps;
	}

	public void setDetailsOpps(List<IRocDetailJasperWrapper> detailsOpps) {
		this.detailsOpps = detailsOpps;
	}

	public List<IRocDetailJasperWrapper> getDetailsContingencies() {
		return detailsContingencies;
	}

	public void setDetailsContingencies(List<IRocDetailJasperWrapper> detailsContingencies) {
		this.detailsContingencies = detailsContingencies;
	}

	public List<IRocDetailJasperWrapper> getDetailsRisksOpps() {
		return detailsRisksOpps;
	}

	public void setDetailsRisksOpps(List<IRocDetailJasperWrapper> detailsRisksOpps) {
		this.detailsRisksOpps = detailsRisksOpps;
	}

	@Override
	public String toString() {
		return "RocJasperWrapper [asAtDate=" + asAtDate + ", projectName=" + projectName + ", projectNumber="
				+ projectNumber + ", sumCaseTenderRisks=" + sumCaseTenderRisks + ", sumCaseTenderOpps="
				+ sumCaseTenderOpps + ", sumCaseTenderOther=" + sumCaseTenderOther + ", sumCaseRisks=" + sumCaseRisks
				+ ", sumCaseOpps=" + sumCaseOpps + ", detailsTenderRisks=" + detailsTenderRisks + ", detailsTenderOpps="
				+ detailsTenderOpps + ", detailsTenderOther=" + detailsTenderOther + ", detailsRisks=" + detailsRisks
				+ ", detailsOpps=" + detailsOpps + ", detailsContingencies=" + detailsContingencies
				+ ", detailsRisksOpps=" + detailsRisksOpps + "]";
	}

}
