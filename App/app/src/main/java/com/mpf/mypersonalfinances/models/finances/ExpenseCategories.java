package com.mpf.mypersonalfinances.models.finances;

/**
 * Created by Lucas on 26/07/2017.
 */

public enum ExpenseCategories {
    CLOTHING("Clothing"),
    EDUCATION("Education"),
    ENTERTAINMENT("Entertainment"),
    FOOD("Food"),
    HEALTH("Health"),
    HOUSESUPPLIES("House Supplies"),
    INSURANCE("Insurance"),
    MEDICAL("Medical"),
    TRANSPORTATION("Transportation"),
    UTILITY("Utility");

    private String categoryName;

    ExpenseCategories(String categoryName) {
        this.categoryName = categoryName;
    }

    @Override
    public String toString() {
        return categoryName;
    }
}
