package com.WealthTracker.demo.global.lock;

import com.WealthTracker.demo.domain.expend.entity.CategoryExpend;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface LockRepository extends JpaRepository<CategoryExpend,Long> {
    @Query(value = "select get_lock(:key,10)",nativeQuery = true)
    Integer getLock(@Param("key") String key);

    @Query(value = "select release_lock(:key)",nativeQuery = true)
    void releaseLock(@Param("key") String key);
}
