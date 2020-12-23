package com.example.kiddo;

public class GiftModel {
    private Integer id;
    private String name, description, stars_required, created_by, created_for,is_redeemed;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStars_required() {
        return stars_required;
    }

    public void setStars_required(String stars_required) {
        this.stars_required = stars_required;
    }

    public String getCreated_by() {
        return created_by;
    }

    public void setCreated_by(String created_by) {
        this.created_by = created_by;
    }

    public String getCreated_for() {
        return created_for;
    }

    public void setCreated_for(String created_for) {
        this.created_for = created_for;
    }

    public String getIs_redeemed() {
        return is_redeemed;
    }

    public void setIs_redeemed(String is_redeemed) {
        this.is_redeemed = is_redeemed;
    }
}
