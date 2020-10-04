package com.orenn.coupons.filters;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;

@Component
public class CORSFilter implements Filter {
	
	public CORSFilter() {
	}
	
	public void destroy() {
	}

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
		
		HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
		
		((HttpServletResponse) servletResponse).addHeader("Access-Control-Allow-Origin", "*");
		((HttpServletResponse) servletResponse).addHeader("Access-Control-Allow-Methods", "GET, OPTIONS, HEAD, PUT, POST, DELETE");
		((HttpServletResponse) servletResponse).addHeader("Access-Control-Allow-Headers","password, authorization, Origin, Accept,"
				+ " x-auth-token, Content-Type, Access-Control-Request-Method, Access-Control-Request-Headers");
		
		if (httpServletRequest.getMethod().equals("OPTIONS")) {
			return;
		}
		
		filterChain.doFilter(httpServletRequest, servletResponse);
	}
	
	public void init(FilterConfig filterConfig) throws ServletException {
	}

}
