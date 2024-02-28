package tn.dksoft.authentification.security;

import java.util.ArrayList;
import java.util.Collection;

import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.nimbusds.jose.jwk.source.ImmutableSecret;

import tn.dksoft.authentification.entity.AppUser;
import tn.dksoft.authentification.filter.JwtAuthenticationFilter;
import tn.dksoft.authentification.filter.JwtAutorizationFilter;
import tn.dksoft.authentification.service.UserServiceImpl;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

	private UserServiceImpl userServiceImpl;

	@Autowired
	public void setUserServiceImpl(UserServiceImpl userServiceImpl) {
		this.userServiceImpl = userServiceImpl;
	}

	@Bean
	UserDetailsService userDetailsService() {

		UserDetailsService userDetailsService = (new UserDetailsService() {

			@Override
			public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
				AppUser appUser = userServiceImpl.loadUserByUserName(username);
				Collection<GrantedAuthority> authorities = new ArrayList<>();
				appUser.getAppRole().forEach(r -> {
					authorities.add(new SimpleGrantedAuthority(r.getRoleName()));
				});
				return new User(appUser.getUserName(), appUser.getPassword(), authorities);
			}
		});
		return userDetailsService;
	}

	@Bean
	@Autowired
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

		/*
		 * AuthenticationManagerBuilder authenticationManagerBuilder = http
		 * .getSharedObject(AuthenticationManagerBuilder.class);
		 * authenticationManagerBuilder.userDetailsService(userDetailsService());
		 * AuthenticationManager authenticationManager =
		 * authenticationManagerBuilder.build();
		 */
		/* http.formLogin(Customizer.withDefaults()); */
		/*
		 * http.authorizeHttpRequests((req) ->
		 * req.requestMatchers("/delete").hasRole("admin"));
		 */
		http.sessionManagement((session) -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
		http.csrf(AbstractHttpConfigurer::disable);
		http.cors(Customizer.withDefaults());
		http.authorizeHttpRequests((authz) -> authz.requestMatchers("/api/auth/**").permitAll());
		http.authorizeHttpRequests((authz) -> authz.anyRequest().authenticated());
		http.oauth2ResourceServer(oa -> oa.jwt(Customizer.withDefaults()));
		/*
		 * http.authenticationManager(authenticationManager);
		 */
		/*
		 * http.formLogin(formLogin -> formLogin.loginPage("/login") .
		 * successHandler(new AuthenticationSuccessHandler() {
		 * 
		 * @Override public void onAuthenticationSuccess(HttpServletRequest request,
		 * HttpServletResponse response, Authentication authentication) throws
		 * IOException, ServletException { System.out.println("********");
		 * System.out.println("user connected " + authentication.getName());
		 * UrlPathHelper helper = new UrlPathHelper(); String contextPath =
		 * helper.getContextPath(request);
		 * 
		 * response.sendRedirect(contextPath);
		 * 
		 * } }). permitAll());
		 */
		/*
		 * http.httpBasic((basic) -> basic.addObjectPostProcessor(new
		 * ObjectPostProcessor<BasicAuthenticationFilter>() {
		 * 
		 * @Override public <O extends BasicAuthenticationFilter> O postProcess(O
		 * filter) { filter.setSecurityContextRepository(new
		 * HttpSessionSecurityContextRepository()); return filter; } }));
		 */

		/*
		 * http.addFilter(new JwtAuthenticationFilter(
		 * authenticationManager(http.getSharedObject(AuthenticationConfiguration.class)
		 * ))); http.addFilterBefore(new JwtAutorizationFilter(),
		 * UsernamePasswordAuthenticationFilter.class);
		 */

		return http.build();
	}

	@Bean
	JwtEncoder jwtEncoder() {
		return new NimbusJwtEncoder(new ImmutableSecret<>(JWTUtil.SECRET2.getBytes()));
	}

	@Bean
	JwtDecoder jwtDecoder() {
		SecretKeySpec secretKeySpec = new SecretKeySpec(JWTUtil.SECRET2.getBytes(), "RSA");
		return NimbusJwtDecoder.withSecretKey(secretKeySpec).macAlgorithm(MacAlgorithm.HS512).build();
	}

	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	AuthenticationManager authenticationManager(UserDetailsService userDetailsService) {
		DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
		daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
		daoAuthenticationProvider.setUserDetailsService(userDetailsService);
		return new ProviderManager(daoAuthenticationProvider);
	}

	@Bean
	CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration corsConfiguration = new CorsConfiguration();
		corsConfiguration.addAllowedOrigin("*");
		corsConfiguration.addAllowedMethod("*");
		corsConfiguration.addAllowedHeader("*");
		// corsConfiguration.setExposedHeaders(List.of("x-auth-token"));
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", corsConfiguration);
		return source;
	}
}
