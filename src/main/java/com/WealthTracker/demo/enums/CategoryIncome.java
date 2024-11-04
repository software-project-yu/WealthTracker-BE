package com.WealthTracker.demo.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum CategoryIncome {
    SALARY("월급"),ADD("부수입"),PIN_MONEY("용돈"),BONUS("상여"),FINANCE("금용소득"),ETC("기타");


    private final String categoryType;

    //카테고리 한글명 반환
    public static CategoryIncome fromString(String categoryType) {
        for (CategoryIncome cI : CategoryIncome.values()) {
            if (cI.getCategoryType().equalsIgnoreCase(categoryType)) {
                return cI;
            }
        }
        return null;
    }


    //영어->한글
    public static String toString(CategoryIncome categoryIncome){
        return categoryIncome.getCategoryType();
    }
}
