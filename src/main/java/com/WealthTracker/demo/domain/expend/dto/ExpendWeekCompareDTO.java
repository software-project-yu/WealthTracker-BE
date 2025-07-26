package com.WealthTracker.demo.domain.expend.dto;
public interface ExpendWeekCompareDTO {
    Integer getWeekNum();
    Long getThisMonthTotalCost();
    Long getPrevMonthTotalCost();
}
