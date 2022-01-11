package guru.sfg.brewery.config;

import static org.springframework.http.HttpMethod.*;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import guru.sfg.brewery.security.RestHeaderAuthFilter;
import guru.sfg.brewery.security.RestParameterAuthFilter;
import guru.sfg.brewery.security.SfgPasswordEncoderFactories;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();

        http
            .authorizeRequests(authorize ->
                authorize
                    /* Embedded H2 DB */
                    .antMatchers("/h2-console/**").permitAll() // don't use in production
                    /* Web App */
                    .antMatchers("/", "/webjars/**", "/login", "/resources/**").permitAll()
                    .mvcMatchers("/brewery/breweries/**", "/brewery/breweries*").hasAnyRole("ADMIN","CUSTOMER")
                    .mvcMatchers(GET, "/beers/find", "/beers/{upc}").hasAnyRole("ADMIN", "CUSTOMER", "USER")
                    .mvcMatchers(GET, "/customers/find").hasAnyRole("ADMIN", "CUSTOMER")
                    .mvcMatchers(POST, "/customers/new").hasRole("ADMIN")
                    /* REST API */
                    .mvcMatchers(DELETE, "/api/v1/beer/**").hasRole("ADMIN")
                    .antMatchers(GET, "/api/v1/beer/**").hasAnyRole("ADMIN", "CUSTOMER", "USER")
                    .antMatchers(GET, "/brewery/api/v1/beerUpc/{upc}").hasAnyRole("ADMIN", "CUSTOMER", "USER")
                    .mvcMatchers(GET, "/brewery/api/v1/breweries/**").hasAnyRole("ADMIN","CUSTOMER")
            )
            .authorizeRequests() // all the paths
            .anyRequest().authenticated()
            .and()
            .formLogin().and()
            .httpBasic();

        // for h2 console iframes
        http.headers().frameOptions().sameOrigin();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return SfgPasswordEncoderFactories.createDelegatingPasswordEncoder();
    }


/*
Our JpaUserDetailsService is a @Component that will get wired with the UserRepository via the constructor.
This is only used if the AuthenticationManagerBuilder has not been populated and no AuthenticationProviderBean is defined.
*/


}
