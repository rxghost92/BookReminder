package com.example.bookreminder.ui.search;

public class Model {

    String title, image, description, description_cardview, autore, type;
    String numberpages;

    //constructor
    public Model() {
    }

    //getter and setters press Alt+Insert

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription_cardview() {
        return description_cardview;
    }

    public void setDescription_cardview(String description_cardview) {
        this.description_cardview = description_cardview;
    }

    public String getAutore() {
        return autore;
    }

    public void setAutore(String autore) {
        this.autore = autore;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getNumberpages() {
        return numberpages;
    }

    public void setNumberpages (String  numberpages) {
        this.numberpages = numberpages;
    }

}