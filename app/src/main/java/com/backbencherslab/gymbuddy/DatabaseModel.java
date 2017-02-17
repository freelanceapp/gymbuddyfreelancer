package com.backbencherslab.gymbuddy;

public class DatabaseModel {
    private String name;
    private String address;
    private Double branch;
    private Double email;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {return address; }

    public void setAddress(String address) {
        this.address = address;
    }

    public Double getBranch() {
        return branch;
    }

    public void setBranch(Double branch) {
        this.branch = branch;
    }

    public Double getEmail() {
        return email;
    }

    public void setEmail(Double email) {
        this.email = email;
    }
}
