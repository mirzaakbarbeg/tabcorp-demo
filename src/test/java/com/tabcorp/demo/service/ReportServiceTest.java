package com.tabcorp.demo.service;

import com.tabcorp.demo.repository.TransactionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ReportServiceTest {

    @InjectMocks
    private ReportService reportService;
    @Mock
    private TransactionRepository transactionRepository;

    @Test
    public void testCalculateCostPerCustomer_Success() {
        Object[] row1 = new Object[]{10001, 500L};
        Object[] row2 = new Object[]{10002, 750L};
        when(transactionRepository.findTotalCostPerCustomer()).thenReturn(List.of(row1, row2));

        Map<Integer, Long> result = reportService.calculateCostPerCustomer();
        assertEquals(2, result.size());
        assertEquals(500L, result.get(10001));
        assertEquals(750L, result.get(10002));
    }

    @Test
    public void testCalculateCostPerProduct_Success() {
        Object[] row1 = new Object[]{"PRODUCT_001", 1000L};
        Object[] row2 = new Object[]{"PRODUCT_002", 1500L};
        when(transactionRepository.findTotalCostPerProduct()).thenReturn(List.of(row1, row2));

        Map<String, Long> result = reportService.calculateCostPerProduct();
        assertEquals(2, result.size());
        assertEquals(1000L, result.get("PRODUCT_001"));
        assertEquals(1500L, result.get("PRODUCT_002"));
    }

    @Test
    public void testCountTransactionsForAustralia_Success() {
        when(transactionRepository.countTransactionsForAustralia()).thenReturn(3L);
        Long count = reportService.countTransactionsForAustralia();
        assertEquals(3L, count);
    }
}
