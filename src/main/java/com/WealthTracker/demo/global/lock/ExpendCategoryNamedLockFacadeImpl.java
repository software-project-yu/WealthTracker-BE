package com.WealthTracker.demo.global.lock;

import com.WealthTracker.demo.domain.category.enums.Category_Expend;
import com.WealthTracker.demo.domain.category.repository.ExpendCategoryRepository;
import com.WealthTracker.demo.domain.expend.entity.CategoryExpend;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Slf4j
public class ExpendCategoryNamedLockFacadeImpl implements ExpendCategoryNamedLockFacade{
    private final LockRepository lockRepository;
    private final ExpendCategoryRepository expendCategoryRepository;

    @Transactional
    public CategoryExpend getOrCreateCustomCategoryExpend(String category) {
        Integer lockResult=lockRepository.getLock(category);
        //get_lock 실패시
        if(lockResult==null || lockResult!=1){
            log.error("GET_LOCK FAILED");
            throw new RuntimeException("GET_LOCK FAILED");
        }

        try {
            return expendCategoryRepository.findByCustomCategoryName(category)
                    .orElseGet(()->{
                       CategoryExpend newCategory=CategoryExpend.createCustomCategory(category);
                       return expendCategoryRepository.save(newCategory);
                    });
        } finally {
            lockRepository.releaseLock(category);
        }
    }

}
