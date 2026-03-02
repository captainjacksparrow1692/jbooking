package uzumtech.jbooking.config;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestClient;
import uzumtech.jbooking.config.props.RestClientProps;
import uzumtech.jbooking.handler.RestClientExceptionHandler;

import java.time.Duration;

@Configuration
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class RestClientConfig {

    RestClientProps restClientProps;

    @Bean
    public ClientHttpRequestFactory clientHttpRequestFactory() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setReadTimeout(Duration.ofMillis(restClientProps.getReadTimeoutOfMillis()));
        factory.setConnectTimeout(Duration.ofMillis(restClientProps.getConnectionTimeoutOfMillis()));
        return factory;
    }

    @Bean("jbankClient")
    public RestClient jbankClient(RestClient.Builder builder) {
        return builder
                .baseUrl(restClientProps.getJbankUrl())
                .requestFactory(clientHttpRequestFactory())
                .defaultStatusHandler(new RestClientExceptionHandler())
                .build();
    }

    @Bean("jnotificationClient")
    public RestClient jnotificationClient(RestClient.Builder builder) {
        return builder
                .baseUrl(restClientProps.getJnotificationUrl())
                .requestFactory(clientHttpRequestFactory())
                .defaultStatusHandler(new RestClientExceptionHandler())
                .build();
    }
}