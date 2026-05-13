package com.divsis.money_manager.repository;

import com.divsis.money_manager.entity.IncomeEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface IncomeRepository extends JpaRepository<IncomeEntity, Long> {
    List<IncomeEntity> findByProfileIdOrderByDateDesc(Long profileID);
    List<IncomeEntity> findByProfileIdOrderByDateDesc(Long profileID, Pageable pageable);

    @Query("SELECT COALESCE(SUM(i.amount), 0) FROM IncomeEntity i WHERE i.profile.id = :profileId")
    BigDecimal findTotalExpenseByProfileId(@Param("profileId") Long profileId);

    List<IncomeEntity>findByProfileIdAndDateBetweenAndNameContainingIgnoreCase(Long profileId , LocalDate startDate , LocalDate endDate , String name , Sort sort);
    List<IncomeEntity>findByProfileIdAndDateBetween(Long profileId , LocalDate startDate , LocalDate endDate);
    List<IncomeEntity>findByProfileIdAndDate(Long profileId , LocalDate date);
}
