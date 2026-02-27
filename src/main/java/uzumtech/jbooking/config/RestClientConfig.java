package uzumtech.jbooking.config;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.boot.http.client.ClientHttpRequestFactoryBuilder;
import org.springframework.boot.http.client.HttpClientSettings;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
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
        var settings = HttpClientSettings
                .defaults()
                .withReadTimeout(Duration.ofMillis(restClientProps.getReadTimeoutOfMillis()))
                .withConnectTimeout(Duration.ofMillis(restClientProps.getConnectionTimeoutOfMillis()));

        return ClientHttpRequestFactoryBuilder.jdk().build(settings);
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