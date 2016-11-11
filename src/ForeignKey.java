public class ForeignKey {
    private String[] myFields;
    private String[] foreignFields;
    private String foreignTable;

    public String[] getMyFields() {
        return myFields;
    }

    public void setMyFields(String[] myFields) {
        this.myFields = myFields;
    }

    public String[] getForeignFields() {
        return foreignFields;
    }

    public void setForeignFields(String[] foreignFields) {
        this.foreignFields = foreignFields;
    }

    public String getForeignTable() {
        return foreignTable;
    }

    public void setForeignTable(String foreignTable) {
        this.foreignTable = foreignTable;
    }
}
