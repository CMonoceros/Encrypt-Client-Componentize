package zjm.cst.dhu.basemodule.model;

import com.google.gson.annotations.Expose;

import java.io.Serializable;

/**
 * Created by zjm on 2017/2/24.
 */

public class User implements Serializable{
    @Expose
    private int id;
    @Expose
    private String name;
    @Expose
    private String password;
    @Expose
    private String registerTime;
    @Expose
    private Byte sex;
    @Expose
    private Integer tel;
    @Expose
    private Integer qq;
    @Expose
    private String email;
    @Expose
    private Integer permission;

    public User(int id,String password){
        this.id=id;
        this.password=password;
    }

    public User(String name,String password){
        this.name=name;
        this.password=password;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRegisterTime() {
        return registerTime;
    }

    public void setRegisterTime(String registerTime) {
        this.registerTime = registerTime;
    }

    public Byte getSex() {
        return sex;
    }

    public void setSex(Byte sex) {
        this.sex = sex;
    }

    public Integer getTel() {
        return tel;
    }

    public void setTel(Integer tel) {
        this.tel = tel;
    }

    public Integer getQq() {
        return qq;
    }

    public void setQq(Integer qq) {
        this.qq = qq;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getPermission() {
        return permission;
    }

    public void setPermission(Integer permission) {
        this.permission = permission;
    }


}
