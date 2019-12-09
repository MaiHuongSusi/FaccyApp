package com.mindorks.faccyapp.obj;

/**
 * Created by martin on 03/05/2018.
 */

public class Category {
    private String nameCat;
    private int imgCat;

    public String getNameCat() {
        return nameCat;
    }

    public int getImgCat() {
        return imgCat;
    }

    public void setNameCat(String nameCat) {
        this.nameCat = nameCat;
    }

    public void setImgCat(int imgCat) {
        this.imgCat = imgCat;
    }

    public Category(String nameCat, int imgCat) {
        this.nameCat = nameCat;
        this.imgCat = imgCat;
    }

    @Override
    public String toString() {
        return "Category{" +
                "nameCat='" + nameCat + '\'' +
                ", imgCat=" + imgCat +
                '}';
    }
}
