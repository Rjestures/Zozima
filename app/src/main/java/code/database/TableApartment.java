package code.database;

public class TableApartment {

    static final String SQL_CREATE_APARTMENT = ("CREATE TABLE apartment ("
            + apartmentColumn.id + " VARCHAR,"
            + apartmentColumn.name + " VARCHAR,"
            + apartmentColumn.cityId + " VARCHAR,"
            + apartmentColumn.status + " VARCHAR"
            + ")");

    public static final String apartment = "apartment";

    public enum apartmentColumn {
        id,
        name,
        cityId,
        status
    }
}