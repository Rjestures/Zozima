package code.entity;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ProductData implements Serializable {
            String id,
            productName,
            productPrice,
            discounType,
            discountValue,
            finalPrice,
            unitValue,
            description,
                    Type,
                    shipping_charge,
                    addedType,
                    addedById,
                    sku_code,
                    status;



    List<UnitNameData> unitNames = new ArrayList<>();
    List<ProductImage> productImages = new ArrayList<>();
    List<ProductImage> setimage = new ArrayList<>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }




    public String getType() {
        return Type;
    }

    public void setType(String type) {
        this.Type = type;
    }



    public String getSku_code() {
        return sku_code;
    }

    public void setSku_code(String sku_code) {
        this.Type = sku_code;
    }



    public String getShipping_charge() {
        return shipping_charge;
    }

    public void setShipping_charge(String shipping_charge1) {
        this.shipping_charge = shipping_charge1;
    }

    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }


    public String getAddedType() {
        return addedType;
    }
    public void setAddedType(String addedType) {
        this.addedType = addedType;
    }

    public String getAddedById() {
        return addedById;
    }
    public void setAddedById(String addedById) {
        this.addedById = addedById;
    }



    public String getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }

    public String getDiscounType() {
        return discounType;
    }

    public void setDiscounType(String discounType) {
        this.discounType = discounType;
    }

    public String getDiscountValue() {
        return discountValue;
    }

    public void setDiscountValue(String discountValue) {
        this.discountValue = discountValue;
    }

    public String getFinalPrice() {
        return finalPrice;
    }

    public void setFinalPrice(String finalPrice) {
        this.finalPrice = finalPrice;
    }

    public String getUnitValue() {
        return unitValue;
    }

    public void setUnitValue(String unitValue) {
        this.unitValue = unitValue;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<UnitNameData> getUnitNames() {
        return unitNames;
    }

    public void setUnitNames(List<UnitNameData> unitNames) {
        this.unitNames = unitNames;
    }

    public List<ProductImage> getProductImages() {
        return productImages;
    }

    public List<ProductImage> getPimage() {
        return setimage;
    }

    public void setProductImages(List<ProductImage> productImages) {
        this.productImages = productImages;
    }
    public void setimage(List<ProductImage> setimage) {
        this.setimage = setimage;
    }
    public static List<ProductData> createJsonInList(String str) {
        Gson gson = new Gson();
        Type type = new TypeToken<List<ProductData>>() {
        }.getType();
        return gson.fromJson(str, type);
    }
}

