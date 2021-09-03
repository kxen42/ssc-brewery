package guru.sfg.brewery.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
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

    public RestHeaderAuthFilter restHeaderAuthFilter(AuthenticationManager authenticationManager) {
        // set the required things for this filter
        RestHeaderAuthFilter filter = new RestHeaderAuthFilter(new AntPathRequestMatcher("/api/**"));
        filter.setAuthenticationManager(authenticationManager);
        return filter;
    }

    public RestParameterAuthFilter restParameterAuthFilter(AuthenticationManager authenticationManager) {
        // set the required things for this filter
        RestParameterAuthFilter filter = new RestParameterAuthFilter(new AntPathRequestMatcher("/api/**"));
        filter.setAuthenticationManager(authenticationManager);
        return filter;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.addFilterBefore(restHeaderAuthFilter(authenticationManager()), UsernamePasswordAuthenticationFilter.class)
            .csrf().disable();

        http.addFilterBefore(restParameterAuthFilter(authenticationManager()), UsernamePasswordAuthenticationFilter.class);

        http
            .authorizeRequests(authorize ->
                authorize
                    .antMatchers("/", "/webjars/**", "/login", "/resources/**").permitAll()
                    .antMatchers("/beers/find", "/beers*").permitAll()
                    .antMatchers(HttpMethod.GET, "/api/v1/beer/**").permitAll()
                    .mvcMatchers(HttpMethod.GET, "/api/v1/beerUpc/{upc}").permitAll()
            )
            .authorizeRequests() // all the paths
            .anyRequest().authenticated()
            .and()
            .formLogin().and()
            .httpBasic();
    }

    /*
     * Overriding the usersDetailsService means that the {@code spring.security.user.name} and {@code spring.security.user.password} values in the {@code application.properties} file
     * are not used.
     * @return spring/guru ADMIN UserDetails, user/password USER UserDetails
     */
    /**
     * Using the Fluent API to setup the in-memory authentication.
     * <p>
     * Here we use the {@code noop} password encoder - you have to provide a password encoder.
     * Thee syntax is {@code .password("{noop}password")}.
     * <p>
     * User/Password
     * <ul>
     *     <li>admin/guru</li>
     *     <li>user/password</li>
     *     <li>scott/tiger</li>
     * </ul>
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
            .withUser("admin")
            .password("{bcrypt}$2a$10$.yhSATXz7lB/57umk14iueihgiI92vrCQf2Mjm78.cllOA1DuIs0i")
            .roles("ADMIN")
            .and()
            .withUser("user")
            .password("{sha256}bc8b31352fd578b08be6277307876eaae7f2ba62f4303ecd281036f3ce440abdf372801750a409ff")
            .roles("USER")
            .and()
            .withUser("scott")
            .password("{bcrypt15}$2a$15$nkei0V6tzJnQ85eWOXasAOMuX/jS48PY9wzd6VnAui/GidkbbDeIq")
            .roles("CUSTOMER");
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return SfgPasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}
