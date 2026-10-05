package com.example.store;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BillRepository extends JpaRepository<Bill, Long> {

    @EntityGraph(attributePaths = "items")
    List<Bill> findAllByOrderByCreatedAtDesc();

    @Override
    @EntityGraph(attributePaths = "items")
    Optional<Bill> findById(Long id);
}
