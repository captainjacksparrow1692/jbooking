package uzumtech.jbooking.config.props;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
@ConfigurationProperties(prefix = "kafka")
public class KafkaProps {

    Topic topic;

    String bootstrapServers;
    String clientId;
    String clientDnsLookup;
    // Настройки продюсера
    String acksConfig;
    String retriesConfig;
    int batchSizeConfig;
    long lingerMsConfig;
    int bufferMemoryConfig;
    // Настройки консьюмера
    String groupId;
    String autoOffsetResetConfig;
    // Безопасность
    String saslProtocol;

    @Data
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class Topic {

        // Топик для новых бронирований (использовали в тестах)
        String bookingCreated;

        // Топик для статусов оплаты
        String paymentStatus;

        // Топик для уведомлений (Email/SMS)
        String notifications;
    }

}
