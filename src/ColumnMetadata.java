
public class ColumnMetadata {
    public boolean active = true;

    private String columnName;
    private int columnType;

    public ColumnMetadata(String columnName, int columnType) {
        this.columnName = columnName;
        this.columnType = columnType;
    }

    public String getColumnName() {
        return columnName;
    }

    public int getColumnType() {
        return columnType;
    }
}
