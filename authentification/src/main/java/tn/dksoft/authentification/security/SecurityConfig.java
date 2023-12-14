package tn.dksoft.authentification.security;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
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
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import tn.dksoft.authentification.entity.AppUser;
import tn.dksoft.authentification.filter.JwtAuthenticationFilter;
import tn.dksoft.authentification.filter.JwtAutorizationFilter;
import tn.dksoft.authentification.service.UserServiceImpl;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

	@Autowired
	private UserServiceImpl userServiceImpl;

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
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

		http.sessionManagement((session) -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
		http.csrf(AbstractHttpConfigurer::disable);
		http.formLogin(Customizer.withDefaults());

		http.authorizeHttpRequests((authz) -> authz
				.requestMatchers(new AntPathRequestMatcher("/refreshToken/**", "/login/**")).permitAll());

		http.authorizeHttpRequests((req) -> req.requestMatchers("/delete").hasRole("admin"));

		http.authorizeHttpRequests((authz) -> authz.anyRequest().authenticated());

		http.addFilter(new JwtAuthenticationFilter(
				authenticationManager(http.getSharedObject(AuthenticationConfiguration.class))));
		http.addFilterBefore(new JwtAutorizationFilter(), UsernamePasswordAuthenticationFilter.class);

		return http.build();

	}

	@Bean
	AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
			throws Exception {
		return authenticationConfiguration.getAuthenticationManager();
	}

}
