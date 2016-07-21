package com.wfj.exception.dal.entity;

/**
 * Created with IntelliJ IDEA.
 * User: MaYong
 * Date: 2015-08-19
 * Time: 09:59:39
 * To change this template use File | Settings | File Templates.
 */

public class UserInfo {
    private String tel;  //
    private String userName;  //
    private Integer busiId;  //
    private Integer id;  //
    private String email;  //
    private String userCode;  //
    private Integer sysId;  //

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Integer getBusiId() {
        return busiId;
    }

    public void setBusiId(Integer busiId) {
        this.busiId = busiId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    public Integer getSysId() {
        return sysId;
    }

    public void setSysId(Integer sysId) {
        this.sysId = sysId;
    }

}