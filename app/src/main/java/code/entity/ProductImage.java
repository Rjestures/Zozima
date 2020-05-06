package code.entity;

import java.io.Serializable;
import java.util.List;

public class ProductImage implements Serializable {
    String productImage,setimage;


    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    public String getimage() {
        return setimage;
    }

    public void setimage(String setimage) {
        this.setimage = setimage;
    }
}
