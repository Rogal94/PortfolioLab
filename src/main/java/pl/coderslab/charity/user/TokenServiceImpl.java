package pl.coderslab.charity.user;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Service
public class TokenServiceImpl implements TokenService{
    private static final int EXPIRATION = 60 * 24;
    private final TokenRepository tokenRepository;

    public TokenServiceImpl(TokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }

    private String calculateExpiryDate(long expiryTimeInMinutes) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return LocalDateTime.now().plusMinutes(expiryTimeInMinutes).format(formatter);
    }

    @Override
    public Token createToken(User user) {
        Token token = new Token();
        if(tokenRepository.findByUser(user) != null) {
            token.setId(tokenRepository.findByUser(user).getId());
        }
        String tokenStr = UUID.randomUUID().toString();
        token.setToken(tokenStr);
        token.setExpiryDate(calculateExpiryDate(EXPIRATION));
        token.setUser(user);
        tokenRepository.save(token);
        return token;
    }
}
