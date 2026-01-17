package TienToan.example.Cinema.config;

import TienToan.example.Cinema.Entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Date;
import java.util.function.Function;

@Service
@Slf4j
public class JwtUntils {
    @Value("${jwt.secret}")
    private String signerKey;

    @Value("${jwt.expiration}")
    private Long expiration;

    private SecretKey secretKey;

    @PostConstruct
    public void init(){
        secretKey = new SecretKeySpec(signerKey.getBytes(), "HmacSHA256");
    }

    //taotoken
    public String generateToken(User user){
        return Jwts.builder()
                .subject(user.getName())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(secretKey)
                .claim("role" , user.getRole())
                .compact();
    }

    public String getUserFromUser(String token){
        return extractClaim(token,Claims::getSubject);
    }


    public <T> T extractClaim(String token , Function<Claims , T> claimsTFunction){
        return claimsTFunction.apply(Jwts
                .parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()
        );
    }

    public Boolean validateToken(String token , UserDetails userDetails){
        final String username = getUserFromUser(token);
        return ( username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    public Boolean isTokenExpired(String token){
        return extractClaim(token , Claims::getExpiration).before(new Date());
    }
}
