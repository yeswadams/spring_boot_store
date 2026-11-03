package com.masoko.store.features.payment;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
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

//   Consumes incoming plain JSON streams from Safaricom, parsing nested structures using Jackson.
    @PostMapping
    public ResponseEntity<String> receiveMpesaCallback(@RequestBody String jsonPayload) {
        try {
            JsonNode root = objectMapper.readTree(jsonPayload);
            JsonNode stkCallback = root.path("Body").path("stkCallback");

            String checkoutRequestId = stkCallback.path("CheckoutRequestID").asText();
            int resultCode = stkCallback.path("ResultCode").asInt();

            if(resultCode == 0) {
                JsonNode metaItems = stkCallback.path("CallbackMetaData").path("Item");
                String receiptNumber = "";
                for(JsonNode item : metaItems) {
                    if("MpesaReceiptNumber".equals(item.path("Name").asText())) {
                        receiptNumber = item.path("value").asText();
                    }
                }
                paymentService.processMpesaCallback(checkoutRequestId, receiptNumber, true);
            } else {
                paymentService.processMpesaCallback(checkoutRequestId, null, false);
            }

        } catch(Exception e) {
            return ResponseEntity.status(500).body("Error processing callback format");
        }
    }

}
