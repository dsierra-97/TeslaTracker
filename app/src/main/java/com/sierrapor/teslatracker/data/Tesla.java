package com.sierrapor.teslatracker.data;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Objects;

@Entity(tableName = "teslas")
public class Tesla {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int id;
    @ColumnInfo(name = "plate")
    private String plate;
    @ColumnInfo(name = "color")
    private String color;
    @ColumnInfo(name = "foreign")
    private boolean foreign;
    @ColumnInfo(name = "country")
    private String country;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPlate() {
        return plate;
    }

    public void setPlate(String plate) {
        this.plate = plate;
    }

    public boolean isForeign() {
        return foreign;
    }

    public void setForeign(boolean foreign) {
        this.foreign = foreign;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tesla tesla = (Tesla) o;
        return id == tesla.id && foreign == tesla.foreign && Objects.equals(plate, tesla.plate) && Objects.equals(color, tesla.color) && Objects.equals(country, tesla.country);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, plate, color, foreign, country);
    }

    @NonNull
    @Override
    public String toString() {
        return "Tesla{" +
                "id=" + id +
                ", plate='" + plate + '\'' +
                ", color='" + color + '\'' +
                ", foreign=" + foreign +
                ", country='" + country + '\'' +
                '}';
    }
}
