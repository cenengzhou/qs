package com.gammon.qs.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gammon.qs.application.exception.DatabaseOperationException;
import com.gammon.qs.client.repository.PageRepositoryRemote;
import com.gammon.qs.dao.PageHBDao;
import com.gammon.qs.domain.Bill;
import com.gammon.qs.domain.Page;
@Service
public class PageRepositoryController extends GWTSpringController implements PageRepositoryRemote {

	private static final long serialVersionUID = 3075039592702982701L;
	@Autowired
	private PageHBDao pageHBDao;
	
	@Override
	public List<Page> obtainPageListByBill(Bill bill) throws DatabaseOperationException {
		return pageHBDao.obtainPageListByBill(bill);
	}

}
