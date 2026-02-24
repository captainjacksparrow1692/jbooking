package uzumtech.jbooking.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import uzumtech.jbooking.constant.Constant;

@Configuration
public class KafkaTopicConfig {

    @Bean
    public NewTopic bookingCreatedTopic() {
        return new NewTopic(Constant.TOPIC_BOOKING_CREATED, 3, (short) 1);
    }

    @Bean
    public NewTopic paymentProcessedTopic() {
        return new NewTopic(Constant.TOPIC_PAYMENT_PROCESSED, 3, (short) 1);
    }
}
