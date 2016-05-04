package com.gammon.qs.aspect;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gammon.qs.application.exception.DatabaseOperationException;
import com.gammon.qs.application.exception.ValidateBusinessLogicException;
import com.gammon.qs.service.security.SecurityService;

@Aspect()
@Component("loggingAspect")
public class Logging {
	
	@Autowired
	private SecurityService securityService;
	
	
	public Logging(){}
	
	@Pointcut("within(com.gammon.qs.web..*)")
	public void inWebLayer(){}
	
	@Pointcut("within(com.gammon.qs.service..*)")
	public void inServiceLayer(){}
	
	@Pointcut("within(com.gammon.qs.dao..*)")
	public void inDataAccessLayer(){}
	
	@Pointcut("!execution(* com.gammon.qs.service.security.SecurityService.*(..))")
	public void excludeSecurityService(){}
	
	@Pointcut("execution(public * *(..))")
	public void anyPublicMethod(){}
	
	@Around("inWebLayer() && anyPublicMethod()")
	public Object logWebLayer(ProceedingJoinPoint pjp) throws Throwable{
		Logger logger = Logger.getLogger(pjp.getSignature().getDeclaringTypeName());
		logger.log(Level.FINE, getFormattedLogHeader(pjp.getSignature().getDeclaringTypeName()) , "Log Method Executed: " + pjp.getTarget().getClass().getName() + ": " + pjp.getSignature().getName());
		
		try{
			return pjp.proceed();
		}catch(Exception e)
		{
			logger.log(Level.SEVERE, getFormattedLogHeader(pjp.getSignature().getDeclaringTypeName()), e);
			if (e instanceof ValidateBusinessLogicException || e instanceof DatabaseOperationException)
				throw e;
			else throw new Exception(e);
		}
	}
	
	@Around("inServiceLayer() && anyPublicMethod() && excludeSecurityService()")
	public Object logServiceLayer(ProceedingJoinPoint pjp) throws Throwable{
		Logger logger = Logger.getLogger(pjp.getSignature().getDeclaringTypeName());
		logger.log(Level.FINE, getFormattedLogHeader(pjp.getSignature().getDeclaringTypeName()) , "Service Method Executed: " + pjp.getTarget().getClass().getName() + ": " + pjp.getSignature().getName());
		
		try{
			return pjp.proceed();
		}catch(Exception e)
		{
			logger.log(Level.SEVERE, getFormattedLogHeader(pjp.getSignature().getDeclaringTypeName()), e);
			if (e instanceof ValidateBusinessLogicException || e instanceof DatabaseOperationException)
				throw e;
			else throw new Exception(e);
		}
	}
	
	@Around("inDataAccessLayer() && anyPublicMethod()")
	public Object logDataAccessLayer(ProceedingJoinPoint pjp) throws Throwable{
		Logger logger = Logger.getLogger(pjp.getSignature().getDeclaringTypeName());
		logger.log(Level.FINE, getFormattedLogHeader(pjp.getSignature().getDeclaringTypeName()) , "Data Access Method Executed: " + pjp.getTarget().getClass().getName() + ": " + pjp.getSignature().getName());
				
		try{
			return pjp.proceed();
		}catch(Exception e)
		{
			logger.log(Level.SEVERE, getFormattedLogHeader(pjp.getSignature().getDeclaringTypeName()), e);
			if (e instanceof ValidateBusinessLogicException || e instanceof DatabaseOperationException)
				throw e;
			else throw new Exception(e);
		}
	}

	
	private String getFormattedLogHeader(String classname)
	{	
		return "["+ this.securityService.getCurrentRemoteAddress() 
			+"]["+(this.securityService.getCurrentUser()!=null? this.securityService.getCurrentUser().getFullname():null)
			+"]["+classname+"]";
	}
	
}
