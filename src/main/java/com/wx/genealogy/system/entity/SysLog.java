package com.wx.genealogy.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 日志表
 * </p>
 *
 * @author ${author}
 * @since 2021-05-29
 */
public class SysLog extends Model<SysLog> {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 操作
     */
    private String operation;

    /**
     * 方法名
     */
    private String method;

    /**
     * 参数
     */
    private String params;

    /**
     * 用户IP
     */
    private String ip;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;


    public Integer getId() {
        return id;
    }

    public SysLog setId(Integer id) {
        this.id = id;
        return this;
    }

    public String getUserName() {
        return userName;
    }

    public SysLog setUserName(String userName) {
        this.userName = userName;
        return this;
    }

    public String getOperation() {
        return operation;
    }

    public SysLog setOperation(String operation) {
        this.operation = operation;
        return this;
    }

    public String getMethod() {
        return method;
    }

    public SysLog setMethod(String method) {
        this.method = method;
        return this;
    }

    public String getParams() {
        return params;
    }

    public SysLog setParams(String params) {
        this.params = params;
        return this;
    }

    public String getIp() {
        return ip;
    }

    public SysLog setIp(String ip) {
        this.ip = ip;
        return this;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public SysLog setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
        return this;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "SysLog{" +
        "id=" + id +
        ", userName=" + userName +
        ", operation=" + operation +
        ", method=" + method +
        ", params=" + params +
        ", ip=" + ip +
        ", createTime=" + createTime +
        "}";
    }
}
