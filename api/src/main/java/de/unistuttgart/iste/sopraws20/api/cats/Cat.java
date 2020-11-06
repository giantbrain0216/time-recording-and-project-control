package de.unistuttgart.iste.sopraws20.api.cats;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class Cat {

    private long id;
    @NotNull
    @Size(min = 1, max = 40)
    private String name;
    @Min(0)
    @Max(40)
    private int ageInYears;
    private String picUrl;

    public Cat(long id, String name, int ageInYears, String picUrl) {
        this.id = id;
        this.name = name;
        this.ageInYears = ageInYears;
        this.picUrl = picUrl;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAgeInYears() {
        return ageInYears;
    }

    public void setAgeInYears(int ageInYears) {
        this.ageInYears = ageInYears;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

}
