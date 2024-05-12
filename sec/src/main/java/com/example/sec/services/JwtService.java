package com.example.sec.services;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {
  public static final String SECRET = "D6E8A9791B6F0CE7E578AE04D3680713FDE7CFC38A09DBDEF5C0A7816DB1B2CA";


  //getUsernameFromToken
  public String extractUsername(String token) {
    return extractClaim(token, Claims::getSubject);
  }

  //wyodrębnia datę z tokenu
  public Date extractExpiration(String token) {
    return extractClaim(token, Claims::getExpiration);
  }

  //wyodrębnia dane z tokenu jwt
  public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
    final Claims claims = extractAllClaims(token);
    return claimsResolver.apply(claims);
  }

  private Claims extractAllClaims(String token) {
    return Jwts
      .parser()
      .verifyWith(getSignKey())
      .build().parseSignedClaims(token).getPayload();
  }

  //sprawdza czy token wygasł
  private Boolean isTokenExpired(String token) {
    return extractExpiration(token).before(new Date());
  }

  public Boolean validateToken(String token, UserDetails userDetails) {
    final String username = extractUsername(token);
    //czy zgodny username zawarty w tokenie i czy token nie wygasł
    return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
  }


  public String generateToken(String userName){
    Map<String,Object> claims=new HashMap<>();
    return createToken(claims,userName);
  }

  private String createToken(Map<String, Object> claims, String userName) {
    return Jwts.builder()
      .claims(claims)
      .subject(userName)
      .issuedAt(new Date(System.currentTimeMillis()))
      .expiration(new Date(System.currentTimeMillis()+1000*60*30))
      .signWith(getSignKey()).compact();
  }

  private SecretKey getSignKey() {
    byte[] keyBytes= Decoders.BASE64.decode(SECRET);
    return Keys.hmacShaKeyFor(keyBytes);
  }
}
