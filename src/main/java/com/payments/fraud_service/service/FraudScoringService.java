package com.payments.fraud_service.service;

import com.payments.fraud_service.domain.FraudScore;
import com.payments.fraud_service.event.PaymentEvent;
import com.payments.fraud_service.repository.FraudScoreRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class FraudScoringService {

    private final FraudScoreRepository fraudScoreRepository;

    public FraudScoringService(FraudScoreRepository fraudScoreRepository) {
        this.fraudScoreRepository = fraudScoreRepository;
    }

    @Transactional
    public FraudScore score(PaymentEvent event) {

        return fraudScoreRepository.findByPaymentId(event.paymentId())
                .orElseGet(() -> computeAndSave(event));
    }

    private FraudScore computeAndSave(PaymentEvent event) {
        int score = 0;
        String reason = "No issues detected";

        if (event.amount().compareTo(new BigDecimal("10000")) > 0) {
            score += 60;
            reason = "High value transaction";
        }

        if(event.amount().compareTo(new BigDecimal("5000")) > 0
            && event.amount().compareTo(new BigDecimal("10000")) <= 0) {
            score += 30;
            reason = "Medium value transaction";
        }

        if(!event.currency().equals("USD")) {
            score += 20;
            reason = reason + " | Non-USD currency";
        }

        String riskLevel;
        String result;

        if(score >= 70) {
            riskLevel = "HIGH";
            result = "FLAGGED";
        } else if(score >= 40) {
            riskLevel = "MEDIUM";
            result = "FLAGGED";
        } else {
            riskLevel = "LOW";
            result = "CLEARED";
        }

        System.out.println("[FRAUD] Scored payment " + event.paymentId()
            + " -> score=" + score + " level=" + riskLevel + " result=" + result);

        FraudScore fraudScore = new FraudScore(
                event.paymentId(), score, riskLevel,
                reason, event.amount(), event.currency(), result
        );

        return fraudScoreRepository.save(fraudScore);
    }
}
