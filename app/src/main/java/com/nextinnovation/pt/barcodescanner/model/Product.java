package com.nextinnovation.pt.barcodescanner.model;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by PT on 2/9/2017.
 */

public class Product implements Serializable {

    @SerializedName("product_name_fr")
    @Expose
    private String labels;



    @SerializedName("nova_group")
    private String nova_group;
    @SerializedName("image_thumb_url")
    private String product_image;
    @SerializedName("nutriscore_grade")
    private String nutriscore_grade;

    @SerializedName("brands")
    private transient String fournisseur;
    @SerializedName("additives_original_tags")
    private ArrayList<String> a1=new ArrayList<String>();
    @SerializedName("image_url")
    private String imageUrl;
    @SerializedName("image_small_url")
    private String imageSmallUrl;
    @SerializedName("image_nutrition_url")
    private String imageNutritionUrl;
    @SerializedName("image_front_url")
    private String imageFrontUrl;
    @SerializedName("image_ingredients_url")
    private String imageIngredientsUrl;

    @SerializedName("code")
    @Expose
    private String code;
    @SerializedName("quantity")
    private String quantity;
    @SerializedName("countries")
    private String  countries;
    @SerializedName("nutrient_levels")
    HashMap<String, String>  nutrient_levels = new HashMap<String, String>();
    public Product(String title, int imageID){
        this.labels = title;
        this.imageID = imageID;
    }


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
    int imageID;

    public int getImageID() {
        return imageID;
    }

    public void setImageID(int imageID) {
        this.imageID = imageID;
    }

    @SerializedName("brands")
    @Expose
    private  String brands;

    public String getBrands() {
        return brands;
    }

    public Product(String title, String imageID){
        this.labels = title;
        this.product_image =imageID ;
    }


    public void setBrands(String brands) {
        this.brands = brands;
    }
    public Product(String code, String labels, String barcode, String brands,int img){
        this.code = code;
        this.labels = labels;
        this.code = barcode;
        this.imageID = img;
        this.brands = brands;

    }

    public String getQuantity() {
        return quantity;
    }

    public HashMap<String, String> getNutrient_levels() {
        return nutrient_levels;
    }

    public void setNutrient_levels(HashMap<String, String> nutrient_levels) {
        this.nutrient_levels = nutrient_levels;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getCountries() {
        return countries;
    }

    public void setCountries(String countries) {
        this.countries = countries;
    }

    @SerializedName("nutriments")
    HashMap<String, String> map = new HashMap<String, String>();

    public HashMap<String, String> getMap() {
        return map;
    }

    @SerializedName("ingredients_text")
    @Expose
    private String ingredients;
    private Map<String, Object> additionalProperties = new HashMap<>();




    private  String scanTime;
    private String scanDate ;

    public void setImageIngredientsUrl(String imageIngredientsUrl) {
        this.imageIngredientsUrl = imageIngredientsUrl;
    }

    public String getLabels() {
        return labels;
    }

    public void setLabels(String labels) {
        this.labels = labels;
    }

    public String getProduct_image() {
        return product_image;
    }

    public void setProduct_image(String product_image) {
        this.product_image = product_image;
    }

    public String getfournisseur(){return fournisseur;}
    public ArrayList getadditives( ){return a1;}
    public String getScanTime() {
        return scanTime;
    }

    public void setScanTime(String scanTime) {
        this.scanTime = scanTime;
    }

    public String getScanDate() {
        return scanDate;
    }

    public void setScanDate(String scanDate) {
        this.scanDate = scanDate;
    }

    public String getIngredients() {
        return ingredients;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

    public Product(String labels, String brands, String barcode) {
        this.labels = labels;
        this.brands = brands;
        this.code = barcode;
    }

    private String getImageUrl() {
        return imageUrl;
    }


    public String getImageIngredientsUrl() {
        return imageIngredientsUrl;
    }

    /**
     * @return The imageNutritionUrl
     */


    public void setNutriscore_grade(String nutriscore_grade) {
        this.nutriscore_grade = nutriscore_grade;
    }

    public String getNutriscore_grade() {
        return nutriscore_grade;
    }

    public String getNova_group() {
        return nova_group;
    }

    public void setNova_group(String nova_group) {
        this.nova_group = nova_group;
    }




    public Product(){

    }







}