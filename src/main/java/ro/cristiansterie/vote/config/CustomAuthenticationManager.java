package ro.cristiansterie.vote.config;

import java.util.Base64;

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

		if (authentication.getCredentials() != null) {
			if (!passwordEncoder.matches(authentication.getCredentials().toString(), userDetail.getPassword())) {
				throw new BadCredentialsException("invalid_credentials");
			}

			return new UsernamePasswordAuthenticationToken(userDetail.getUsername(), userDetail.getPassword(),
					userDetail.getAuthorities());
		}

		try {
			var id = (Integer) authentication.getPrincipal();

			if (authentication.getPrincipal() != null && id > 0) {
				var foundUser = userDetailsService.getVoter(id);

				var auth = (FaceIDAuthentication) authentication;
				auth.getFaceImageBase64().equals(Base64.getEncoder().encodeToString(foundUser.getFaceImage()));

				return new FaceIDAuthentication(authentication.getPrincipal(), auth.getFaceImageBase64(),
						userDetail.getAuthorities());
			}
		} catch (ClassCastException cce) {
			throw new BadCredentialsException(cce.getMessage());
		}

		throw new BadCredentialsException("neither ADMIN nor VOTANT tried to authenticate...");
	}

}