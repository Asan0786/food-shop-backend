package com.fooddelivery.FoodHouse.Filters;

import ch.qos.logback.core.util.StringUtil;
import com.fooddelivery.FoodHouse.service.UserService;
import com.fooddelivery.FoodHouse.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthentiationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;


    @Autowired
    private UserDetailsService userDetailsService;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final String authHeader = request.getHeader("Authorization");

        if (StringUtils.hasText(authHeader) && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);

              String email ="";
            try {
                email = jwtUtil.extractUsername(token); //might throw exception token expirtion

            }catch(io.jsonwebtoken.ExpiredJwtException e){
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.setContentType("application/josn");
                response.getWriter().write("{\"error\": \"Token expired\"}");
                return; //stop filter chain
            }

            if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = userDetailsService.loadUserByUsername(email);


                if (jwtUtil.validateToken(token, userDetails)) {
                    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails,
                            null, userDetails.getAuthorities());

                    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request)); //add meta data like logged user IP address ex;_. User john logged in from IP 192.168.1.10
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken); //this tells sprnng-> This user is now logged in for this request
                }


            }
        }
          filterChain.doFilter(request,response
































          );
    }
}
