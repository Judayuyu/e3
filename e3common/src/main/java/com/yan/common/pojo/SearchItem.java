package com.yan.common.pojo;

import java.io.Serializable;
    //用于联合查表item和item_cat生成索引数据
public class SearchItem implements Serializable {
     private String id;
     private String title;
     private String sellPoint;
     private long price;
     private String image;
     private String categoryName;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    public String getImage() {
        return image;
    }

        public String getSellPoint() {
            return sellPoint;
        }

        public void setSellPoint(String sellPoint) {
            this.sellPoint = sellPoint;
        }

        public String getCategoryName() {
            return categoryName;
        }

        public void setCategoryName(String categoryName) {
            this.categoryName = categoryName;
        }

        public void setImage(String image) {
        this.image = image;
    }
        public String[] getImages() {
            if (image != null && !"".equals(image)) {
                String[] strings = image.split(",");
                return strings;
            }
            return null;
        }

}
