package com.deloitte.smt.dto;

import java.util.Date;
import java.util.List;

/**
 * Created by myelleswarapu on 14-04-2017.
 */
public class SearchDto {

    private List<String> statuses;
    private Date createdDate;
    private List<String> ingredients;
    private List<String> products;
    private List<String> licenses;

    public List<String> getStatuses() {
        return statuses;
    }

    public void setStatuses(List<String> statuses) {
        this.statuses = statuses;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public List<String> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<String> ingredients) {
        this.ingredients = ingredients;
    }

    public List<String> getProducts() {
        return products;
    }

    public void setProducts(List<String> products) {
        this.products = products;
    }

    public List<String> getLicenses() {
        return licenses;
    }

    public void setLicenses(List<String> licenses) {
        this.licenses = licenses;
    }
}
