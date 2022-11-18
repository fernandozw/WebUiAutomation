package com.thinkingdata.lib;

import java.lang.reflect.Method;
/**
 * @author Fernando Zhu
 * @version 1.0
 * @date 2020/5/21 17:45
 */
public class ReflectionUtils {
	@SuppressWarnings("unused")
	/**
	 * 动态加载类并执行指定方法
	 * @param object
	 * 实例化的类
	 * @param methodName
	 * 方法名称
	 * @param paramTypes
	 * 参数类型数组
	 * @param paramValues
	 * 参数值数组
	 */
	public static void initLoadClass(Object object, String methodName, String[] paramTypes, String[] paramValues) throws Exception {

			@SuppressWarnings("rawtypes")
			Class[] parameterTypes = constructTypes(paramTypes);

			// 得到方法
			Method method = object.getClass().getMethod(methodName, parameterTypes);

			// 根据参数类型数组和参数值数组得到参数值的obj数组
			Object[] parameterValues = constructValues(paramTypes, paramValues);

			// 执行这个方法并返回obj值
			Object returnValue = method.invoke(object, parameterValues);

		
	}

	/**
	 * 被调用方法的参数值
	 * 
	 * @param paramTypes
	 * 被调用方法的参数类型数组
	 * @param paramValues
	 * 被调用方法的参数值
	 * @return
	 */
	private static Object[] constructValues(String[] paramTypes, String[] paramValues) {
		Object[] obj = new Object[paramTypes.length];
		for (int i = 0; i < paramTypes.length; i++) {
			if (paramTypes[i] != null && !paramTypes[i].trim().equals("")) {
				if ("Integer".equals(paramTypes[i]) || "int".equals(paramTypes[i])) {
					obj[i] = Integer.parseInt(paramValues[i]);
				} else if ("Double".equals(paramTypes[i]) || "double".equals(paramTypes[i])) {
					obj[i] = Double.parseDouble(paramValues[i]);
				} else if ("Float".equals(paramTypes[i]) || "float".equals(paramTypes[i])) {
					obj[i] = Float.parseFloat(paramValues[i]);
				} else {
					obj[i] = paramValues[i];
				}
			}
		}
		return obj;
	}

	/**
	 * 被调用方法的参数类型
	 * 
	 * @param paramTypes
	 * 被调用方法的参数类型数组
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	private static Class[] constructTypes(String[] paramTypes) {
		Class[] cls = new Class[paramTypes.length];
		for (int i = 0; i < paramTypes.length; i++) {
			if (paramTypes[i] != null && !paramTypes[i].trim().equals("")) {
				if ("Integer".equals(paramTypes[i]) || "int".equals(paramTypes[i])) {
					cls[i] = Integer.class;
				} else if ("Double".equals(paramTypes[i]) || "double".equals(paramTypes[i])) {
					cls[i] = Double.class;
				} else if ("Float".equals(paramTypes[i]) || "float".equals(paramTypes[i])) {
					cls[i] = Float.class;
				} else {
					cls[i] = String.class;
				}
			}
		}
		return cls;
	}
}
