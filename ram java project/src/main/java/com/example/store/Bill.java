package com.example.store;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "bills")
public class Bill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal total;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "bill", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @OrderBy("id ASC")
    private List<BillItem> items = new ArrayList<>();

    protected Bill() {
    }

    public Bill(BigDecimal total) {
        this.total = total;
    }

    public void addItem(BillItem item) {
        items.add(item);
        item.setBill(this);
    }

    @PrePersist
    void onCreate() {
        createdAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public List<BillItem> getItems() {
        return items;
    }
}
