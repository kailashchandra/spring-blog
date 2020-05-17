package org.kdcoder.springblog.security;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import javax.annotation.PostConstruct;

import org.kdcoder.springblog.exception.SpringBlogException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

@Service
public class JwtProvider {
	
//	private java.security.Key key;
	private KeyStore keyStore;
	
	@PostConstruct
	public void init() {
//		key = Keys.secretKeyFor(SignatureAlgorithm.HS512);
		try {
			keyStore = KeyStore.getInstance("JKS");
			InputStream resourceAsStream = getClass().getResourceAsStream("/springblog.jsk");
			keyStore.load(resourceAsStream, "kdcoder".toCharArray());
		} catch (KeyStoreException | NoSuchAlgorithmException | CertificateException | IOException e) {
			throw new SpringBlogException("Exception occure while loading keystore!!");
		}
	}
	
	public String generatToken(Authentication authentication) {
		User principal = (User)authentication.getPrincipal();
		return Jwts.builder()
				.setSubject(principal.getUsername())
				.signWith(getPrivateKey())
				.compact();
	}
	
	private PrivateKey getPrivateKey() {
		try {
			return (PrivateKey) keyStore.getKey("springblog", "kdcoder".toCharArray());
		} catch (KeyStoreException | NoSuchAlgorithmException | UnrecoverableKeyException e) {
			throw new SpringBlogException("Exception occure while loading keystore!!");
		}
	}

	public boolean validationToken(String jwt) {
		Jwts.parser().setSigningKey(getPublicKey()).parseClaimsJws(jwt);
		return true;
	}

	private PublicKey getPublicKey() {
		try {
			return keyStore.getCertificate("springblog").getPublicKey();
		} catch (KeyStoreException e) {
			throw new SpringBlogException("Exception occure while loading keystore!!");
		}
	}

	public String getUserNameFromJWT(String token) {
		Claims claim = Jwts.parser()
			.setSigningKey(getPublicKey())
			.parseClaimsJws(token)
			.getBody();
		return claim.getSubject();
		
	}
}
