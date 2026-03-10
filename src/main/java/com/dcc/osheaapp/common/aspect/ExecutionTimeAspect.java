package com.dcc.osheaapp.common.aspect;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

@Aspect
@Component
public class ExecutionTimeAspect {
	private static final Logger LOGGER = LogManager.getLogger(ExecutionTimeAspect.class);

	@Around("@annotation(com.dcc.osheaapp.common.model.LogExecutionTime)")
	public Object timeLogger(ProceedingJoinPoint pjp) throws Throwable {
		MethodSignature signature = (MethodSignature) pjp.getSignature();

		String className = signature.getDeclaringType().getSimpleName();
		String methodName = signature.getMethod().getName();

		StopWatch watch = new StopWatch(className + " - " + methodName);
		watch.start();
		Object result = pjp.proceed();
		watch.stop();
		LOGGER.info("Elapsed:: [ " + watch.getId() + " ]:: " + watch.getTotalTimeSeconds() + "s");
		return result;
	}
}
