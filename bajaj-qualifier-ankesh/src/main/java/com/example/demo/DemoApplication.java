package com.example.demo;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
public class DemoApplication implements CommandLineRunner {

    // Pre-filled with your details
    private static final String NAME = "Anikesh Kumar";
    private static final String REG_NO = "112215021";
    private static final String EMAIL = "112215021@cse.iiitp.ac.in";

    private static final String GENERATE_WEBHOOK_URL =
            "https://bfhldevapigw.healthrx.co.in/hiring/generateWebhook/JAVA";

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

    @Override
    public void run(String... args) {
        RestTemplate restTemplate = new RestTemplate();

        try {

            Map<String, String> body = new HashMap<>();
            body.put("name", NAME);
            body.put("regNo", REG_NO);
            body.put("email", EMAIL);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<Map<String, String>> request = new HttpEntity<>(body, headers);

            System.out.println("Sending generateWebhook request...");
            ResponseEntity<Map> response = restTemplate.exchange(
                    GENERATE_WEBHOOK_URL,
                    HttpMethod.POST,
                    request,
                    Map.class
            );

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                String webhookUrl = (String) response.getBody().get("webhook");
                String accessToken = (String) response.getBody().get("accessToken");

                System.out.println(" Webhook URL: " + webhookUrl);
                System.out.println(" Access Token: " + accessToken);

                String finalQuery = getFinalSQLQuery(REG_NO);


                submitFinalQuery(restTemplate, webhookUrl, accessToken, finalQuery);
            } else {
                System.err.println("Failed to generate webhook. Response status: " + response.getStatusCode());
                System.err.println("Response body: " + response.getBody());
            }

        } catch (Exception e) {
            System.err.println("Exception while running startup flow:");
            e.printStackTrace();
        } finally {
            // Optionally exit so the app doesn't keep running forever for this demo
            System.exit(0);
        }
    }

    private String getFinalSQLQuery(String regNo) {
        String digits = regNo.replaceAll("\\D+", "");
        if (digits.length() < 2) {
            throw new IllegalArgumentException("regNo must contain at least two digits");
        }
        int lastTwo = Integer.parseInt(digits.substring(digits.length() - 2));

        if (lastTwo % 2 != 0) {
            System.out.println("Assigned: Question 1 (Odd)");
            return "SELECT " +
                    "p.AMOUNT AS SALARY, " +
                    "CONCAT(e.FIRST_NAME, ' ', e.LAST_NAME) AS NAME, " +
                    "TIMESTAMPDIFF(YEAR, e.DOB, CURDATE()) AS AGE, " +
                    "d.DEPARTMENT_NAME " +
                    "FROM PAYMENTS p " +
                    "JOIN EMPLOYEE e ON p.EMP_ID = e.EMP_ID " +
                    "JOIN DEPARTMENT d ON e.DEPARTMENT = d.DEPARTMENT_ID " +
                    "WHERE DAY(p.PAYMENT_TIME) <> 1 " +
                    "ORDER BY p.AMOUNT DESC " +
                    "LIMIT 1;";
        } else {
            System.out.println("Assigned: Question 2 (Even)");
            return "SELECT 'Even registration number — no query yet' AS message;";
        }
    }

    private void submitFinalQuery(RestTemplate restTemplate, String webhookUrl,
                                  String accessToken, String finalQuery) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Authorization", accessToken);

            Map<String, String> body = new HashMap<>();
            body.put("finalQuery", finalQuery);

            HttpEntity<Map<String, String>> request = new HttpEntity<>(body, headers);

            System.out.println("Submitting final SQL query to webhook...");
            ResponseEntity<String> response = restTemplate.exchange(
                    webhookUrl,
                    HttpMethod.POST,
                    request,
                    String.class
            );

            System.out.println("Webhook response status: " + response.getStatusCode());
            System.out.println("Webhook response body: " + response.getBody());
        } catch (Exception e) {
            System.err.println("Exception while submitting final query:");
            e.printStackTrace();
        }
    }
}
