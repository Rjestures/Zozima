package code.database;

public class TableFavourite {
    static final String SQL_CREATE_FAVOURITE = ("CREATE TABLE favourite (" + favouriteColumn.catalogId + " VARCHAR" + ")");
    public static final String favourite = "favourite";

    public enum favouriteColumn {
        catalogId
    }
}
