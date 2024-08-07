package org.rgomez.springcloud.msvc.usuarios;

import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@EnableWebSecurity
public class SecurityConfig {

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(authorize -> authorize.requestMatchers("/authorized").permitAll()
                        .requestMatchers(HttpMethod.GET, "/").hasAnyAuthority("SCOPE_read", "SCOPE_write")
                        .requestMatchers(HttpMethod.POST, "/").hasAuthority("SCOPE_write")
                        .requestMatchers(HttpMethod.PUT, "/{id}").hasAuthority("SCOPE_write")
                        .requestMatchers(HttpMethod.DELETE, "/{id}").hasAuthority("SCOPE_write")
                        .anyRequest().authenticated())
                .sessionManagement(sessionManagement -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .oauth2Login(oau2Login -> oau2Login.loginPage("/oauth2/authorization/msvc-usuarios-client"))
                .oauth2Login(withDefaults())
                .oauth2ResourceServer().jwt();

        return http.build();
    }

}
