package com.sierrapor.teslatracker.achievements.achievement;

public abstract class Achievement {

    private String name;
    private String description;
    private boolean unlocked;
    private boolean hidden;

    public Achievement(String name, String description, boolean hidden) {
        this.name = name;
        this.description = description;
        this.unlocked = false;
        this.hidden = hidden;
    }

    public abstract boolean checkCondition();

    public void checkAndUnlock() {
        if (checkCondition()) {
            unlocked = true;
        }
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

    public boolean isUnlocked() {
        return unlocked;
    }

    public void setUnlocked(boolean unlocked) {
        this.unlocked = unlocked;
    }

    public boolean isHidden() {
        return hidden;
    }

    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }
}
