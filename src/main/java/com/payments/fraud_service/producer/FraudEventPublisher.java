package com.payments.fraud_service.producer;

import com.payments.fraud_service.domain.FraudScore;
import com.payments.fraud_service.event.FraudResultEvent;
import com.payments.fraud_service.event.PaymentEvent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class FraudEventPublisher {

    private static final String TOPIC = "fraud.results";

    private final KafkaTemplate<String, FraudResultEvent> kafkaTemplate;

    public FraudEventPublisher(KafkaTemplate<String, FraudResultEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publish(FraudScore score, PaymentEvent originalEvent) {
        String eventType = score.getResult().equals("CLEARED")
                ? "FRAUD_CLEARED" : "FRAUD_FLAGGED";

        FraudResultEvent event = new FraudResultEvent(
                eventType,
                score.getPaymentId(),
                score.getRiskLevel(),
                score.getRiskScore(),
                score.getReason(),
                score.getAmount(),
                score.getCurrency(),
                LocalDateTime.now()
        );

        kafkaTemplate.send(TOPIC, score.getPaymentId(), event);
        System.out.println("[FRAUD] Published " + eventType
            + " for payment " + score.getPaymentId());
    }
}
