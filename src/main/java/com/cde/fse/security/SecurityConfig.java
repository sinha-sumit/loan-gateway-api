package com.cde.fse.security;

import javax.servlet.http.HttpServletResponse;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.cde.fse.security.jwt.AuthTokenFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Bean
	public AuthTokenFilter authenticationJwtTokenFilter() {
		return new AuthTokenFilter();
	}
	

	 @Override
	    protected void configure(HttpSecurity httpSecurity) throws Exception {
	        httpSecurity
	                .csrf().disable()
	                .logout().disable()
	                .formLogin().disable()
	                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
	                .and()
	                    .anonymous()
	                .and()
	                    .exceptionHandling().authenticationEntryPoint(
	                            (req, rsp, e) -> rsp.sendError(HttpServletResponse.SC_UNAUTHORIZED))
	                .and()
	                    .addFilterAfter(authenticationJwtTokenFilter(),
	                            UsernamePasswordAuthenticationFilter.class)
	                .authorizeRequests()
	                    .antMatchers("/login/V1", "/search/**").permitAll()
	                    .antMatchers("/save/**", "/update/**").hasRole("ADMIN")
	                    .anyRequest().authenticated();
	    }
}
