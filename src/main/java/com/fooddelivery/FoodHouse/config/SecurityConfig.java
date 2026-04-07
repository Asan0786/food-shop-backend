package com.fooddelivery.FoodHouse.config;


import com.fooddelivery.FoodHouse.Filters.JwtAuthentiationFilter;
import com.fooddelivery.FoodHouse.service.AppUserDetailsService;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.List;

@Configuration
@AllArgsConstructor
public class SecurityConfig {

    private final AppUserDetailsService userDetailsService;


    private final JwtAuthentiationFilter jwtAuthentiationFilter;
//
//    public SecurityConfig(AppUserDetailsService userDetailsService) {
//        this.userDetailsService = userDetailsService;
//    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http)throws Exception{

        http.cors(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth->auth.requestMatchers("/api/register","/api/login","/api/foods/**","/api/orders/all",
                                "/api/orders/status/**").permitAll()
                        .anyRequest().authenticated()).sessionManagement(session->session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                        .addFilterBefore(jwtAuthentiationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();

    }




    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CorsFilter corsFilter(){
        return new CorsFilter(corsConfigurationSource());
    }

    private UrlBasedCorsConfigurationSource corsConfigurationSource(){
        CorsConfiguration config = new CorsConfiguration();

        config.setAllowedOrigins(List.of("http://localhost:5173","http://localhost:3000"));
        config.setAllowedMethods(List.of("GET","POST","DELETE","UPDATE","PUT","PATCH","OPTIONS"));
        config.setAllowedHeaders(List.of("Authorization","Content-Type"));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
         source.registerCorsConfiguration("/**",config);
         return source;


    }



    @Bean
    public AuthenticationManager authenticationManager(UserDetailsService userDetailsService) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(userDetailsService);

        provider.setPasswordEncoder(passwordEncoder());
        return new ProviderManager(provider);
    }
}
