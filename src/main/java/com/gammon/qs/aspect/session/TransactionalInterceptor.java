package com.gammon.qs.aspect.session;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.orm.hibernate4.SessionHolder;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Aspect()
public class TransactionalInterceptor {
	
	@Autowired
	@Qualifier("sessionFactory")
	private SessionFactory sessionFactory;
	
	@Pointcut("within(com.gammon.qs.service..*)")
	public void inServiceLayer(){}
	
	@Pointcut("!execution(* com.gammon.qs.service.security.SecurityService.*(..))")
	public void excludeSecurityService(){}

	@Pointcut("execution(public * *(..))")
	public void anyPublicMethod(){}
	
	
	private List<String> signatureList;
	
	@Around("inServiceLayer() && anyPublicMethod() && excludeSecurityService()")
	public Object execute(ProceedingJoinPoint pjp) throws Throwable{
		
			
		Logger logger = Logger.getLogger(pjp.getSignature().getDeclaringTypeName());
		
		String signature = pjp.getSignature().getName();
		
		boolean isTransactionRequired = false;
		if(signatureList!=null){
			if(signatureList.contains(signature))
				isTransactionRequired = true;
		}
		
		
		if(!isTransactionRequired){
			return pjp.proceed();
		}
		
		
		
		Session session = sessionFactory.getCurrentSession();
        boolean existingTransaction = session.getTransaction().isActive();
		
        if (existingTransaction) {
        	logger.log(Level.SEVERE, "Found thread-bound Session for TransactionalQuartzTask");
        }
        else {
            TransactionSynchronizationManager.bindResource(sessionFactory, new SessionHolder(session));
        }
 
		
		try{			
			return pjp.proceed();			
		}catch(Exception e){	
			throw e;
		}
		finally {

            if (existingTransaction) {
            	logger.log(Level.SEVERE,"Not closing pre-bound Hibernate Session after TransactionalQuartzTask");
            }
            else {
                TransactionSynchronizationManager.unbindResource(sessionFactory);
//    			sessionFactory.close(); //will cause Could not open Hibernate Session

            }

        }
        
	}

	public List<String> getSignatureList() {
		return signatureList;
	}

	public void setSignatureList(List<String> signatureList) {
		this.signatureList = signatureList;
	}
	

}
