package com.wx.genealogy.system.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.wx.genealogy.common.util.ExcelUtils.ExcelImport;
import lombok.Data;

import java.util.Date;

@Data
public class FamilyPedigree {

    private static final long serialVersionUID = 1L;

    /** id */
    private Long id;

    public FamilyPedigree() {
    }

    /** 父级id */
//    @Excel(name = "父级id")
    @ExcelImport("人员父亲")
    private Long pid;

    /** 家族id */
//    @Excel(name = "家族id")
    @ExcelImport("家族id")
    private Long familyId;

    /** 直属亲属 */
//    @Excel(name = "直属亲属")
    @ExcelImport(value = "是否直属亲属" ,kv = "0-否;1-是")
    private Long isDirect;

    /** 第几代 */
    @ExcelImport("第几代")
    private Long iteration;

//    配偶id
    private Long mateId;

    private Long marriage;

    /** 头像 */
//    @Excel(name = "头像")
    private String headimg;

    /** 名字 */
//    @Excel(name = "名字")
    @ExcelImport("人员姓名")
    private String name;

    /** 性别 */
    @ExcelImport(value = "性别",kv = "0-男;1-女")
    private Long sex;

    /** 生日 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @ExcelImport("生日")
    private Date birthday;

    /** 学历 */
//    @Excel(name = "学历")
    @ExcelImport("学历")
    private String education;

    /** 血型 */
//    @Excel(name = "血型")
    @ExcelImport("血型")
    private String blood;

    /** 电话 */
    @ExcelImport(value = "电话" ,maxLength = 11)
    private String phone;

    /** 是否上门女婿 */
    @ExcelImport(value = "是否上门女婿" ,kv = "0-否;1-是")
    private Long isVisit;

    //    是否在世
    @ExcelImport(value = "是否上门女婿" ,kv = "0-否;1-是")
    private Long isSurvival;

//    谱
    private String spectrum;

    /** 用户id */
//    @Excel(name = "用户id")
    private Long userId;

    /** 用户标识符 */
//    @Excel(name = "用户标识符")
    private Long isUser;

    /** 管理员标识 */
//    @Excel(name = "管理员标识")
    private Long isAdmin;

    /** 是否删除 */
//    @Excel(name = "是否删除")
    private String isDelete;

    /** 姓名首字母 */
//    @Excel(name = "姓名首字母")
    private String initial;


    private String remark;

    private String createBy;

    private Date createTime;

    private String updateBy;

    private Date updateTime;

    public Long getMateId() {
        return mateId;
    }

    public void setMateId(Long mateId) {
        this.mateId = mateId;
    }

    public Long getMarriage() {
        return marriage;
    }

    public void setMarriage(Long marriage) {
        this.marriage = marriage;
    }

    public Long getIsSurvival() {
        return isSurvival;
    }

    public void setIsSurvival(Long isSurvival) {
        this.isSurvival = isSurvival;
    }

    public String getSpectrum() {
        return spectrum;
    }

    public void setSpectrum(String spectrum) {
        this.spectrum = spectrum;
    }

    @Override
    public String toString() {
        return "FamilyPedigree{" +
                "id=" + id +
                ", pid=" + pid +
                ", familyId=" + familyId +
                ", isDirect=" + isDirect +
                ", iteration=" + iteration +
                ", mateId=" + mateId +
                ", marriage=" + marriage +
                ", headimg='" + headimg + '\'' +
                ", name='" + name + '\'' +
                ", sex=" + sex +
                ", birthday=" + birthday +
                ", education='" + education + '\'' +
                ", blood='" + blood + '\'' +
                ", phone='" + phone + '\'' +
                ", isVisit=" + isVisit +
                ", isSurvival=" + isSurvival +
                ", spectrum='" + spectrum + '\'' +
                ", userId=" + userId +
                ", isUser=" + isUser +
                ", isAdmin=" + isAdmin +
                ", isDelete='" + isDelete + '\'' +
                ", initial='" + initial + '\'' +
                ", remark='" + remark + '\'' +
                ", createBy='" + createBy + '\'' +
                ", createTime=" + createTime +
                ", updateBy='" + updateBy + '\'' +
                ", updateTime=" + updateTime +
                '}';
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPid() {
        return pid;
    }

    public void setPid(Long pid) {
        this.pid = pid;
    }

    public Long getFamilyId() {
        return familyId;
    }

    public void setFamilyId(Long familyId) {
        this.familyId = familyId;
    }

    public Long getIsDirect() {
        return isDirect;
    }

    public void setIsDirect(Long isDirect) {
        this.isDirect = isDirect;
    }

    public Long getIteration() {
        return iteration;
    }

    public void setIteration(Long iteration) {
        this.iteration = iteration;
    }

    public String getHeadimg() {
        return headimg;
    }

    public void setHeadimg(String headimg) {
        this.headimg = headimg;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getSex() {
        return sex;
    }

    public void setSex(Long sex) {
        this.sex = sex;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    public String getBlood() {
        return blood;
    }

    public void setBlood(String blood) {
        this.blood = blood;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Long getIsVisit() {
        return isVisit;
    }

    public void setIsVisit(Long isVisit) {
        this.isVisit = isVisit;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getIsUser() {
        return isUser;
    }

    public void setIsUser(Long isUser) {
        this.isUser = isUser;
    }

    public Long getIsAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(Long isAdmin) {
        this.isAdmin = isAdmin;
    }

    public String getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(String isDelete) {
        this.isDelete = isDelete;
    }

    public String getInitial() {
        return initial;
    }

    public void setInitial(String initial) {
        this.initial = initial;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public FamilyPedigree(Long id, Long pid, Long familyId, Long isDirect, Long iteration, String headimg, String name, Long sex, Date birthday, String education, String blood, String phone, Long isVisit, Long userId, Long isUser, Long isAdmin, String delete, String initial, String remark, String createBy, Date createTime, String updateBy, Date updateTime) {
        this.id = id;
        this.pid = pid;
        this.familyId = familyId;
        this.isDirect = isDirect;
        this.iteration = iteration;
        this.headimg = headimg;
        this.name = name;
        this.sex = sex;
        this.birthday = birthday;
        this.education = education;
        this.blood = blood;
        this.phone = phone;
        this.isVisit = isVisit;
        this.userId = userId;
        this.isUser = isUser;
        this.isAdmin = isAdmin;
        this.isDelete = isDelete;
        this.initial = initial;
        this.remark = remark;
        this.createBy = createBy;
        this.createTime = createTime;
        this.updateBy = updateBy;
        this.updateTime = updateTime;
    }
}
