package com.dividends.dohi.persist.repository;

import com.dividends.dohi.persist.entity.DividendEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DividendRepository extends JpaRepository<DividendEntity, Long> {
}
