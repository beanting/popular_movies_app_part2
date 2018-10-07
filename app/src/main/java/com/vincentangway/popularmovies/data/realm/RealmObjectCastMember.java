package com.vincentangway.popularmovies.data.realm;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class RealmObjectCastMember extends RealmObject {
    @SerializedName("character")
    @Expose
    private String character;
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

    public int getMovieID() {
        return movieID;
    }

    public void setMovieID(int movieID) {
        this.movieID = movieID;
    }

    public int getUniqID() {
        return uniqID;
    }

    public void setUniqID(int uniqID) {
        this.uniqID = uniqID;
    }

    private int movieID;

    @PrimaryKey
    private int uniqID;

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
     *
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
