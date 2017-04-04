package com.deloitte.smt.entity;

import javax.persistence.Embeddable;

/**
 * Created by myelleswarapu on 04-04-2017.
 */
@Embeddable
public class Drug {

    private String ingredient;
    private String product;
    private String license;

    public String getIngredient() {
        return ingredient;
    }

    public void setIngredient(String ingredient) {
        this.ingredient = ingredient;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getLicense() {
        return license;
    }

    public void setLicense(String license) {
        this.license = license;
    }
}
