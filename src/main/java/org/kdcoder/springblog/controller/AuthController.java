package org.kdcoder.springblog.controller;

import org.kdcoder.springblog.dto.LoginRequest;
import org.kdcoder.springblog.dto.RegisterRequestDto;
import org.kdcoder.springblog.service.AuthService;
import org.kdcoder.springblog.service.AuthenticationResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
	
	@Autowired
	private AuthService authService;

	@PostMapping("/signup")
	public ResponseEntity<String> signup(@RequestBody RegisterRequestDto registerRequestDto) {
		authService.signup(registerRequestDto);
		return new ResponseEntity<String>(HttpStatus.OK);
	}
	
	@PostMapping("/login")
	public AuthenticationResponse login(@RequestBody LoginRequest loginRequest) {
		return authService.login(loginRequest);
	}
}
