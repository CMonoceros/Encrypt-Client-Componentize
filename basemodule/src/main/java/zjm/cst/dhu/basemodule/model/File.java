package zjm.cst.dhu.basemodule.model;

import com.google.gson.annotations.Expose;

import java.io.Serializable;

/**
 * Created by zjm on 3/2/2017.
 */

public class File implements Serializable{
    @Expose
    private int id;
    @Expose
    private String name;
    @Expose
    private String uploadTime;
    @Expose
    private String lastDownloadTime;
    @Expose
    private String lastEncryptTime;
    @Expose
    private int owner;
    @Expose
    private String size;
    @Expose
    private String path;

    public File(int id, String name, int owner, String uploadTime, String size, String path) {
        this.id = id;
        this.name = name;
        this.owner = owner;
        this.uploadTime = uploadTime;
        this.size = size;
        this.path = path;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setLastDownloadTime(String lastDownloadTime) {
        this.lastDownloadTime = lastDownloadTime;
    }

    public String getLastDownloadTime() {
        return lastDownloadTime;
    }

    public void setLastEncryptTime(String lastEncryptTime) {
        this.lastEncryptTime = lastEncryptTime;
    }


    public String getLastEncryptTime() {
        return lastEncryptTime;
    }


    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setUploadTime(String uploadTime) {
        this.uploadTime = uploadTime;
    }

    public String getUploadTime() {
        return uploadTime;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getSize() {
        return size;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }

    public void setOwner(int owner) {
        this.owner = owner;
    }

    public int getOwner() {
        return owner;
    }
}
