package pl.kubaf2k.langschoolappspring.configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import pl.kubaf2k.langschoolappspring.services.LangschoolUserDetailsService;

@Configuration
public class SecurityConfig {

    @Bean
    public UserDetailsService userDetailsService() {
        return new LangschoolUserDetailsService();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/admin").hasAuthority("ROLE_ADMIN")
                .antMatchers("/teacher").hasAuthority("ROLE_TEACHER")
                .antMatchers("/user/**", "/courses/enroll", "/courses/user").authenticated()
                .antMatchers("/language").hasAuthority("ROLE_ADMIN")
                .antMatchers("/courses/add", "/courses/edit", "/courses/**/edit").hasAnyAuthority("ROLE_TEACHER", "ROLE_ADMIN")
                .anyRequest().permitAll()
                .and().formLogin()
                .permitAll()
                .and().logout();



        return http.build();
    }
}
