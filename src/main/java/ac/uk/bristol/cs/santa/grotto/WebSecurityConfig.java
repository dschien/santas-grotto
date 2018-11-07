package ac.uk.bristol.cs.santa.grotto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

/**
 * Created by csxds on 30/11/2017.
 */
//@Configuration
//@EnableWebSecurity
public class WebSecurityConfig
//        extends WebSecurityConfigurerAdapter {
    {

//    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                // make static resources available
                .antMatchers("/css/**", "/images/**", "/webjars/**").permitAll()
                // allow browsing index
                .antMatchers("/", "/terms", "/contact").permitAll()
                .anyRequest().authenticated()
                .and()
                // configure login
                .formLogin()
                .loginPage("/login")
                .failureUrl("/login-error.html")
                .permitAll()
                .and()
                .logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .logoutSuccessUrl("/")
                .permitAll();


    }

//    @Override
    public void configure(WebSecurity web) throws Exception {
        web
                .ignoring()
                .antMatchers("/resources/**"); // #3
    }




}