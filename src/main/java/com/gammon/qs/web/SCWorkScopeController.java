package com.gammon.qs.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gammon.qs.application.exception.DatabaseOperationException;
import com.gammon.qs.client.repository.SCWorkScopeRepositoryRemote;
import com.gammon.qs.dao.SCWorkScopeHBDao;
import com.gammon.qs.domain.SCPackage;
import com.gammon.qs.domain.SCWorkScope;
@Service("scWorkScopeController")
public class SCWorkScopeController extends GWTSpringController  implements SCWorkScopeRepositoryRemote {

	private static final long serialVersionUID = 8636228677888667024L;
	@Autowired
	private SCWorkScopeHBDao scWorkScopeHBDao;
	
	@Override
	public List<SCWorkScope> obtainSCWorkScopeListBySCPackage(SCPackage scPackage)
			throws DatabaseOperationException {
		return scWorkScopeHBDao.obtainSCWorkScopeListByPackage(scPackage);
	}

	@Override
	public void deleteSCWorkScopeBySCPackage(SCPackage scPackage) throws DatabaseOperationException {
		scWorkScopeHBDao.deleteSCWorkScopeBySCPackage(scPackage);		
	}

}
