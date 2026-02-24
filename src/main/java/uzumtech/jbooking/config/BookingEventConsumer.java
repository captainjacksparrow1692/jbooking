package uzumtech.jbooking.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import uzumtech.jbooking.constant.Constant;
import uzumtech.jbooking.dto.request.BookingCreateRequest;

@Slf4j
@Component
public class BookingEventConsumer {

    @KafkaListener(topics = Constant.TOPIC_BOOKING_CREATED, groupId = "jbooking-notifications")
    public void handleBookingCreated(BookingCreateRequest request) {
        log.info("Received booking.created: bookingId={}, roomId={}, userId={}",
                request.bookingId(), request.roomId(), request.userId());
        // TODO: отправить email/SMS, обновить аналитику и т.д.
    }
}
