package com.ensak.project.foodscanner.activity;


import android.content.Context;


import com.nextinnovation.pt.barcodescanner.R;


/**
 * @author herau
 */
public enum NutrimentLevel {
    LOW, MODERATE, HIGH;



    /**
     * get the localize text of a nutriment level
     * @param context to fetch localised strings
     * @return The localised word for the nutrition amount. If nutritionAmount is neither low,
     * moderate nor high, return nutritionAmount
     */
    public String getLocalize(Context context){
        switch (this){
            case LOW:
                return context.getString(R.string.txtNutritionLevelLow);
            case MODERATE:
                return context.getString(R.string.txtNutritionLevelModerate);
            case HIGH:
                return context.getString(R.string.txtNutritionLevelHigh);
            default:
                return null;
        }
    }

    public int getImageLevel(int i) {
        int drawable = 0;

        switch (i) {
            case 0:
                drawable = R.drawable.moderate;
                break;
            case 1:
                drawable = R.drawable.moderate;
                break;
            case 2:
                drawable = R.drawable.moderate;
                break;
            default:
                // no job
                break;
        }

        return drawable;
    }
}
