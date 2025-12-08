package com.bluebell.project.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Base64;
import java.util.Map;

@Service
public class PayMongoService {

    @Value("${paymongo.secret.key}")
    private String PAYMONGO_SECRET_KEY ;

    private static final String API_BASE = "https://api.paymongo.com/v1/payment_intents";

    private static final String PAYMENT_METHODS_API = "https://api.paymongo.com/v1/payment_methods";


    private final ObjectMapper objectMapper = new ObjectMapper();


    public JsonNode createMayaIntent(long amount, String returnUrl) throws Exception {

        String auth = PAYMONGO_SECRET_KEY + ":";
        String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes());

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Basic " + encodedAuth);
        headers.setContentType(MediaType.APPLICATION_JSON);

        RestTemplate restTemplate = new RestTemplate();

        // CREATE PAYMENT INTENT
        String body = "{\n" +
                "  \"data\": {\n" +
                "    \"attributes\": {\n" +
                "      \"amount\": " + amount + ",\n" +
                "      \"currency\": \"PHP\",\n" +
                "      \"payment_method_allowed\": [\"paymaya\"],\n" +
                "      \"payment_method_options\": {\n" +
                "        \"paymaya\": {\n" +
                "          \"success_url\": \"" + returnUrl + "\",\n" +
                "          \"cancel_url\": \"" + returnUrl + "\"\n" +
                "        }\n" +
                "      }\n" +
                "    }\n" +
                "  }\n" +
                "}";

        ResponseEntity<String> intentResponse =
                restTemplate.postForEntity(API_BASE, new HttpEntity<>(body, headers), String.class);

        JsonNode intentJson = objectMapper.readTree(intentResponse.getBody());
        String clientKey = intentJson.get("data").get("attributes").get("client_key").asText();

        // RETURN client_key to frontend for PayMaya checkout
        ObjectNode responseNode = objectMapper.createObjectNode();
        responseNode.set("paymentIntent", intentJson);
        responseNode.put("clientKey", clientKey);

        return responseNode;
    }

    // Optional: Verify Payment Intent status
    public JsonNode getPaymentIntentStatus(String intentId) throws Exception {
        String url = API_BASE + "/" + intentId;

        String auth = PAYMONGO_SECRET_KEY + ":";
        String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes());

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Basic " + encodedAuth);
        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<String> response = restTemplate.exchange(url, org.springframework.http.HttpMethod.GET,
                new HttpEntity<>(headers), String.class);

        return objectMapper.readTree(response.getBody());
    }




//    public JsonNode createQRPHIntent(long amount, String returnUrl) throws Exception {
//
//        // 1️⃣ Prepare headers
//        String auth = PAYMONGO_SECRET_KEY + ":";
//        String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes());
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.set("Authorization", "Basic " + encodedAuth);
//        headers.setContentType(MediaType.APPLICATION_JSON);
//
//        // 2️⃣ Create Payment Intent with QR PH static type
//        String intentBody = "{\n" +
//                "  \"data\": {\n" +
//                "    \"attributes\": {\n" +
//                "      \"amount\": " + amount + ",\n" +
//                "      \"currency\": \"PHP\",\n" +
//                "      \"payment_method_allowed\": [\"qrph\"],\n" +
//                "      \"payment_method_options\": {\n" +
//                "        \"qrph\": { \"type\": \"static\" }\n" +
//                "      }\n" +
//                "    }\n" +
//                "  }\n" +
//                "}";
//
//        RestTemplate restTemplate = new RestTemplate();
//        ResponseEntity<String> intentResponse =
//                restTemplate.postForEntity(API_BASE, new HttpEntity<>(intentBody, headers), String.class);
//
//        JsonNode intentJson = objectMapper.readTree(intentResponse.getBody());
//        String clientKey = intentJson.get("data").get("attributes").get("client_key").asText();
//        String intentId = intentJson.get("data").get("id").asText();
//
//        // 3️⃣ Get QR PH URL from qr_ph_url (works for live mode)
//        String qrUrl = intentJson.path("data").path("attributes").path("qr_ph_url").asText(null);
//
//        // 4️⃣ Prepare response
//        ObjectNode responseNode = objectMapper.createObjectNode();
//        responseNode.put("intentId", intentId);
//        responseNode.put("clientKey", clientKey);
//        responseNode.put("qrUrl", qrUrl != null ? qrUrl : "QR URL not available (sandbox mode)");
//
//
//
//
//        return responseNode;
//    }


//
//    public String createPaymentIntent(long amount) throws Exception {
//
//        // PayMongo requires Basic Auth (secret key)
//        String auth = PAYMONGO_SECRET_KEY + ":";
//        String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes());
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.set("Authorization", "Basic " + encodedAuth);
//        headers.setContentType(MediaType.APPLICATION_JSON);
//
//        // Payload
//        // Optional: description
//        String body = "{\n" +
//                "  \"data\": {\n" +
//                "    \"attributes\": {\n" +
//                "      \"amount\": " + amount + ",\n" +
//                "      \"currency\": \"PHP\",\n" +
//                "      \"payment_method_allowed\": [\"card\", \"gcash\"]\n" +
//                "    }\n" +
//                "  }\n" +
//                "}";
//
//        HttpEntity<String> request = new HttpEntity<>(body, headers);
//
//        RestTemplate restTemplate = new RestTemplate();
//        ResponseEntity<String> response =
//                restTemplate.postForEntity(API_BASE, request, String.class);
//
//        return response.getBody();
//    }
}
