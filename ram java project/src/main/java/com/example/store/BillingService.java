package com.example.store;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class BillingService {

    private static final BigDecimal MAX_BILL_TOTAL = new BigDecimal("9999999999.99");

    private final ProductRepository productRepository;
    private final BillRepository billRepository;

    public BillingService(ProductRepository productRepository, BillRepository billRepository) {
        this.productRepository = productRepository;
        this.billRepository = billRepository;
    }

    @Transactional
    public Bill createBill(Map<Long, Integer> quantities) {
        if (quantities == null) {
            throw new IllegalArgumentException("Choose at least one product.");
        }

        List<BillItem> items = new ArrayList<>();
        BigDecimal total = BigDecimal.ZERO;

        for (Map.Entry<Long, Integer> entry : quantities.entrySet()) {
            Integer quantity = entry.getValue();
            if (entry.getKey() == null || quantity == null || quantity < 0) {
                throw new IllegalArgumentException("Choose valid products and quantities of zero or greater.");
            }
            if (quantity == 0) {
                continue;
            }

            Product product = productRepository.findById(entry.getKey())
                    .orElseThrow(() -> new IllegalArgumentException("A selected product no longer exists."));
            BillItem item = new BillItem(product, quantity);
            items.add(item);
            total = total.add(item.getLineTotal());
            if (total.compareTo(MAX_BILL_TOTAL) > 0) {
                throw new IllegalArgumentException("Bill total exceeds the maximum supported amount.");
            }
        }

        if (items.isEmpty()) {
            throw new IllegalArgumentException("Choose at least one product with a quantity greater than zero.");
        }

        Bill bill = new Bill(total);
        items.forEach(bill::addItem);
        return billRepository.save(bill);
    }
}
