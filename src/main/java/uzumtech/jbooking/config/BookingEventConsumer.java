package uzumtech.jbooking.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import uzumtech.jbooking.component.adapter.JNotificationAdapter;
import uzumtech.jbooking.constant.Constant;
import uzumtech.jbooking.dto.BookingCreatedEvent;

@Slf4j
@Component
@RequiredArgsConstructor
public class BookingEventConsumer {

    private final JNotificationAdapter notificationAdapter;

    @KafkaListener(topics = Constant.TOPIC_BOOKING_CREATED, groupId = "jbooking-notifications")
    public void handleBookingCreated(BookingCreatedEvent event) {

        log.info("Received booking.created: bookingId={}, roomId={}, userId={}",
                event.bookingId(), event.roomId(), event.userId());

        notificationAdapter.sendBookingConfirmation(
                event.userId(),
                "Your booking " + event.bookingId() + " has been created. " +
                        "Check-in: " + event.checkInDate() + ", Check-out: " + event.checkOutDate() + ". " +
                        "Please complete payment to confirm your reservation."
        );
    }
}