package com.sierrapor.teslatracker.achievements.achievement;

import com.sierrapor.teslatracker.data.TeslaViewModel;

import javax.inject.Inject;


public class NumericAchievement extends Achievement {
    private final int conditionNumber;
    private final TeslaViewModel teslaViewModel;

    @Inject
    public NumericAchievement(String name, String description, boolean hidden, int conditionNumber, TeslaViewModel teslaViewModel) {
        super(name, description, hidden);
        this.conditionNumber = conditionNumber;
        this.teslaViewModel = teslaViewModel;
    }

    @Override
    public boolean checkCondition() {

        return getTotalNumber() >= conditionNumber;
    }

    public int getTotalNumber(){

        return teslaViewModel.getAllTeslas().getValue() != null ? teslaViewModel.getAllTeslas().getValue().size() : 0;
    }
}
