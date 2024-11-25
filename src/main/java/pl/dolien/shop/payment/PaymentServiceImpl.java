package pl.dolien.shop.payment;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import pl.dolien.shop.exception.PaymentProcessingException;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PaymentServiceImpl implements PaymentService {

    public PaymentServiceImpl(@Value("${stripe.key.secret}") String secretKey) {
        Stripe.apiKey = secretKey;
    }

    @Override
    public PaymentIntent createPaymentIntent(PaymentInfo paymentInfo) {
        List<String> paymentMethodTypes = Collections.singletonList("card");

        Map<String, Object> params = new HashMap<>();
        params.put("amount", paymentInfo.getAmount());
        params.put("currency", paymentInfo.getCurrency());
        params.put("payment_method_types", paymentMethodTypes);
        params.put("description", "E-commerce shop purchase");
        params.put("receipt_email", paymentInfo.getReceiptEmail());

        try {
            return PaymentIntent.create(params);
        } catch (StripeException e) {
            throw new PaymentProcessingException("Failed to create payment intent");
        }
    }
}
