package com.gammon.qs.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gammon.qs.application.exception.DatabaseOperationException;
import com.gammon.qs.client.repository.ResourceRepositoryRemote;
import com.gammon.qs.dao.ResourceHBDao;
import com.gammon.qs.domain.BQItem;
import com.gammon.qs.domain.Resource;
@Service
public class ResourceRepositoryController extends GWTSpringController implements ResourceRepositoryRemote {

	private static final long serialVersionUID = -962935148686710849L;
	@Autowired
	private ResourceHBDao resourceHBDao;
	
	@Override
	public List<Resource> obtainResourceListByBQItem(BQItem bqItem) throws DatabaseOperationException {
		return resourceHBDao.getResourcesByBQItem(bqItem);
	}

}
