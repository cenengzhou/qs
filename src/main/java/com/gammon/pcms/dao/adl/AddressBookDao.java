package com.gammon.pcms.dao.adl;

import java.math.BigDecimal;
import java.util.List;

import org.apache.commons.validator.GenericValidator;
import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.criterion.MatchMode;
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
		for (AddressBook addressBook : resultList) {
			Hibernate.initialize(addressBook.getSubcontractorWorkscopes());
		}
		return resultList;
	}
	
	
	@SuppressWarnings("unchecked")
	public List<AddressBook> findByAddressBook(String addressBookTypeCode, BigDecimal addressBookNumber, String addressBookName) throws DataAccessException {
		Criteria criteria = getSession().createCriteria(getType());

		// Where
		criteria.add(Restrictions.eq("addressBookTypeCode", addressBookTypeCode));
		if (addressBookNumber != null)
			criteria.add(Restrictions.eq("addressBookNumber", addressBookNumber));

		else if (!GenericValidator.isBlankOrNull(addressBookName))
			criteria.add(Restrictions.ilike("addressBookName", addressBookName, MatchMode.ANYWHERE));

		List<AddressBook> resultList = (List<AddressBook>) criteria.list();
		for (AddressBook addressBook : resultList) {
			Hibernate.initialize(addressBook.getSubcontractorWorkscopes());
		}
		return resultList;
	}
	
	public AddressBook obtainSubcontractor(BigDecimal addressBookNumber) {
		Criteria criteria = getSession().createCriteria(getType());
		criteria.add(Restrictions.in("addressBookTypeCode", new String[] { "O  ", "V  " }));
		criteria.add(Restrictions.eq("addressBookNumber", addressBookNumber));
		AddressBook addressBook = (AddressBook) criteria.uniqueResult();
		return addressBook;
	}

}
