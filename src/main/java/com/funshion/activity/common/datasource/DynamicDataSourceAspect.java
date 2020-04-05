//package com.funshion.activity.common.datasource;
//
//import org.aspectj.lang.JoinPoint;
//import org.aspectj.lang.Signature;
//import org.aspectj.lang.annotation.Aspect;
//import org.aspectj.lang.annotation.Before;
//import org.aspectj.lang.reflect.MethodSignature;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.core.annotation.Order;
//import org.springframework.stereotype.Component;
//
//import java.lang.reflect.Method;
//import java.util.Random;
//
//
//@Aspect
//@Order(-1)// 保证该AOP在@Transactional之前执行
//@Component
//public class DynamicDataSourceAspect {
//
//	private static final Logger logger = LoggerFactory.getLogger(DynamicDataSourceAspect.class);
//
//	@Before("execution(* com.funshion.activity.*.mapper.*.*(..))")
//	public void changeDataSource(JoinPoint point) throws Throwable {
//		DynamicDataSourceContextHolder.setDataSourceType(DataSourceType.MASTER);
//		Signature signature = point.getSignature();
//		Class clazz = signature.getDeclaringType();
//		MethodSignature methodSignature = (MethodSignature) signature;
//		Method method = methodSignature.getMethod();
//		TargetDataSource ds = null;
//		if (clazz.isAnnotationPresent(TargetDataSource.class)) {
//			ds = (TargetDataSource) clazz.getAnnotation(TargetDataSource.class);
//		}
//		if (method.isAnnotationPresent(TargetDataSource.class)) {
//			ds = method.getAnnotation(TargetDataSource.class);
//		}
//		String dsId;
//		if (ds == null) {
//			dsId = DataSourceType.MASTER;
//		} else {
//			dsId = ds.value();
//		}
//
//		if (DataSourceType.SLAVE.equals(dsId)) {
//			Random rdm = new Random(System.currentTimeMillis());
//			if (Math.abs(rdm.nextInt()) % 2 == 1) {
//				dsId = DataSourceType.SLAVE_01;
//			} else {
//				dsId = DataSourceType.SLAVE_02;
//			}
//		}
//		if (!DynamicDataSourceContextHolder.containsDataSource(dsId)) {
//			logger.error("数据源[{}]不存在，使用默认数据源 > {}", dsId, point.getSignature());
//		} else {
//			DynamicDataSourceContextHolder.setDataSourceType(dsId);
//			logger.debug("Use DataSource : {} > {}", dsId, point.getSignature());
//		}
//	}
//
//	//去掉after是因为要去重新获取注解，影响性能，直接在before上set即可
//	/*@After("execution(* com.funshion.activity.*.mapper.*.*(..))")
//	public void restoreDataSource(JoinPoint point) {
//		logger.debug("Revert DataSource : {} > {}", ds.value(), point.getSignature());
//		DynamicDataSourceContextHolder.clearDataSourceType();
//	}*/
//
//}