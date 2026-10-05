package com.example.store;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class BillingServiceTest {

    private final ProductRepository productRepository = mock(ProductRepository.class);
    private final BillRepository billRepository = mock(BillRepository.class);
    private final BillingService billingService = new BillingService(productRepository, billRepository);

    @Test
    void createsBillFromSelectedProductsAndQuantities() {
        Product coffee = product("Coffee", "4.25");
        Product tea = product("Tea", "2.50");
        when(productRepository.findById(1L)).thenReturn(Optional.of(coffee));
        when(productRepository.findById(2L)).thenReturn(Optional.of(tea));
        when(billRepository.save(any(Bill.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Map<Long, Integer> quantities = new LinkedHashMap<>();
        quantities.put(1L, 2);
        quantities.put(2L, 1);

        Bill bill = billingService.createBill(quantities);

        assertEquals(0, bill.getTotal().compareTo(new BigDecimal("11.00")));
        assertEquals(2, bill.getItems().size());
        assertEquals(2, bill.getItems().get(0).getQuantity());
        assertEquals("Coffee", bill.getItems().get(0).getProductName());
        assertEquals(0, bill.getItems().get(0).getLineTotal().compareTo(new BigDecimal("8.50")));
    }

    @Test
    void rejectsBillWithoutSelectedProducts() {
        assertThrows(IllegalArgumentException.class, () -> billingService.createBill(Map.of(1L, 0)));
    }

    @Test
    void rejectsNegativeQuantity() {
        assertThrows(IllegalArgumentException.class, () -> billingService.createBill(Map.of(1L, -1)));
    }

    @Test
    void rejectsBillThatExceedsDatabasePrecision() {
        Product expensive = product("Expensive item", "99999999.99");
        when(productRepository.findById(1L)).thenReturn(Optional.of(expensive));

        assertThrows(IllegalArgumentException.class, () -> billingService.createBill(Map.of(1L, 101)));
    }

    private Product product(String name, String price) {
        Product product = mock(Product.class);
        when(product.getName()).thenReturn(name);
        when(product.getPrice()).thenReturn(new BigDecimal(price));
        return product;
    }
}
