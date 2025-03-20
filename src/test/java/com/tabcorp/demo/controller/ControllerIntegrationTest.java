package com.tabcorp.demo.controller;

import com.tabcorp.demo.dto.TransactionDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import java.time.LocalDateTime;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ControllerIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void testSubmitTransaction_Success() {
        // given
        TransactionDTO dto = new TransactionDTO();
        dto.setTransactionTime(LocalDateTime.now().plusDays(1));
        dto.setQuantity(2);
        dto.setProductCode("PRODUCT_001");
        dto.setCustomerID(10001);

        // when
        ResponseEntity<String> response = restTemplate.postForEntity("/transactions", dto, String.class);

        // then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().contains("Transaction processed successfully"));
    }

    @Test
    public void testSubmitTransaction_InvalidProduct() {
        // given
        TransactionDTO dto = new TransactionDTO();
        dto.setTransactionTime(LocalDateTime.now().plusDays(1));
        dto.setQuantity(2);
        dto.setProductCode("INVALID_PRODUCT");
        dto.setCustomerID(10001);

        // when
        ResponseEntity<String> response = restTemplate.postForEntity("/transactions", dto, String.class);

        // then
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertTrue(response.getBody().contains("Product not found"));
    }

    @Test
    public void testSubmitTransaction_InvalidCustomer() {
        // given
        TransactionDTO dto = new TransactionDTO();
        dto.setTransactionTime(LocalDateTime.now().plusDays(1));
        dto.setQuantity(2);
        dto.setProductCode("PRODUCT_001");
        dto.setCustomerID(99999);

        // when
        ResponseEntity<String> response = restTemplate.postForEntity("/transactions", dto, String.class);

        // then
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertTrue(response.getBody().contains("Customer not found"));
    }

    @Test
    public void testSubmitTransaction_ValidationFailure() {
        // given
        TransactionDTO dto = new TransactionDTO();
        // Set invalid values (null or empty)
        dto.setTransactionTime(null);
        dto.setQuantity(null);
        dto.setProductCode("");
        dto.setCustomerID(null);

        // when
        ResponseEntity<String> response = restTemplate.postForEntity("/transactions", dto, String.class);

        // then
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void testSubmitTransaction_DateInPast() {
        // given
        TransactionDTO dto = new TransactionDTO();
        // Use a past date which should fail the @FutureOrPresent constraint
        dto.setTransactionTime(LocalDateTime.now().minusDays(1));
        dto.setQuantity(1);
        dto.setProductCode("PRODUCT_001");
        dto.setCustomerID(10001);

        // when
        ResponseEntity<String> response = restTemplate.postForEntity("/transactions", dto, String.class);

        // then
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void testSubmitTransaction_CostExceedsLimit() {
        // given
        TransactionDTO dto = new TransactionDTO();
        dto.setTransactionTime(LocalDateTime.now().plusDays(1));
        // Set very high quantity to ensure total cost > 5000
        dto.setQuantity(500);
        dto.setProductCode("PRODUCT_001");
        dto.setCustomerID(10001);

        // when
        ResponseEntity<String> response = restTemplate.postForEntity("/transactions", dto, String.class);

        // then
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody().contains("Total cost of transaction must not exceed 5000"));
    }

    @Test
    public void testReportEndpoints() {
        // given
        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth("user", "password");
        HttpEntity<?> httpEntity = new HttpEntity<>(headers);

        // when
        ResponseEntity<Map> customerCostResponse = restTemplate.exchange(
                "/report/customer-costs", HttpMethod.GET, httpEntity, Map.class);
        ResponseEntity<Map> productCostResponse = restTemplate.exchange(
                "/report/product-costs", HttpMethod.GET, httpEntity, Map.class);
        ResponseEntity<Long> australiaCountResponse = restTemplate.exchange(
                "/report/australia-transactions", HttpMethod.GET, httpEntity, Long.class);

        // then
        assertEquals(HttpStatus.OK, customerCostResponse.getStatusCode());
        assertEquals(HttpStatus.OK, productCostResponse.getStatusCode());
        assertEquals(HttpStatus.OK, australiaCountResponse.getStatusCode());
    }
}
