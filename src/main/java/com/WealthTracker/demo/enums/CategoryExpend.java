package com.WealthTracker.demo.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum CategoryExpend {
    PAYMENT("납부"), FOOD("식비"), TRANSPORTATION("교통"), GAME("오락"), SHOPPING("쇼핑"), ETC("기타");

    private final String categoryType;

    //한글->영어
    public static CategoryExpend fromString(String categoryType) {
        for (CategoryExpend ct : CategoryExpend.values()) {
            if (ct.getCategoryType().equalsIgnoreCase(categoryType)) {
                return ct;
            }
        }
        return null;
    }
    //영어->한글
    public static String toString(CategoryExpend categoryExpend){
       return categoryExpend.getCategoryType();
    }

}
