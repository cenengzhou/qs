package com.gammon.qs.wrapper;

import java.io.Serializable;
import java.util.List;

public class PaginationWrapper <T> implements Serializable {
	
	private static final long serialVersionUID = 5649859173510855610L;
	private int totalPage;
	private int currentPage;
	private List<T> currentPageContentList;
	private int totalRecords;	

	public int getTotalPage() {
		return totalPage;
	}

	public void setTotalPage(int totalPage) {
		this.totalPage = totalPage;
	}

	public int getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}

	public List<T> getCurrentPageContentList() {
		return currentPageContentList;
	}

	public void setCurrentPageContentList(List<T> currentPageContentList) {
		this.currentPageContentList = currentPageContentList;
	}

	public void setTotalRecords(int totalRecords) {
		this.totalRecords = totalRecords;
	}

	public int getTotalRecords() {
		return totalRecords;
	}
	
	
	

}
