package com.medical.appointment.appointment.Conf;


import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtAuthentificationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;
    private final JwtService jwtService;

//     @Bean
//     public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//         http
//             .csrf(csrf -> csrf.disable())
//             .authorizeHttpRequests(auth -> auth
//                 // Pages publiques
//                 .requestMatchers("/", "/home", "/auth/**", "/css/**", "/js/**", "/images/**","/auth/register/**","/favicon.ico").permitAll()
//                 .requestMatchers("/error", "/fragments/**", "/css/patient/**").permitAll()
//                 .requestMatchers("/doctor/**").permitAll()
//                 .requestMatchers("/rendezvous/**").hasAnyRole("ASSISTANCE", "PATIENT", "ADMIN")
//                 // test gestion medecin 
//                 .requestMatchers("/doctor/appointments").permitAll()
//                 // Gestion des rendez-vous
              
//                 .requestMatchers(
//                     "/assistance/**", // Autorise toutes les URLs sous /assistance/
//                     "/assistance/home" // Optionnel, déjà couvert par /**
//                 ).hasRole("ASSISTANCE")
//                 // Gestion des patients
//                 .requestMatchers("/patients/**").hasRole("PATIENT")
//                 .requestMatchers("/patient/appointments").hasRole("PATIENT") // <-- Rôle PATIENT requis
             
          
                
              
                
//                 .anyRequest().authenticated()
//             )
//             // .formLogin(form -> form
//             //     .loginPage("/auth/login")
//             //     .defaultSuccessUrl("/home", true)
//             //     .permitAll()
//             // )
//             .logout(logout -> logout
//                 .logoutUrl("/logout")
//                 .addLogoutHandler((request, response, authentication) -> {
//                     String token = jwtService.extractTokenFromRequest(request);
//                     if (token != null) {
//                         jwtService.revokeToken(token); 
//                     }
//                 })
//                 .logoutSuccessUrl("/auth/login")
//                 .invalidateHttpSession(true)
//                 .deleteCookies("JWT")
//                 .permitAll()
//             )
//             .sessionManagement(session -> session
//                 .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//                 .invalidSessionUrl("/auth/login")
//             )
//             .authenticationProvider(authenticationProvider)
//             .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
//            // Désactiver CSRF pour les requêtes d'API REST
//         return http.build();
//     }
// }

@Bean
public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
        .csrf(csrf -> csrf.disable())
        .authorizeHttpRequests(auth -> auth
            // Public pages and static resources
            .requestMatchers("/", "/home", "/auth/**", "/favicon.ico", "/css/**", "/js/**","/error", "/images/**").permitAll()
            .requestMatchers("/error", "/fragments/**", "/css/patient/**").permitAll()
            .requestMatchers("/doctor/**").permitAll()
            // Role-based access control
            .requestMatchers("/rendezvous/**").hasAnyRole("ASSISTANCE", "PATIENT", "ADMIN")
            .requestMatchers("/assistance/**").hasRole("ASSISTANCE")
            .requestMatchers("/patients/**", "/patient/appointments").hasRole("PATIENT")
            .requestMatchers("/doctor/assistance").authenticated()
            .requestMatchers("/doctor/assistance").hasRole("DOCTOR")
            .anyRequest().authenticated()
            

        )
        .logout(logout -> logout
            .logoutUrl("/logout")
            .addLogoutHandler((request, response, authentication) -> {
                String token = jwtService.extractTokenFromRequest(request);
                if (token != null) {
                    jwtService.revokeToken(token);
                }
            })
            .logoutSuccessUrl("/auth/login")
            .invalidateHttpSession(true)
            .deleteCookies("JWT")
            .permitAll()
        )
        .sessionManagement(session -> session
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .invalidSessionUrl("/auth/login")
        )
        .authenticationProvider(authenticationProvider)
        .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

    return http.build();
}
}