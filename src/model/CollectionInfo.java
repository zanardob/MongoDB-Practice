package model;

import java.util.ArrayList;

public class CollectionInfo {
    private String name;
    private ArrayList<AttributeInfo> attributes;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<AttributeInfo> getAttributes() {
        return attributes;
    }

    public void setAttributes(ArrayList<AttributeInfo> attributes) {
        this.attributes = attributes;
    }
}
