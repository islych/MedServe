package com.medical.appointment.appointment.Conf;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
// import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;

// import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.medical.appointment.appointment.Model.RevokedToken;
import com.medical.appointment.appointment.Repository.RevokedTokenRepository;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;


@AllArgsConstructor
@Service
public class JwtService {



    private static final String SECRET_KEY = "404E635266556A586E3272357538782F413F442B472B4B6250645367566B5970";
    private final RevokedTokenRepository revokedTokenRepository;
   
    private Claims exctractAllClaims(String token){
        return Jwts.parserBuilder().setSigningKey(getSignInKey()).build().parseClaimsJws(token).getBody();
    }
    private Key getSignInKey(){
        byte[] keybyte = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keybyte);
    }

    public <T> T exctractClaims(String token, Function<Claims,T>claimsResolver) {
        final Claims claims = exctractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public String extractusername(String Tokens){
        return exctractClaims(Tokens, Claims::getSubject);
    }

    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("sub", userDetails.getUsername());
        claims.put("authorities", userDetails.getAuthorities().stream() // Ajoutez les autorités
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList()));
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60)) // 1h
                .signWith(getSignInKey())
                .compact();
    }
  
    public boolean isTokenValid(String token, UserDetails userDetails){
        final String username = extractusername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    
    }
    public boolean isTokenExpired(String token){
        return extractExpiration(token).before(new Date());
    }
    public Date extractExpiration(String token){
        return exctractClaims(token, Claims::getExpiration);
    }
    public String extractTokenFromRequest(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7); // Supprime "Bearer "
        }

        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("JWT".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }

        return null;
    }
    public void revokeToken(String token) {
        RevokedToken revokedToken = new RevokedToken();
        revokedToken.setToken(token);
        revokedToken.setExpirationDate(extractExpiration(token));
        revokedTokenRepository.save(revokedToken);
    }

}
