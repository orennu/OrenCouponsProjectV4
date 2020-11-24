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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.orenn.coupons.beans.PostLoginData;
import com.orenn.coupons.logic.TokenCacheController;

@Component
@Order(1)
public class LoginFilter implements Filter {
	
	@Autowired
	private TokenCacheController tokenCacheController;
	
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		
		if (httpRequest.getMethod().equals("OPTIONS")) {
			chain.doFilter(httpRequest, response);
			return;
		}
		
		String url = httpRequest.getRequestURL().toString();
		System.out.println(url);
		
		if (url.endsWith("coupons/") && !url.contains("admin")) {
			chain.doFilter(httpRequest, response);
			return;
		}
		if (url.endsWith("/register")) {
			chain.doFilter(httpRequest, response);
			return;
		}
		if (url.endsWith("/login")) {
			chain.doFilter(httpRequest, response);
			return;
		}
		if (url.contains("reset-password")) {
			chain.doFilter(httpRequest, response);
			return;
		}
		if (url.contains("swagger") || url.contains("api-docs")) {
			chain.doFilter(httpRequest, response);
			return;
		}
		
		String token = httpRequest.getHeader("Authorization");
		PostLoginData postLoginData = (PostLoginData) tokenCacheController.get(token);
		
		if (postLoginData != null) {
			request.setAttribute("userData", postLoginData);
			chain.doFilter(httpRequest, response);
			return;
		}
		
		HttpServletResponse httpResponse = (HttpServletResponse) response;
		int unauthorizedError = 401;
		httpResponse.setStatus(unauthorizedError);
		
	}
	
	public void init(FilterConfig filterConfig) {
	}
	
	public void destroy() {
	}

}
