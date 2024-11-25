package pl.dolien.shop.payment;

import com.stripe.model.PaymentIntent;
import pl.dolien.shop.exception.PaymentProcessingException;

public interface PaymentService {

    PaymentIntent createPaymentIntent(PaymentInfo paymentInfo) throws PaymentProcessingException;
}

