package code.database;

import java.io.Serializable;

public class setter implements Serializable {

    String Size;
    String age ;
    String address;

    public setter() {

    }

    public setter(String size) {
     this.Size=size;
    }

    public String getSize() {
        return Size;
    }

    public void setName(String size) {
        Size = Size;
    }


}
