package ac.uk.bristol.cs.santa.grotto.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {


    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    //
    @Autowired
    private DataSource dataSource;


    public static final String REALM_NAME = "SPE";

    @Override
    protected void configure(AuthenticationManagerBuilder auth)
            throws Exception {
        auth.
                jdbcAuthentication()
                .usersByUsernameQuery("select username, password, enabled from users where username=?")
                .authoritiesByUsernameQuery("select username, role from users where username=?")
                .dataSource(dataSource)
                .passwordEncoder(bCryptPasswordEncoder);
//        auth.inMemoryAuthentication()
//                .withUser("user").password("password").roles("USER")
//                .and()
//                .withUser("manager").password("password").roles("MANAGER");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http
                .authorizeRequests()
                // make static resources available
                .antMatchers("/css/**", "/images/**", "/webjars/**").permitAll()
                // allow browsing index
                .antMatchers("/", "/terms", "/contact").permitAll()
                .antMatchers("/api/**").permitAll()
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

    @Configuration
    @Order(1)
    public static class ApiWebSecurityConfigurationAdapter extends WebSecurityConfigurerAdapter {

        @Autowired
        private AuthenticationEntryPoint authEntryPoint;

        @Override
        protected void configure(AuthenticationManagerBuilder auth)
                throws Exception {
            auth.
                    jdbcAuthentication()
                    .usersByUsernameQuery("select username, password, enabled from users where username=?")
                    .authoritiesByUsernameQuery("select username, role from users where username=?")
                    .dataSource(dataSource)
                    .passwordEncoder(bCryptPasswordEncoder);
//        auth.inMemoryAuthentication()
//                .withUser("user").password("password").roles("USER")
//                .and()
//                .withUser("manager").password("password").roles("MANAGER");
        }

        @Autowired
        private BCryptPasswordEncoder bCryptPasswordEncoder;
        //
        @Autowired
        private DataSource dataSource;

        @Override
        protected void configure(HttpSecurity http) throws Exception {


            http.csrf().disable();
            // the ant matcher is what limits the scope of this configuration.
            http.antMatcher("/api/**").authorizeRequests()
                    .antMatchers("/api/**").authenticated()
                    .and().httpBasic().realmName(REALM_NAME)
                    .authenticationEntryPoint(authEntryPoint);
        }
    }
}

