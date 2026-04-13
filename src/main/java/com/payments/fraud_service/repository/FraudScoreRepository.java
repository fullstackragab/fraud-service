package com.payments.fraud_service.repository;

import com.payments.fraud_service.domain.FraudScore;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FraudScoreRepository extends JpaRepository<FraudScore, Long> {
    Optional<FraudScore> findByPaymentId(String paymentId);
}
