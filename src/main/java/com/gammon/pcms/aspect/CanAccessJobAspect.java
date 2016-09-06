package com.gammon.pcms.aspect;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;

import com.gammon.qs.service.admin.AdminService;

import edu.emory.mathcs.backport.java.util.Arrays;

@Aspect
@Component
public class CanAccessJobAspect {

	@Pointcut("within(com.gammon.*.web..*)")
	public void inWebLayer(){}
	
	@Pointcut("within(com.gammon.*.service..*)")
	public void inServiceLayer(){}
	
	@Pointcut("!execution(* com.gammon.qs.service.admin.AdminService.*(..))")
	public void excludeAdminService(){}
	
	@Pointcut("com.gammon.pcms.aspect.CanAccessJobAspect.inServiceLayer() && "
			+ "excludeAdminService() && (args(str, ..) || args(.., str) || "
			+ "args(*, str, ..) || args(*, *, str, ..) || args(*, *, *, str, ..) || "
			+ "args(.., str, *) || args(.., str, *, *) || args(.., str, *, *, *))")
	public void strInServiceLayer(String str){}
	
	@Autowired
	private AdminService adminService;
	
	String[] jobNumberNames = {"noJob", "jobNo", "jobNumber"};
	
	/**
	 * check if service method's first three or last three parameter is Sting and name like job number
	 * @param joinPoint
	 * @param noJob
	 * @throws Throwable
	 */
	@Before(value = "strInServiceLayer(str)", argNames = "str")
	public void canAccessJobChecking(JoinPoint joinPoint, String str) throws AccessDeniedException{
		canAccessJobChecking(joinPoint);
	}

	/**
	 * call canAccessJob(jobNo) if find parameter like job number
	 * @param joinPoint
	 * @throws AccessDeniedException
	 */
	private void canAccessJobChecking(JoinPoint joinPoint) {
		Logger logger = Logger.getLogger(joinPoint.getSignature().getDeclaringTypeName());
		MethodSignature methodSignature = (MethodSignature) joinPoint.getStaticPart().getSignature();
        String[] parameterNames = methodSignature.getParameterNames();
        Object[] args = joinPoint.getArgs();
        for(int i = 0; i< args.length; i++){
        	if(Arrays.asList(jobNumberNames).contains(parameterNames[i])){
        		logger.log(Level.INFO, joinPoint.getTarget().getClass().getName() + ": " 
				+ joinPoint.getSignature().getName() + "=> canAccessJob(" +args[i]+")") ;
        		adminService.canAccessJob((String) args[i]);
        		break;
        	}
        }
	}
	
}
