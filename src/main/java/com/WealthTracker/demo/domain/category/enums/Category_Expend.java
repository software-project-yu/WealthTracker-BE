package com.WealthTracker.demo.domain.category.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Category_Expend {
    PAYMENT("납부"), FOOD("식비"), TRANSPORTATION("교통"), GAME("오락"), SHOPPING("쇼핑"), ETC("기타");

    private final String categoryType;

    //한글->영어
    public static Category_Expend fromString(String categoryType) {
        for (Category_Expend ct : Category_Expend.values()) {
            if (ct.getCategoryType().equalsIgnoreCase(categoryType)) {
                return ct;
            }
        }
        return null;
    }
    //영어->한글
    public static String toString(Category_Expend categoryExpend){
       return categoryExpend.getCategoryType();
    }

}
