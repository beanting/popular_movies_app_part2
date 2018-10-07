package com.vincentangway.popularmovies.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Cast {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("cast")
    @Expose
    private List<CastMember> castMembers = new ArrayList<>();

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
     * @return cast
     */
    public List<CastMember> getCastMembers() {
        return castMembers;
    }

    /**
     * @param castMembers
     */
    public void setCastMembers(List<CastMember> castMembers) {
        this.castMembers = castMembers;
    }
}
