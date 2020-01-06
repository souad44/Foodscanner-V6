package com.nextinnovation.pt.barcodescanner.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class JsonProduct  implements Serializable {

    @SerializedName("code")
    @Expose
    private String code;

    @SerializedName("status_verbose")
    @Expose
    private String status_verbose;
    @SerializedName("product")
    @Expose
    private Product product;
    @SerializedName("status")
    @Expose
    private String status;
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public String getStatus_verbose() {
        return status_verbose;
    }

    public void setStatus_verbose(String status_verbose) {
        this.status_verbose = status_verbose;
    }
}
