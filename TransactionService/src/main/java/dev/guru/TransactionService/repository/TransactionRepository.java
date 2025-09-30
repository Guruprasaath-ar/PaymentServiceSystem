package dev.guru.TransactionService.repository;

import dev.guru.TransactionService.domain.TransactionEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends JpaRepository<TransactionEntity,Long> {

    Page<TransactionEntity> findBySenderId(Long id, Pageable pageable);

}
