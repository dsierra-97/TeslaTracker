package com.sierrapor.teslatracker.data;

import androidx.annotation.NonNull;
import androidx.annotation.Size;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Entity(tableName = "teslas", indices = {@Index(value = "plate", unique = true)})
public class Tesla implements Serializable {

    public enum players{
        ERIKA,
        MARINA,
        DIEGO,
        MANU,
        SUSI
    }
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int id;
    @Size(max = 8)
    @ColumnInfo(name = "plate")
    private String plate;
    @ColumnInfo(name = "color")
    private String color;
    @ColumnInfo(name = "foreign")
    private boolean foreign;
    @ColumnInfo(name = "country")
    private String country;

    @ColumnInfo(name = "number_times_seen")
    private int numberTimesSeen = 1;
    @ColumnInfo(name = "first_time_seen")
    private LocalDate firstTimeSeen;
    @ColumnInfo(name = "last_time_seen")
    private LocalDate lastTimeSeen;
    @ColumnInfo(name = "seen_by")
    private List<players> seenBy;

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

    public int getNumberTimesSeen() {return numberTimesSeen;}

    public void setNumberTimesSeen(int numberTimesSeen) {this.numberTimesSeen = numberTimesSeen;}

    public LocalDate getFirstTimeSeen() {return firstTimeSeen;}

    public void setFirstTimeSeen(LocalDate firstTimeSeen) {this.firstTimeSeen = firstTimeSeen;}

    public LocalDate getLastTimeSeen() {return lastTimeSeen;}

    public void setLastTimeSeen(LocalDate lastTimeSeen) {this.lastTimeSeen = lastTimeSeen;}

    public List<players> getSeenBy() {
        return seenBy;
    }

    public void setSeenBy(List<players> seenBy) {
        this.seenBy = seenBy;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tesla tesla = (Tesla) o;
        return id == tesla.id && foreign == tesla.foreign && numberTimesSeen == tesla.numberTimesSeen && Objects.equals(plate, tesla.plate) && Objects.equals(color, tesla.color) && Objects.equals(country, tesla.country);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, plate, color, foreign, country, numberTimesSeen);
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
                ", numberTimesSeen=" + numberTimesSeen +
                '}';
    }
}
