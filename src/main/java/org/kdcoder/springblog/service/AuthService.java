package org.kdcoder.springblog.service;

import java.util.Optional;

import org.kdcoder.springblog.dto.LoginRequest;
import org.kdcoder.springblog.dto.RegisterRequestDto;
import org.kdcoder.springblog.model.User;
import org.kdcoder.springblog.repository.UserRepository;
import org.kdcoder.springblog.security.JwtProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private PasswordEncoder encoder;
	@Autowired
	private AuthenticationManager authenticationManager;
	@Autowired
	private JwtProvider jwtProvider;
	
	public void signup(RegisterRequestDto registerRequestDto) {
		User user = new User();
		user.setUserName(registerRequestDto.getUsername());
		user.setPassword(encodePassword(registerRequestDto.getPassword()));
		user.setEmail(registerRequestDto.getEmail());
		userRepository.save(user);
	}

	private String encodePassword(String password) {
		return encoder.encode(password);
	}

	public AuthenticationResponse login(LoginRequest loginRequest) {
		Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
		SecurityContextHolder.getContext().setAuthentication(authenticate);
		String authenticationToken = jwtProvider.generatToken(authenticate);
		return new AuthenticationResponse(authenticationToken, loginRequest.getUsername());
	}

	public Optional<org.springframework.security.core.userdetails.User> getCurrentUser() {
		org.springframework.security.core.userdetails.User principle = (org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		return Optional.of(principle);
	}

}
