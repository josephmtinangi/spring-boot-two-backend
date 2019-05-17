package com.example.backend.auth;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.fasterxml.jackson.databind.ObjectMapper;

public class JWTLoginFilter extends AbstractAuthenticationProcessingFilter {

	public JWTLoginFilter(String url, AuthenticationManager authManager) {
		super(new AntPathRequestMatcher(url));
		setAuthenticationManager(authManager);
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException, IOException, ServletException {

		UsernamePasswordAuthenticationToken userToken = new UsernamePasswordAuthenticationToken(
				request.getParameter("username"), request.getParameter("password"));

		return getAuthenticationManager().authenticate(userToken);
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authResult) throws IOException, ServletException {

		UserContext userContext = (UserContext) authResult.getPrincipal();

		ObjectMapper mapper = new ObjectMapper();

		Map<String, Object> tokenMap = new HashMap<String, Object>();

		tokenMap.put("token", TokenAuthenticationService.createJWTAuthentication(response, userContext.getUsername(),
				userContext.getId() + "", userContext));

		tokenMap.put("userDetails", userContext);
		tokenMap.put("success", true);
		response.setStatus(HttpStatus.OK.value());
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);

		mapper.writeValue(response.getWriter(), tokenMap);
	}

	@Override
	protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException failed) throws IOException, ServletException {

		ObjectMapper mapper = new ObjectMapper();

		Map<String, Object> tokenMap = new HashMap<String, Object>();
		tokenMap.put("message", failed.getMessage());
		tokenMap.put("success", false);

		response.setStatus(HttpStatus.UNAUTHORIZED.value());
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);

		mapper.writeValue(response.getWriter(), tokenMap);
	}
}
