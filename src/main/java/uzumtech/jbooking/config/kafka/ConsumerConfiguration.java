package uzumtech.jbooking.config.kafka;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.context.annotation.Configuration;
import uzumtech.jbooking.config.props.KafkaProps;
import uzumtech.jbooking.handler.KafkaExceptionHandler;

@Configuration
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ConsumerConfiguration {

    KafkaProps kafkaProps;
    KafkaExceptionHandler kafkaErrorHandler;

}
