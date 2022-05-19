package com.blur.cryptobank.config;

import com.blur.cryptobank.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;


/**
 * This the security configuration class. It is used to configure the different levels of authorization based on path.
 * The application used a custom authentication based on UserEntity which implements the UserDetails interface.
 * The application uses the BCryptPasswordEncoder to hash the password.
 * The application uses the DaoAuthenticationProvider to authenticate the user.
 * There are two user roles: USER and ADMIN. These roles are used to determine the authorization level.
 *
 */
@Configuration
@AllArgsConstructor
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {


    private final UserService userService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/cryptos","/register","/login", "/", "confirm", "/error/**", "/getCryptos").permitAll()
                .antMatchers("/users").hasRole("ADMIN")
                .antMatchers("/user/**", "/cryptos/trade/", "/logout", "/cryptos/transfer").hasAnyRole("USER", "ADMIN")
                .anyRequest().authenticated().and()
                .formLogin(form -> form.defaultSuccessUrl("/").loginPage("/login").failureUrl("/login?error=true"))
                .logout(logout -> logout.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                        .logoutSuccessUrl("/")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                )
                .exceptionHandling().accessDeniedPage("/error/forbidden");

    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(daoAuthenticationProvider());
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider provider =
                new DaoAuthenticationProvider();
        provider.setPasswordEncoder(bCryptPasswordEncoder);
        provider.setUserDetailsService(userService);
        return provider;
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/resources/**", "/static/**", "/css/**", "/js/**", "/images/**");
    }

}


