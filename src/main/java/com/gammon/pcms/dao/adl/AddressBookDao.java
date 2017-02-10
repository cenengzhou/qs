package com.gammon.pcms.dao.adl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Hibernate;
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
	public List<AddressBook> findByAddressBookTypeList(List<String> addressBookTypeList) throws DataAccessException {
		Criteria criteria = getSession().createCriteria(getType());

		// Where
		criteria.add(Restrictions.in("addressBookTypeCode", addressBookTypeList));
		List<AddressBook> resultList = (List<AddressBook>) criteria.list();
		for(AddressBook addressBook : resultList){
			Hibernate.initialize(addressBook.getSubcontractorWorkscopes());
		}
		return resultList;
	}

}
