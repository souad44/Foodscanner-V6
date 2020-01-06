package com.ensak.project.foodscanner.model;

import java.util.Locale;

public enum ProductImageField {
    FRONT, INGREDIENTS, NUTRITION, OTHER;

    @Override

    public String toString() {
        return this.name().toLowerCase(Locale.getDefault());
    }
}
