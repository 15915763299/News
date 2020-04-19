//package com.demo.news.core.requestwrapper;
//
//
//import org.springframework.stereotype.Component;
//import org.springframework.web.util.ContentCachingResponseWrapper;
//
//import javax.servlet.*;
//import javax.servlet.annotation.WebFilter;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//
//@Component
//@WebFilter(urlPatterns = "/*", filterName = "ChannelFilter")
//public class ChannelFilter implements Filter {
//
//    @Override
//    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
//        ServletRequest requestWrapper = new RequestWrapper((HttpServletRequest) servletRequest);
//        //The following filter in chain sets content-length:
//        ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper((HttpServletResponse) servletResponse);
//        filterChain.doFilter(requestWrapper, servletResponse);
//        responseWrapper.copyBodyToResponse();
//    }
//
//}
