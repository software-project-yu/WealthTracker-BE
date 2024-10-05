package com.WealthTracker.demo.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum CategoryType {
    payment("납부"), food("식비"), transportation("교통"), game("게임"), shopping("쇼핑"), etc("기타");

    private final String categoryType;

    //카테고리 한글명 반환
    public static CategoryType fromString(String categoryType) {
        for (CategoryType ct : CategoryType.values()) {
            if (ct.getCategoryType().equalsIgnoreCase(categoryType)) {
                return ct;
            }
        }
        return null;
    }

}
