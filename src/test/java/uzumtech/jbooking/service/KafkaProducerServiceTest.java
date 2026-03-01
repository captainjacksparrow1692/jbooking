package uzumtech.jbooking.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import uzumtech.jbooking.constant.Constant;
import uzumtech.jbooking.dto.BookingCreatedEvent;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class KafkaProducerServiceTest {

    @Mock
    KafkaTemplate<String, Object> kafkaTemplate;

    @InjectMocks
    KafkaProducerService kafkaProducerService;

    @Test
    void sendBookingCreated_shouldSendToCorrectTopic() {
        UUID bookingId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        UUID roomId = UUID.randomUUID();

        BookingCreatedEvent  request = new BookingCreatedEvent(
                bookingId, userId, roomId,
                LocalDate.now().plusDays(1), LocalDate.now().plusDays(3),
                2, LocalDateTime.now()
        );

        CompletableFuture<SendResult<String, Object>> future = new CompletableFuture<>();
        future.complete(mock(SendResult.class));

        when(kafkaTemplate.send(
                eq(Constant.TOPIC_BOOKING_CREATED),
                eq(bookingId.toString()),
                eq(request)
        )).thenReturn(future);

        kafkaProducerService.sendBookingCreated(request);

        ArgumentCaptor<String> topicCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> keyCaptor = ArgumentCaptor.forClass(String.class);

        verify(kafkaTemplate).send(topicCaptor.capture(), keyCaptor.capture(), eq(request));

        assertThat(topicCaptor.getValue()).isEqualTo(Constant.TOPIC_BOOKING_CREATED);
        assertThat(keyCaptor.getValue()).isEqualTo(bookingId.toString());
    }

    @Test
    void sendBookingCreated_shouldHandleFailureGracefully() {
        UUID bookingId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        UUID roomId = UUID.randomUUID();

        BookingCreatedEvent  request = new BookingCreatedEvent(
                bookingId, userId, roomId,
                LocalDate.now().plusDays(1), LocalDate.now().plusDays(3),
                2, LocalDateTime.now()
        );

        CompletableFuture<SendResult<String, Object>> future = new CompletableFuture<>();
        future.completeExceptionally(new RuntimeException("Kafka unavailable"));

        when(kafkaTemplate.send(
                eq(Constant.TOPIC_BOOKING_CREATED),
                eq(bookingId.toString()),
                eq(request)
        )).thenReturn(future);

        kafkaProducerService.sendBookingCreated(request);

        verify(kafkaTemplate).send(
                eq(Constant.TOPIC_BOOKING_CREATED),
                eq(bookingId.toString()),
                eq(request)
        );
    }
}