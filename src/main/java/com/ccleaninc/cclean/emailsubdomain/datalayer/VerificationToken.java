package com.ccleaninc.cclean.emailsubdomain.datalayer;

import jakarta.persistence.*;
import lombok.*;

import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

@Table(name = "verification_token")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
public class VerificationToken {
    private static final int EXPIRATION = 15;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String token;

    private String email;

    private String customerId;

    private Date expiryDate;

    public VerificationToken(String customerId, String email) {
        this.token = UUID.randomUUID().toString();
        this.customerId = customerId;
        this.email = email;
        Calendar date = Calendar.getInstance();
        long timeInSecs = date.getTimeInMillis();
        this.expiryDate = new Date(timeInSecs + (EXPIRATION * 60 * 1000));
    }
}
