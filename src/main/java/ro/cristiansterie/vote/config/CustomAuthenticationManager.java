package ro.cristiansterie.vote.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import ro.cristiansterie.vote.service.UserService;

@Component
public class CustomAuthenticationManager implements AuthenticationManager {

	@Autowired
	private UserService userDetailsService;
	@Autowired
	private PasswordEncoder passwordEncoder;

	@Override
	public Authentication authenticate(Authentication authentication) {
		final UserDetails userDetail = userDetailsService.loadUserByUsername(authentication.getName());

		if (!passwordEncoder.matches(authentication.getCredentials().toString(), userDetail.getPassword())) {
			throw new BadCredentialsException("invalid_credentials");
		}

		return new UsernamePasswordAuthenticationToken(userDetail.getUsername(), userDetail.getPassword(),
				userDetail.getAuthorities());
	}

}