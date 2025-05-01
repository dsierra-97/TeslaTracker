package com.sierrapor.teslatracker.list.filter;

import com.sierrapor.teslatracker.data.Tesla;

import java.util.List;

public class FilterOptions {
    private String color;
    private String country;
    private boolean isForeign;
    private int minTimesSeen;
    private List<Tesla.players> seenBy;

    // Getters y Setters

    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }

    public String getCountry() { return country; }
    public void setCountry(String country) { this.country = country; }

    public boolean isForeign() { return isForeign; }
    public void setForeign(boolean foreign) { isForeign = foreign; }

    public int getMinTimesSeen() { return minTimesSeen; }
    public void setMinTimesSeen(int minTimesSeen) { this.minTimesSeen = minTimesSeen; }

    public List<Tesla.players> getSeenBy() { return seenBy; }
    public void setSeenBy(List<Tesla.players> seenBy) { this.seenBy = seenBy; }
}
