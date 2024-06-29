package com.example.models;

public class Group {
    private String group_name;
    private String description;


    public Group(String group_name, String description) {
        this.group_name = group_name;
        this.description = description;
    }


    public String getGroup_name() {
        return group_name;
    }

    public void setGroup_name(String group_name) {
        this.group_name = group_name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    @Override
    public String toString() {
        return "Group Name: " + group_name +"\n" +
                "Description: " + description;
    }
}
