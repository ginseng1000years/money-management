package com.moneymanagement.core.dto;

public class CategoryDTO {

    private String id;
    private String name;
    private String image;
    private String type;
    private CategoryDTO parentCategory;
    private String description;

    public CategoryDTO() {
    }

    public CategoryDTO(String id, String name, String image, String type, CategoryDTO parentCategory, String description) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.type = type;
        this.parentCategory = parentCategory;
        this.description = description;
    }



    // Getters and setters

    public String getId() {
        return id;
    }

    public void setId(String id) {
       this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public CategoryDTO getParentCategory() {
        return parentCategory;
    }

    public void setParentCategory(CategoryDTO parentCategory) {
        this.parentCategory = parentCategory;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
