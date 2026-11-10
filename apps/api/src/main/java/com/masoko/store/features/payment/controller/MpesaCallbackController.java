// File: apps/api/src/main/java/com/masoko/store/features/payment/MpesaCallbackController.java
package com.masoko.store.features.payment.controller;

import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;
import com.masoko.store.features.payment.service.PaymentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/callback/mpesa")
public class MpesaCallbackController {

    private final PaymentService paymentService;
    private final ObjectMapper objectMapper;

    public MpesaCallbackController(PaymentService paymentService, ObjectMapper objectMapper) {
        this.paymentService = paymentService;
        this.objectMapper = objectMapper;
    }

    /**
     * Consumes incoming plain JSON streams from Safaricom, parsing nested structures using Jackson.
     */
    @PostMapping
    public ResponseEntity<String> receiveMpesaCallback(@RequestBody String jsonPayload) {
        try {
            // Traverse Safaricom's nested JSON schema framework via Jackson JsonNode paths
            JsonNode root = objectMapper.readTree(jsonPayload);
            JsonNode stkCallback = root.path("Body").path("stkCallback");

            String checkoutRequestId = stkCallback.path("CheckoutRequestID").asText();
            int resultCode = stkCallback.path("ResultCode").asInt();

            if (resultCode == 0) {
                // ResultCode 0 means transaction success. Pull receipt item out of metadata array
                JsonNode metaItems = stkCallback.path("CallbackMetadata").path("Item");
                String receiptNumber = "";
                for (JsonNode item : metaItems) {
                    if ("MpesaReceiptNumber".equals(item.path("Name").asText())) {
                        receiptNumber = item.path("Value").asText();
                    }
                }
                paymentService.processMpesaCallback(checkoutRequestId, receiptNumber, true);
            } else {
                // User cancelled or lacked sufficient funds balance
                paymentService.processMpesaCallback(checkoutRequestId, null, false);
            }

            // Standard acknowledgment pattern response structure expected by Daraja
            return ResponseEntity.ok("{\"ResultCode\":0,\"ResultDesc\":\"Success\"}");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error processing callback format");
        }
    }
}
