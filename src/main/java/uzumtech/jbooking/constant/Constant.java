package uzumtech.jbooking.constant;

import lombok.experimental.UtilityClass;

@UtilityClass
public class Constant {

    public static final int DEFAULT_PAGE_SIZE = 20;

    public static final int MAX_PAGE_SIZE = 100;

    public static final int DEFAULT_BOOKING_HOLD_MINUTES = 15;

    public static final String BANK_WEBHOOK_SECRET = "YOUR_SECRET_BANK_TOKEN";

    public static final String PAYMENT_SUCCESS_MESSAGE = "OK";

    // Kafka topics
    public static final String TOPIC_BOOKING_CREATED = "booking.created";
    public static final String TOPIC_PAYMENT_PROCESSED = "payment.processed";
}
