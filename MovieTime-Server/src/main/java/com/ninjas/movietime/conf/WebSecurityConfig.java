package com.ninjas.movietime.conf;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * @author ayassinov on 05/09/2014.
 */
@Configuration
@EnableWebSecurity
@Order(Ordered.LOWEST_PRECEDENCE - 6)
@EnableGlobalMethodSecurity(prePostEnabled = true)
//@ConditionalOnProperty(prefix = "movietime", value = "enableSecurity")
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final static String[] SECURED_PATH = {"/manage/**"};
    private final static String[] OPEN_PATH = {"/", "/api/**"};

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests().
                antMatchers(OPEN_PATH).permitAll()
                .antMatchers(SECURED_PATH).hasRole("MANAGER")
                .anyRequest().authenticated();
        http.httpBasic().realmName("MovieTimeAPI");
    }

    @Override
    protected void configure(AuthenticationManagerBuilder authManagerBuilder) throws Exception {
        authManagerBuilder.inMemoryAuthentication()
                .withUser("manager")
                .password("manager")
                .roles("MANAGER");
    }
}
