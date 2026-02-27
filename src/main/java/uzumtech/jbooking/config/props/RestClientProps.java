package uzumtech.jbooking.config.props;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@FieldDefaults(level = AccessLevel.PRIVATE)
@ConfigurationProperties(prefix = "rest-client")
public class RestClientProps {

    long readTimeoutOfMillis;
    long connectionTimeoutOfMillis;

    String jbankUrl;
    String jnotificationUrl;
}