import java.util.ArrayList;

public class ForeignKey {
    public boolean composesPrimary = false;

    private ArrayList<String> myFields;
    private ArrayList<String> foreignFields;
    private ArrayList<String> foreignPrimaries;
    private ArrayList<String> values;
    private String foreignTable;

    public ArrayList<String> getMyFields() {
        return myFields;
    }

    public void setMyFields(ArrayList<String> myFields) {
        this.myFields = myFields;
    }

    public ArrayList<String> getForeignFields() {
        return foreignFields;
    }

    public void setForeignFields(ArrayList<String> foreignFields) {
        this.foreignFields = foreignFields;
    }

    public String getForeignTable() {
        return foreignTable;
    }

    public void setForeignTable(String foreignTable) {
        this.foreignTable = foreignTable;
    }

    public ArrayList<String> getValues() {
        return values;
    }

    public void setValues(ArrayList<String> values) {
        this.values = values;
    }

    public ArrayList<String> getForeignPrimaries() {
        return foreignPrimaries;
    }

    public void setForeignPrimaries(ArrayList<String> foreignPrimaries) {
        this.foreignPrimaries = foreignPrimaries;
    }
}
