package work.szczepanskimichal.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import work.szczepanskimichal.utils.PasswordHashingUtil;
import work.szczepanskimichal.exception.PasswordHashingException;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

@Service
@RequiredArgsConstructor
public class HashingService {

    private final PasswordHashingUtil passwordHashingUtil;

    private static final String HASH_ALGORITHM = "SHA-256";

    public String hashPassword(String password) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance(HASH_ALGORITHM);
            byte[] salt = passwordHashingUtil.getHashingSalt().getBytes();
            messageDigest.update(salt);
            byte[] hashedPassword = messageDigest.digest(password.getBytes());

            return Base64.getEncoder().encodeToString(hashedPassword);
        } catch (NoSuchAlgorithmException e) {
            throw new PasswordHashingException(e);
        }
    }
}
