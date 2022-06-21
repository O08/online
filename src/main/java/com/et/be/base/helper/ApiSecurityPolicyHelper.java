package com.et.be.base.helper;

import com.et.be.base.annotation.ApiSecutityPolicy;
import com.et.be.base.wrapper.ApiHttpServletRequestWrapper;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * api 安全校验辅助类
 */
public class ApiSecurityPolicyHelper {


   private HttpServletRequest request;

   private ApiHttpServletRequestWrapper wrapperedRequest;
   private Class clz;

   private Method method;

   public ApiSecurityPolicyHelper (HttpServletRequest request,Class clz) {
      this.request = request;
      this.clz = clz;
   }

   /**
    * 策略匹配
    * @param method
    * @param api
    * @return
    */
   private boolean matchPolicySuccess(Method method,String api){
      return method.getAnnotation(ApiSecutityPolicy.class).value()
              .equalsIgnoreCase(api);
   }


   /**
    * 是否配置类安全策略
    * @return
    */
   public boolean needExecuteSecurityPolicy(){
      // 获取配置类的方法
      Method[] methods = clz.getDeclaredMethods();
      // 请求的 api
      String api = request.getServletPath();
      //遍历所有列的⽅法名称进⾏⽐较
      for (Method method : methods){
         // 判断方法是否有注解
         if(method.isAnnotationPresent(ApiSecutityPolicy.class)){
            // api 匹配 对应的安全策略,匹配成功 执行安全策略
            if(matchPolicySuccess(method,api)){
               this.method = method;
               return true;
            }

         }
      }
      return false;
   }


   /**
    * 执行策略
    * @return
    */
   public boolean executePolicy(){
      try {
         Object obj = clz.newInstance();
         this.wrapperedRequest = new ApiHttpServletRequestWrapper(this.request);
         return (boolean) method.invoke(obj,wrapperedRequest);
      } catch (InstantiationException | IllegalAccessException | InvocationTargetException | IOException e) {
         throw new RuntimeException(e);
      }
   }

   /**
    * 返回包装的request 实现多次读取request
    * @return
    */
   public ApiHttpServletRequestWrapper getRequest(){
      return this.wrapperedRequest;
   }



}
