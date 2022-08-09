package tacos.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import tacos.User;
import tacos.data.UserRepository;

@Configuration
public class SecurityConfig {
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Как вы наверняка помните, интерфейс UserDetailsService определяет
     * только один метод: loadUserByUsername(). То есть это функциональный интерфейс,
     * и его можно реализовать как лямбда-функцию.
     * Поскольку нам нужно, только чтобы наша реализация UserDetailsService
     * делегировала выполнение операций репозиторию UserRepository
     *
     * @param userRepo
     * @return
     */
    @Bean
    public UserDetailsService userDetailsService(UserRepository userRepo) {
        return username -> {
            User user = userRepo.findByUsername(username);
            if (user != null) return user;

            throw new UsernameNotFoundException("User '" + username + "' не найден");

        };
    }

    /**
     * пользователями с подтвержденными
     * привилегиями ROLE_USER. Не включайте префикс ROLE_ в назва-
     * ния ролей, передаваемых в вызов hasRole(), – он подразумева-
     * ется по умолчанию;
     *
     * @param http
     * @return
     * @throws Exception
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeRequests()
                .mvcMatchers("/design", "/orders").access("hasRole('USER')")
                .anyRequest().permitAll()

      .and()
        .formLogin()
          .loginPage("/login")

      .and()
        .logout()
          .logoutSuccessUrl("/")

      // Make H2-Console non-secured; for debug purposes
      .and()
        .csrf()
          .ignoringAntMatchers("/h2-console/**", "/orders")

      // Allow pages to be loaded in frames from the same origin; needed for H2-Console
      .and()
        .headers()
          .frameOptions()
            .sameOrigin()

       .and()
       .build();
  }

}

