package com.wx.genealogy.system.vo.req;

/**
 * <p>
 * 用户编辑申请表
 * </p>
 *
 * @author ${author}
 * @since 2021-09-07
 */
public class FamilyGenealogyEditApplyReqVo {

    private static final long serialVersionUID = 1L;

    private Integer familyGenealogyId;

    /** 申请理由*/
    private String reason;

    /**
     * 申请类型：0关闭申请；1打开申请
     */
    private Integer type;

    private Integer userId;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getFamilyGenealogyId() {
        return familyGenealogyId;
    }

    public void setFamilyGenealogyId(Integer familyGenealogyId) {
        this.familyGenealogyId = familyGenealogyId;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

}
