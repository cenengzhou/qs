package com.gammon.pcms.dao.adl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import com.gammon.pcms.model.adl.AddressBook;

@Repository
public class AddressBookDao extends BaseAdlHibernateDao<AddressBook> {

	public AddressBookDao() {
		super(AddressBook.class);
	}

	@SuppressWarnings("unchecked")
	public List<AddressBook> getAddressBookListOfSubcontractorAndClient(List<String> typeAddressBooks) throws DataAccessException {
		Criteria criteria = getSession().createCriteria(getType());
		criteria.add(Restrictions.in("addressBookTypeCode", typeAddressBooks));
		List<AddressBook> resultList =  criteria.list();
		return resultList;
	}
	
}
