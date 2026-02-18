package ro.cristiansterie.vote.config;

import java.lang.invoke.MethodHandles;
import java.util.Base64;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

	protected static final Logger log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	@Autowired
	private UserService userDetailsService;
	@Autowired
	private PasswordEncoder passwordEncoder;

	@Override
	public Authentication authenticate(Authentication authentication) {
		final UserDetails userDetail = userDetailsService.loadUserByUsername(authentication.getName());

		if (authentication.getCredentials() != null) {
			if (!passwordEncoder.matches(authentication.getCredentials().toString(), userDetail.getPassword())) {
				throw new BadCredentialsException("invalid_credentials");
			}

			log.info("CustomAuthenticationManager::: logging ADMIN user in... username: {}, auth: {}",
					userDetail.getUsername(), userDetail.getAuthorities());
			return new UsernamePasswordAuthenticationToken(userDetail.getUsername(), userDetail.getPassword(),
					userDetail.getAuthorities());
		}

		try {
			var id = (Long) authentication.getPrincipal();
			log.info("CustomAuthenticationManager::: logging id {} in", id);

			if (authentication.getPrincipal() != null && id > 0) {
				var foundUser = userDetailsService.getVoter(id);

				var auth = (FaceIDAuthentication) authentication;
				auth.getFaceImageBase64().equals(Base64.getEncoder().encodeToString(foundUser.getFaceImage()));

				log.info("CustomAuthenticationManager::: logging VOTANT user in... principal: {}, auth: {}",
						authentication.getPrincipal(), userDetail.getAuthorities());
				return new FaceIDAuthentication(authentication.getPrincipal(), auth.getFaceImageBase64(),
						userDetail.getAuthorities());
			}
		} catch (ClassCastException cce) {
			throw new BadCredentialsException(cce.getMessage());
		}

		throw new BadCredentialsException("neither ADMIN nor VOTANT tried to authenticate...");
	}

}