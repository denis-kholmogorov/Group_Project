package project.services;

import org.springframework.stereotype.Service;
import project.models.VerificationToken;
import project.repositories.VerificationTokenRepository;

@Service
public class VerificationTokenService {

    private VerificationTokenRepository verificationTokenRepository;

    public VerificationTokenService(VerificationTokenRepository verificationTokenRepository) {
        this.verificationTokenRepository = verificationTokenRepository;
    }

    public VerificationToken findByUUID(String token) {
        return verificationTokenRepository.findByUuid(token).orElse(null);
    }

    public void save(VerificationToken token){
        verificationTokenRepository.save(token);
    }
}
