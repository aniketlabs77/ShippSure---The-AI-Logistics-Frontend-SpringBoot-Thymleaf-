package com.aniket.controller;

import java.util.Map;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.Utils;

@RestController
public class PaymentController {

    @Value("${razorpay.key.id}")
    private String keyId;

    @Value("${razorpay.key.secret}")
    private String keySecret;

    @PostMapping("/create-order")
    public Map<String, Object> createOrder(@RequestParam double amount) throws Exception {

        RazorpayClient client = new RazorpayClient(keyId, keySecret);

        JSONObject data = new JSONObject();
        data.put("amount", (int) Math.round(amount * 100));
        data.put("currency", "INR");
        data.put("receipt", "shipment_" + System.currentTimeMillis());

        Order order = client.orders.create(data);

        return Map.of(
                "id", order.get("id"),
                "amount", order.get("amount"),
                "key", keyId
        );
    }

    @PostMapping("/save-payment")
    public Map<String, String> savePayment(@RequestBody Map<String, String> data) throws Exception {

        JSONObject options = new JSONObject();
        options.put("razorpay_order_id", data.get("orderId"));
        options.put("razorpay_payment_id", data.get("paymentId"));
        options.put("razorpay_signature", data.get("signature"));

        if (Utils.verifyPaymentSignature(options, keySecret)) {
            return Map.of("status", "success");
        }

        throw new RuntimeException("Invalid payment signature");
    }
}
    