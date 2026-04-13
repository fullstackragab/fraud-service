package com.payments.fraud_service.domain;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name="fraud_scores")
public class FraudScore {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "payment_id", nullable = false, unique = true)
    private String paymentId;

    @Column(name = "risk_score", nullable = false)
    private int riskScore;

    @Column(name = "risk_level", nullable = false)
    private String riskLevel;

    @Column(name = "reason")
    private String reason;

    @Column(name = "amount", precision = 19, scale = 4)
    private BigDecimal amount;

    @Column(name="currency")
    private String currency;

    @Column(name = "result", nullable = false)
    private String result;

    @Column(name = "scored_at", nullable = false)
    private LocalDateTime scoredAt;

    protected FraudScore() {}

    public FraudScore(String paymentId, int riskScore, String riskLevel,
                      String reason, BigDecimal amount, String currency, String result) {
        this.paymentId = paymentId;
        this.riskScore = riskScore;
        this.riskLevel = riskLevel;
        this.reason = reason;
        this.amount = amount;
        this.currency = currency;
        this.result = result;
        this.scoredAt = LocalDateTime.now();
    }

    public String getPaymentId() { return paymentId; }
    public int getRiskScore() { return riskScore; }
    public String getRiskLevel() { return riskLevel; }
    public String getReason() { return reason; }
    public BigDecimal getAmount() { return amount; }
    public String getCurrency() { return currency; }
    public String getResult() { return result; }
    public LocalDateTime getScoredAt() { return scoredAt; }
}
