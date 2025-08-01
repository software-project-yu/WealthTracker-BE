package com.WealthTracker.demo.domain.category.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Asset {
    CASH("현금"), BANK("은행"), ETC("기타");
    private final String categoryType;

    //카테고리 한글명 반환
    public static Asset fromString(String categoryType) {
        for (Asset asset : Asset.values()) {
            if (asset.getCategoryType().equalsIgnoreCase(categoryType)) {
                return asset;
            }
        }
        return null;
    }
    //영어->한글
    public static String toString(Asset asset){
        return asset.getCategoryType();
    }
}
