package com.reeco.http.configuration;

import com.reeco.http.cache.ConnectionCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;



public class PreInterceptor extends HandlerInterceptorAdapter {
    private static Logger log = LoggerFactory.getLogger(PreInterceptor.class);
    @Autowired
    ConnectionCache connectionCache;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        long startTime = System.currentTimeMillis();
        System.out.println("\n-------- LogInterception -------------- ");
        System.out.println("Request URL: " + request.getRequestURL());
//        System.out.println("Start Time: " + secret_key);
//        System.out.println("request header " + request.getHeader("secret_key"));

        if(request.getHeader("access_key") == null){
            response.getWriter().write("Not specified access key");
            response.setStatus(403);
            return false;

        }
        String accessKey = request.getHeader("access_key");
        String connectionId = accessKey.split("%")[0];
        if (connectionCache.get(connectionId) == null || !request.getHeader("access_key").equals(connectionCache.get(connectionId).getAccessToken())){
            response.getWriter().write("Invalid access key");
            response.setStatus(403);
            return false;
        }
        request.setAttribute("startTime", startTime);
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, //
                           Object handler, ModelAndView modelAndView) throws Exception {

//        System.out.println("\n-------- LogInterception.postHandle --- ");
//        System.out.println("Request URL: " + request.getRequestURL());
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, //
                                Object handler, Exception ex) throws Exception {
//        System.out.println("\n-------- LogInterception.afterCompletion --- ");
//
        long startTime = (Long) request.getAttribute("startTime");
        long endTime = System.currentTimeMillis();
//        System.out.println("Request URL: " + request.getRequestURL());
//        System.out.println("End Time: " + endTime);

        System.out.println("Time Taken: " + (endTime - startTime));
    }
}