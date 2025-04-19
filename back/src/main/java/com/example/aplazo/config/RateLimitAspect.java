package com.example.aplazo.config;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.example.aplazo.exception.RateLimitException;
import com.example.aplazo.service.RateLimiterService;

import jakarta.servlet.http.HttpServletRequest;

@Aspect
@Component
public class RateLimitAspect {

	@Autowired
	private RateLimiterService rateLimiterService;

	@Around("@annotation(rateLimit)")
	public Object rateLimit(ProceedingJoinPoint joinPoint, RateLimit rateLimit) throws Throwable {
		ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		HttpServletRequest request = attrs.getRequest();

		String key = request.getRemoteAddr() + ":" + request.getRequestURI();
		if (rateLimiterService.isAllowed(key, rateLimit.limit(), rateLimit.timeWindow())) {
			return joinPoint.proceed();
		} else {
			throw new RateLimitException("Too many requests. Try again later.");
		}
	}
}
