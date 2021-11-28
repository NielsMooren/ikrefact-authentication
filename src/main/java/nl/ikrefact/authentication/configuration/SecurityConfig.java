package nl.ikrefact.authentication.configuration;

import nl.ikrefact.authentication.service.JWTAuthenticationFilter;
import nl.ikrefact.authentication.service.JWTAuthorizationFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.http.HttpMethod.*;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserDetailsService userDetailsService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    @Value("${JWT_SECRET}")
    private String jwtSecret;
    private static final String ROLE_ADMIN = "ROLE_ADMIN";
    private static final String ROLE_MOD = "ROLE_MOD";
    private static final String ROLE_USER = "ROLE_USER";

    public SecurityConfig(UserDetailsService userDetailsService, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userDetailsService = userDetailsService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        // Endpoint for token refresh
        http.authorizeRequests().antMatchers(POST, "/api/v1/token/refresh").permitAll();


        http.authorizeRequests().antMatchers(GET, "/api/v1/auth/admin").hasAnyAuthority(ROLE_ADMIN);
        http.authorizeRequests().antMatchers(GET, "/api/v1/auth/mod").hasAnyAuthority(ROLE_MOD, ROLE_ADMIN);
        http.authorizeRequests().antMatchers(GET, "/api/v1/auth/user").hasAnyAuthority(ROLE_USER, ROLE_MOD, ROLE_ADMIN);

        // Endpoints for users (only accessible for admins
        http.authorizeRequests().antMatchers(GET, "/api/v1/users/**").hasAnyAuthority(ROLE_ADMIN);
        http.authorizeRequests().antMatchers(GET, "/api/v1/users").hasAnyAuthority(ROLE_ADMIN);
        http.authorizeRequests().antMatchers(PUT, "/api/v1/users/changepassword").hasAnyAuthority(ROLE_ADMIN, ROLE_MOD);

        http.authorizeRequests().antMatchers(POST, "/api/v1/users").hasAnyAuthority(ROLE_ADMIN);
        http.authorizeRequests().anyRequest().authenticated();
        http.addFilter(new JWTAuthenticationFilter(authenticationManagerBean(), jwtSecret));
        http.addFilterBefore(new JWTAuthorizationFilter(jwtSecret), UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}
