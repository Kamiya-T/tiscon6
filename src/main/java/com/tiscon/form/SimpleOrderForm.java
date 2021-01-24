package com.tiscon.form;

import com.tiscon.validator.Numeric;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class SimpleOrderForm {
    @Numeric
    @NotBlank
    private String oldPostalCode;

    @Numeric
    @NotBlank
    private String newPostalCode;

    @Numeric
    @NotBlank
    private String box;

    @Numeric
    @NotBlank
    private String bed;

    @Numeric
    @NotBlank
    private String bicycle;

    @Numeric
    @NotBlank
    private String washingMachine;

    @NotNull
    private boolean washingMachineInstallation;

    @NotBlank
    private String  movingDate;

    public String getOldPostalCode() {
        return oldPostalCode;
    }

    public void setOldPostalCode(String box) {
        this.oldPostalCode = box;
    }

    public String getNewPostalCode() {
        return newPostalCode;
    }

    public void setNewPostalCode(String box) {
        this.newPostalCode = box;
    }

    public String getBox() {
        return box;
    }

    public void setmovingDate(String movingDate) {
        this.movingDate = movingDate;
    }

    public String getmovingDate() {
        return movingDate;
    }

    public void setBox(String box) {
        this.box = box;
    }

    public String getBed() {
        return bed;
    }

    public void setBed(String bed) {
        this.bed = bed;
    }

    public String getBicycle() {
        return bicycle;
    }

    public void setBicycle(String bicycle) {
        this.bicycle = bicycle;
    }

    public String getWashingMachine() {
        return washingMachine;
    }

    public void setWashingMachine(String washingMachine) {
        this.washingMachine = washingMachine;
    }

    public boolean getWashingMachineInstallation() {
        return washingMachineInstallation;
    }

    public void setWashingMachineInstallation(boolean washingMachineInstallation) {
        this.washingMachineInstallation = washingMachineInstallation;
    }
}