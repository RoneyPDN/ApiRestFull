package com.api.rest.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.AnyRequestMatcher;

import com.api.rest.service.ImplementacaoUserDetailsService;

@Configuration
@EnableWebSecurity
public class WebConfigSecurity extends WebSecurityConfigurerAdapter{

	@Autowired
	private ImplementacaoUserDetailsService implementacaoUserDetailsService;
	
	protected void configure(HttpSecurity http) throws Exception {
	
		http.csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
		.disable().authorizeRequests().antMatchers("/").permitAll()
		.antMatchers("/index").permitAll()
		.antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
		.anyRequest().authenticated().and().logout().logoutUrl("/index")
		.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
		
		/*Filtra requisições do login para requisição*/
		.and().addFilterBefore(new JWTLoginFilter("/login", authenticationManager()), UsernamePasswordAuthenticationFilter.class)
		/*Filtra demais requisições  para verificar a presença do Token JWT no header HTTP*/
		.addFilterBefore(new JWTApiAutenticacaoFilter(), UsernamePasswordAuthenticationFilter.class);
	}
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {

		auth.userDetailsService(implementacaoUserDetailsService).passwordEncoder(new BCryptPasswordEncoder());
		
	}
}
