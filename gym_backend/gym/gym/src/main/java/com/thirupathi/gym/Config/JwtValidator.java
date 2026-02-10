package com.thirupathi.gym.Config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.util.List;

import static com.thirupathi.gym.Config.JwtProvider.JWT_SECRET;


@Component
public class JwtValidator extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String jwt = null;
        //System.out.println(" Request Cookies : " + request.getCookies());
        if(request.getCookies() != null){
            for(Cookie cookie : request.getCookies()){
                if(cookie.getName().equals("access_token")){
                    jwt = cookie.getValue();
                   // System.out.println("jwt " + jwt);
                }
            }
        }
        //System.out.println(" Requested Headers : " + request.getHeader("Authorization"));
        if(jwt == null){
            String header = request.getHeader("Authorization");
            if(header != null && header.startsWith("Bearer ")){
                jwt = header.substring(7);
                //System.out.println("JWT from Authorization Header = " + jwt);
            }
        }

        try{
            if(jwt != null){
                SecretKey key = Keys.hmacShaKeyFor(JWT_SECRET.getBytes());

                Claims claim = Jwts.parser()
                        .verifyWith(key)
                        .build()
                        .parseSignedClaims(jwt)
                        .getPayload();

                String userName = String.valueOf(claim.getSubject());
                String role = String.valueOf(claim.get("role"));

                //System.out.println("ROLE From Token = " + role);


                List<GrantedAuthority> authority = AuthorityUtils.commaSeparatedStringToAuthorityList(role);

                Authentication auths = new UsernamePasswordAuthenticationToken( userName, null, authority);

                SecurityContextHolder.getContext().setAuthentication(auths);
            }
        }catch(Exception err){
            System.out.println( err.getMessage());
        }

        filterChain.doFilter(request, response);
    }
}
