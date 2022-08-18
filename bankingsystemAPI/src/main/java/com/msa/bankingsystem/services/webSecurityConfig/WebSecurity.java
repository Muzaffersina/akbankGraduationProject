package com.msa.bankingsystem.services.webSecurityConfig;

import java.io.IOException;
import java.util.Arrays;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.msa.bankingsystem.services.user.IUserEntityService;

@Configuration
@EnableWebSecurity
public class WebSecurity extends WebSecurityConfigurerAdapter {

	private static final AuthenticationFailureHandler CustomAuthenticationFailureHandler = null;
	private AuthorizationFilter authorizationFilter;
	private IUserEntityService userService;
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@Autowired
	public WebSecurity(IUserEntityService userService, AuthorizationFilter authorizationFilter,
			BCryptPasswordEncoder bCryptPasswordEncoder) {
		this.userService = userService;
		this.authorizationFilter = authorizationFilter;
		this.bCryptPasswordEncoder = bCryptPasswordEncoder;
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {

		http.csrf().disable();

		http.cors();

		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

		http.authorizeRequests().antMatchers("/api/auth", "/api/register").permitAll();

		http.authorizeRequests().antMatchers(HttpMethod.GET, "/api/account/**", "/api/user", "/api/accounts/**")
				.hasAuthority("CREATE_ACCOUNT");

		http.authorizeRequests().antMatchers(HttpMethod.DELETE, "/api/account/**").hasAuthority("REMOVE_ACCOUNT");

		http.authorizeRequests().antMatchers(HttpMethod.POST, "/api/bank").hasAuthority("CREATE_BANK");

		http.authorizeRequests().antMatchers(HttpMethod.PATCH, "/api/user/**").hasAuthority("ACTIVATE_DEACTIVATE_USER");

		http.authorizeRequests().anyRequest().authenticated();

		http.exceptionHandling().accessDeniedHandler(new AccessDeniedHandler() {

			@Override
			public void handle(HttpServletRequest request, HttpServletResponse response,
					AccessDeniedException accessDeniedException) throws IOException, ServletException {
				response.sendError(403, "Access Denied");

			}
		});

		http.addFilterBefore(authorizationFilter, UsernamePasswordAuthenticationFilter.class);
		http.formLogin().disable();
	}

	@Override
	protected void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
		authenticationManagerBuilder.userDetailsService(this.userService).passwordEncoder(bCryptPasswordEncoder);
	}

	@Override
	@Bean
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	/*
	@Bean
	CorsConfigurationSource corsConfigurationSource() {
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", new CorsConfiguration().applyPermitDefaultValues());
		return source;
	}
*/
}
