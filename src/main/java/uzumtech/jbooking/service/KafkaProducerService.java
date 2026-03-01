package uzumtech.jbooking.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import uzumtech.jbooking.constant.Constant;
import uzumtech.jbooking.dto.BookingCreatedEvent;

import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaProducerService {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void sendBookingCreated(BookingCreatedEvent request) {
        CompletableFuture<SendResult<String, Object>> future = kafkaTemplate.send(
                Constant.TOPIC_BOOKING_CREATED,
                String.valueOf(request.bookingId()),
                request
        );
        future.whenComplete((result, ex) -> {
            if (ex != null) {
                log.error("Failed to send booking.created event: {}", ex.getMessage());
            } else {
                log.debug("Sent booking.created: bookingId={}", request.bookingId());
            }
        });
    }
}
