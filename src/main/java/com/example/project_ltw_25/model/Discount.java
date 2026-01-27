package com.example.project_ltw_25.user.model;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

public class Discount {
    private int id;
    private String discount_code;
    private String discount_name;
    private int discount_percent;
    private Timestamp start_date;
    private Timestamp end_date;
    private String description;
    private Timestamp created_at;
    private String appliedScopeNames;
    private List<Integer> appliedCategoryIds;
    private List<Integer> appliedProductTypeIds;

    public Discount() {
    }

    public Discount(int id, String discount_code, String discount_name, int discount_percent, Timestamp start_date, Timestamp end_date, String description, Timestamp created_at) {
        this.id = id;
        this.discount_code = discount_code;
        this.discount_name = discount_name;
        this.discount_percent = discount_percent;
        this.start_date = start_date;
        this.end_date = end_date;
        this.description = description;
        this.created_at = created_at;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDiscount_code() {
        return discount_code;
    }

    public void setDiscount_code(String discount_code) {
        this.discount_code = discount_code;
    }

    public String getDiscount_name() {
        return discount_name;
    }

    public void setDiscount_name(String discount_name) {
        this.discount_name = discount_name;
    }

    public int getDiscount_percent() {
        return discount_percent;
    }

    public void setDiscount_percent(int discount_percent) {
        this.discount_percent = discount_percent;
    }

    public Timestamp getStart_date() {
        return start_date;
    }

    public void setStart_date(Timestamp start_date) {
        this.start_date = start_date;
    }

    public Timestamp getEnd_date() {
        return end_date;
    }

    public void setEnd_date(Timestamp end_date) {
        this.end_date = end_date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Timestamp getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Timestamp created_at) {
        this.created_at = created_at;
    }

    public String getAppliedScopeNames() {
        return appliedScopeNames;
    }

    public void setAppliedScopeNames(String appliedScopeNames) {
        this.appliedScopeNames = appliedScopeNames;
    }

    public List<Integer> getAppliedCategoryIds() {
        return appliedCategoryIds;
    }

    public void setAppliedCategoryIds(List<Integer> appliedCategoryIds) {
        this.appliedCategoryIds = appliedCategoryIds;
    }

    public List<Integer> getAppliedProductTypeIds() {
        return appliedProductTypeIds;
    }

    public void setAppliedProductTypeIds(List<Integer> appliedProductTypeIds) {
        this.appliedProductTypeIds = appliedProductTypeIds;
    }

    public boolean isActive() {
        if (start_date == null || end_date == null) return false;

        long now = System.currentTimeMillis();
        long start = start_date.getTime();
        long end = end_date.getTime();

        return now >= start && now <= end;
    }
}
