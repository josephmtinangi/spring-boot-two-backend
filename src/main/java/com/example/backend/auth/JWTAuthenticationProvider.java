package com.example.backend.auth;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import com.example.backend.models.User;
import com.example.backend.repositories.UserRepository;

import io.jsonwebtoken.lang.Assert;

@Component
public class JWTAuthenticationProvider implements AuthenticationProvider {

	@Autowired
	private UserRepository userRepository;

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {

		Assert.notNull(authentication, "No authentication data provided");

		String username = (String) authentication.getPrincipal();
		String password = (String) authentication.getCredentials();

		User user = userRepository.findFirstByUsername(username);

		BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

		if (user == null || !bCryptPasswordEncoder.matches(password, user.getPassword())) {
			throw new BadCredentialsException("Authentication failed. Incorrect username or password.");
		}

		UserContext userContext = new UserContext(user.getId(), user.getUsername(), user.getPhoto());

		return new UsernamePasswordAuthenticationToken(userContext, null, Collections.emptyList());
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return (UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication));
	}
}
