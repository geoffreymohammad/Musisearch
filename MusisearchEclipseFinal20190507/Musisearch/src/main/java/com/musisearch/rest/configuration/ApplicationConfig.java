package com.musisearch.rest.configuration;


import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.encoding.ShaPasswordEncoder;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.musisearch.rest.repository.AdminRepo;



@Configuration
@EnableWebSecurity
public class ApplicationConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired
	AdminRepo adminRepo;
		
	private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
	
	@Override
    protected void configure(HttpSecurity http) throws Exception {
		http
	        .csrf()
			.disable()
			.authorizeRequests()
	        .antMatchers("/bootstrap/**", "/dist/**", "/plugins/**")
	        .permitAll()
	        .antMatchers("/v2/api-docs", "/configuration/ui", "/swagger-resources", "/configuration/security", 
	        		"/swagger-ui.html", "/webjars/**","/swagger-resources/configuration/ui","/swagger-ui.html")
	        .permitAll() 
	        .anyRequest()
	        .authenticated()
	        .and()
	    .formLogin()
	        .failureUrl("/login?error")
	        .loginPage("/login")
	        .successHandler(new AuthenticationSuccessHandler() {
	            @Override
	            public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
	                    Authentication authentication) throws IOException, ServletException {
	                redirectStrategy.sendRedirect(request, response, "/api/admin/list?pageNumber=1");
	            }
	        })
	        .permitAll()
	        .and()
	    .logout()
	        .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
	        .logoutSuccessUrl("/login")
	        .permitAll();
    }
	
	@Bean
    public ShaPasswordEncoder passwordEncoder() {
		ShaPasswordEncoder bCryptPasswordEncoder = new ShaPasswordEncoder();
        return bCryptPasswordEncoder;
    }
		
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		//String password = passwordEncoder().encode("4553ts4ndroid");
		//auth.inMemoryAuthentication().withUser("dimas.aprizawandi").password("pass").roles("ADMIN");
		auth.userDetailsService(adminRepo).passwordEncoder(passwordEncoder());
		
	}
	
	@Override
	public void configure(WebSecurity web) throws Exception {
		web
	       .ignoring()
	       .antMatchers("/api/**/rest/**", "/batchsp/**", "/opname/**", "/resources/**", "/static/**", "/css/**",
					"/js/**", "/images/**", "/fonts/**", "/v2/api-docs/**", "/configuration/ui", "/swagger-resources/**",
					"/configuration/**", "/swagger-ui.html", "/webjars/**");
	       //.antMatchers("/assetsp/**","/batchsp/**","/opname/**","/resources/**", "/static/**", "/css/**", "/js/**", "/images/**", "/fonts/**", "/v2/api-docs/**", "/configuration/ui", "/swagger-resources/**", "/configuration/**", "/swagger-ui.html", "/webjars/**");
	}
	
	

}
