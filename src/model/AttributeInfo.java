package model;

public class AttributeInfo {
    private String name;
    private boolean embedded;
    private String realName;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isEmbedded() {
        return embedded;
    }

    public void setEmbedded(boolean embedded) {
        this.embedded = embedded;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }
}
