package code.entity;

import java.io.Serializable;

public class UnitNameData implements Serializable {
    String id,
            unit_name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUnit_name() {
        return unit_name;
    }

    public void setUnit_name(String unit_name) {
        this.unit_name = unit_name;
    }
}
