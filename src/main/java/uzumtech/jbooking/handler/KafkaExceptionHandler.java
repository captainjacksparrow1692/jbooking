package uzumtech.jbooking.handler;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.TopicPartition;
import org.springframework.context.annotation.Lazy;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.CommonErrorHandler;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.listener.MessageListenerContainer;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class KafkaExceptionHandler implements CommonErrorHandler {

    private final DeadLetterPublishingRecoverer recoverer;

    public KafkaExceptionHandler(@Lazy KafkaTemplate<String, Object> kafkaTemplate) {
        this.recoverer = new DeadLetterPublishingRecoverer(kafkaTemplate,
                (record, ex) -> {
                    log.error("Перенаправление сообщения из топика {} в DLQ", record.topic());
                    return new TopicPartition(record.topic() + ".DLT", record.partition());
                });
    }

    @Override
    public boolean handleOne(Exception ex,
                             ConsumerRecord<?, ?> record,
                             Consumer<?, ?> consumer,
                             MessageListenerContainer container) {
        log.error("Ошибка при обработке сообщения в jBooking Kafka Listener!");
        log.error("Топик: {}, Ключ: {}, Ошибка: {}", record.topic(), record.key(), ex.getMessage());
        recoverer.accept(record, ex);
        return true;
    }

    @Override
    public void handleOtherException(Exception ex,
                                     Consumer<?, ?> consumer,
                                     MessageListenerContainer container,
                                     boolean batchListener) {
        log.error("Критическая системная ошибка Kafka в jBooking: {}", ex.getMessage());
    }
}