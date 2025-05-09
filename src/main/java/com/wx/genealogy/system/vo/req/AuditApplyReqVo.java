package com.wx.genealogy.system.vo.req;

/**
 * <p>
 * 用户编辑申请表
 * </p>
 *
 * @author ${author}
 * @since 2021-09-07
 */
public class AuditApplyReqVo {

    private static final long serialVersionUID = 1L;

    private Integer id;

    private String auditReason;

    private Integer status;

    private Integer userId;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAuditReason() {
        return auditReason;
    }

    public void setAuditReason(String auditReason) {
        this.auditReason = auditReason;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
