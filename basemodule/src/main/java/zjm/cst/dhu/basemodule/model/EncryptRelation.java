package zjm.cst.dhu.basemodule.model;

import com.google.gson.annotations.Expose;

import java.io.Serializable;

/**
 * Created by zjm on 3/23/2017.
 */

public class EncryptRelation implements Serializable {
    @Expose
    private int id;
    @Expose
    private int fileId;
    @Expose
    private int typeId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getFileId() {
        return fileId;
    }

    public void setFileId(int fileId) {
        this.fileId = fileId;
    }

    public int getTypeId() {
        return typeId;
    }

    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }
}
