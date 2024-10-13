package com.WealthTracker.demo.repository;

import com.WealthTracker.demo.domain.Category_expend;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CategoryExpendRepository extends JpaRepository<Category_expend,Long> {
    //카테고리명 찾기
    @Query("select ce.categoryExpend.categoryName from CategoryExpend ce where ce.expend.expendId = :expendId")
    Optional<String> findCategoryNameByExpendId(@Param("expendId")Long expendId);
}
