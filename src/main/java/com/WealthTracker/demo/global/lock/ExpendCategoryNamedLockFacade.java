package com.WealthTracker.demo.global.lock;

import com.WealthTracker.demo.domain.expend.entity.CategoryExpend;

public interface ExpendCategoryNamedLockFacade {

    CategoryExpend getOrCreateCustomCategoryExpend(String category);
}
