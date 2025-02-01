package com.ccleaninc.cclean.emailsubdomain.datalayer;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Integer> {

    VerificationToken getVerificationTokenByTokenAndEmail(String token, String email);

    VerificationToken getVerificationTokenByCustomerId(String customerId);

    List<VerificationToken> getAllByCustomerId(String customerId);
}
