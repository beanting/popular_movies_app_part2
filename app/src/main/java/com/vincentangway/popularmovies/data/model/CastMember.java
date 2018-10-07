package com.vincentangway.popularmovies.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CastMember {
    @SerializedName("cast_id")
    @Expose
    private Integer castId;
    @SerializedName("character")
    @Expose
    private String character;
    @SerializedName("credit_id")
    @Expose
    private String creditId;
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("order")
    @Expose
    private Integer order;
    @SerializedName("profile_path")
    @Expose
    private String profilePath;

    /**

     * @return castId
     */
    public Integer getCastId() {
        return castId;
    }

    /**
     * @param castId
     */
    public void setCastId(Integer castId) {
        this.castId = castId;
    }

    /**
     * @return character
     */
    public String getCharacter() {
        return character;
    }

    /**
     * @param character
     */
    public void setCharacter(String character) {
        this.character = character;
    }

    /**
     * @return creditId
     */
    public String getCreditId() {
        return creditId;
    }

    /**
     * @param creditId
     */
    public void setCreditId(String creditId) {
        this.creditId = creditId;
    }

    /**
     * @return id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return order
     */
    public Integer getOrder() {
        return order;
    }

    /**
     * @param order
     */
    public void setOrder(Integer order) {
        this.order = order;
    }

    /**
     * @return profilePath
     */
    public String getProfilePath() {
        return profilePath;
    }

    /**
     * @param profilePath
     */
    public void setProfilePath(String profilePath) {
        this.profilePath = profilePath;
    }
}
