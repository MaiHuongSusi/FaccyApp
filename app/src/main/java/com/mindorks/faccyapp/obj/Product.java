package com.mindorks.faccyapp.obj;

/**
 * Created by martin on 03/05/2018.
 */

public class Product {
    private String namePro;
    private int imgPro;
    private String nameCat;

    public String getNamePro() {
        return namePro;
    }

    public void setNamePro(String namePro) {
        this.namePro = namePro;
    }

    public int getImgPro() {
        return imgPro;
    }

    public void setImgPro(int imgPro) {
        this.imgPro = imgPro;
    }

    public String getNameCat() {
        return nameCat;
    }

    public void setNameCat(String nameCat) {
        this.nameCat = nameCat;
    }

    public Product(String namePro, int imgPro, String nameCat) {
        this.namePro = namePro;
        this.imgPro = imgPro;
        this.nameCat = nameCat;
    }

    @Override
    public String toString() {
        return "Product{" +
                "namePro='" + namePro + '\'' +
                ", imgPro=" + imgPro +
                ", nameCat='" + nameCat + '\'' +
                '}';
    }
}
