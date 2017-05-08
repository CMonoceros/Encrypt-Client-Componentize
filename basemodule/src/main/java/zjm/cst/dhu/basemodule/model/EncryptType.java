package zjm.cst.dhu.basemodule.model;

import com.google.gson.annotations.Expose;

import java.io.Serializable;

/**
 * Created by zjm on 2017/3/3.
 */

public class EncryptType implements Serializable {
    @Expose
    private int id;
    @Expose
    private String name;
    @Expose
    private String description;

    public EncryptType(int id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }


}
