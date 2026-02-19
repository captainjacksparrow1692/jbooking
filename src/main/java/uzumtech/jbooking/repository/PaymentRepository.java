package uzumtech.jbooking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uzumtech.jbooking.constant.enums.PaymentStatus;
import uzumtech.jbooking.entity.Payment;

import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Optional<Payment> findByTransactionId(String transactionId);
    // Важно для поиска успешного платежа перед возвратом
    Optional<Payment> findByBookingIdAndPaymentStatus(Long bookingId, PaymentStatus status);
}