package com.payments.fraud_service.consumer;

import com.payments.fraud_service.domain.FraudScore;
import com.payments.fraud_service.event.PaymentEvent;
import com.payments.fraud_service.producer.FraudEventPublisher;
import com.payments.fraud_service.service.FraudScoringService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class PaymentEventConsumer {

    private final FraudScoringService fraudScoringService;
    private final FraudEventPublisher fraudEventPublisher;

    public PaymentEventConsumer(FraudScoringService fraudScoringService,
                                FraudEventPublisher fraudEventPublisher) {
        this.fraudScoringService = fraudScoringService;
        this.fraudEventPublisher = fraudEventPublisher;
    }

    @KafkaListener(topics = "payment.events", groupId = "fraud-scoring-service")
    public void onPaymentEvent(PaymentEvent event) {

        if (!event.eventType().equals("PAYMENT_INITIATED")) {
            return;
        }

        System.out.println(
                "[FRAUD] Received payment: " + event.paymentId() + " amount=" + event.amount() + " " + event.currency()
        );

        FraudScore score = fraudScoringService.score(event);
        fraudEventPublisher.publish(score, event);
    }
}
