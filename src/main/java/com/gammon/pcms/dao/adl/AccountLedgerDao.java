package com.gammon.pcms.dao.adl;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.gammon.pcms.model.adl.AccountLedger;
import com.gammon.qs.application.exception.DatabaseOperationException;

@Repository
public class AccountLedgerDao extends BaseAdlHibernateDao<AccountLedger> {

	public AccountLedgerDao() {
		super(AccountLedger.class);
	}

	@SuppressWarnings("unchecked")
	public Collection<? extends AccountLedger> obtainFactAccountLedger(String account_type_ledger, String entity_doc_company_key, BigDecimal number_document, String number_je_line_extension, BigDecimal number_journal_entry_line, String type_document) throws DatabaseOperationException{
		Criteria criteria = getSession().createCriteria(getType());
		criteria.add(Restrictions.eq("accountTypeLedger", account_type_ledger));
		criteria.add(Restrictions.eq("entityDocCompanyKey", entity_doc_company_key));
		criteria.add(Restrictions.eq("numberDocument", number_document));
		criteria.add(Restrictions.eq("numberJeLineExtension", number_je_line_extension));
		criteria.add(Restrictions.eq("numberJournalEntryLine", number_journal_entry_line));
		criteria.add(Restrictions.eq("typeDocument", type_document));
		List<AccountLedger> factAccountLedgerList = criteria.list();
		return factAccountLedgerList;
	}
}
