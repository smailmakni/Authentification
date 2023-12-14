package tn.dksoft.authentification.filter;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import tn.dksoft.authentification.security.JWTUtil;

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

	private AuthenticationManager authenticationManager;

	public JwtAuthenticationFilter(AuthenticationManager authenticationManager) {
		this.authenticationManager = authenticationManager;
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException {
		System.out.println("attemptAuthentication");
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		System.out.println(username);
		System.out.println(password);
		UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username,
				password);
		return authenticationManager.authenticate(authenticationToken);
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authResult) throws IOException, ServletException {
		System.out.println("successfulAuthentication");
		User user = (User) authResult.getPrincipal(); // ===> retourne l'utilisateur connecté detype object
		System.out.println("Authenticated user: " + user.getUsername());
		Algorithm algo1 = Algorithm.HMAC256(JWTUtil.SECRET);
		String jwtAccessToken = JWT.create().withSubject(user.getUsername())
				.withExpiresAt(new Date(System.currentTimeMillis() + JWTUtil.EXPIRE_ACCESS_TOKEN))
				.withIssuer(request.getRequestURI().toString())
				.withClaim("roles",
						user.getAuthorities().stream().map(ga -> ga.getAuthority()).collect(Collectors.toList()))
				.sign(algo1);

		String jwtRefreshToken = JWT.create().withSubject(user.getUsername())
				.withExpiresAt(new Date(System.currentTimeMillis() + JWTUtil.EXPIRE_REFRESH_TOKEN))
				.withIssuer(request.getRequestURI().toString()).sign(algo1);

		Map<String, String> idToken = new HashMap<>();
		idToken.put("access-token", jwtAccessToken);
		idToken.put("refresh-token", jwtRefreshToken);
		response.setContentType("application/json");
		new ObjectMapper().writeValue(response.getOutputStream(), idToken);

	}

}
