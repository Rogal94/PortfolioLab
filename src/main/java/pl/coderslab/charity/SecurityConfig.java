package pl.coderslab.charity;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.ExceptionMappingAuthenticationFailureHandler;
import pl.coderslab.charity.security.LoginSuccessHandler;
import pl.coderslab.charity.user.SpringDataUserDetailsService;

import java.util.HashMap;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SpringDataUserDetailsService customUserDetailsService() {
        return new SpringDataUserDetailsService();
    }

    @Bean
    public AuthenticationSuccessHandler loginSuccessHandler(){
        return new LoginSuccessHandler();
    }
    @Bean
    public ExceptionMappingAuthenticationFailureHandler loginMappingFailureHandler() {
        ExceptionMappingAuthenticationFailureHandler exceptionMapping = new ExceptionMappingAuthenticationFailureHandler();
        HashMap<String, String> failureUrlMap = new HashMap<String, String>();
        failureUrlMap.put("org.springframework.security.authentication.BadCredentialsException", "/login/error");
        failureUrlMap.put("org.springframework.security.authentication.LockedException", "/login/locked");
        failureUrlMap.put("org.springframework.security.authentication.DisabledException", "/login/disabled");
        failureUrlMap.put("org.springframework.security.authentication.AccountExpiredException", "/reset/password");
        failureUrlMap.put("org.springframework.security.authentication.CredentialsExpiredException", "/login/error");
        failureUrlMap.put("org.springframework.security.authentication.AuthenticationServiceException", "/login/error");
        exceptionMapping.setExceptionMappings(failureUrlMap);
        return exceptionMapping;
    }

    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/password/**","/token/**","/reset/**").permitAll()
                .antMatchers("/form/**").hasAnyRole("USER","ADMIN")
                .antMatchers("/admin/**").hasRole("ADMIN")
                .and()
                .formLogin().loginPage("/login").usernameParameter("email")
                .defaultSuccessUrl("/form")
                .successHandler(loginSuccessHandler())
                .failureHandler(loginMappingFailureHandler())
                .and()
                .logout().logoutSuccessUrl("/").permitAll()
                .and()
                .exceptionHandling().accessDeniedPage("/login");
    }
}
