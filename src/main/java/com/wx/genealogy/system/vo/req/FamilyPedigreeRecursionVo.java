package com.wx.genealogy.system.vo.req;

import com.wx.genealogy.system.entity.FamilyPedigree;

import java.util.Date;
import java.util.List;

public class FamilyPedigreeRecursionVo {

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    private  Long id;

    //节点id
    private Long uuid;



    public Long getUuid() {
        return uuid;
    }

    public void setUuid(Long uuid) {
        this.uuid = uuid;
    }

    /** 父级id */
    private Long pid;

    /** 家族id */
    private Long familyId;

    /** 直属亲属 */
    private Long isDirect;

    /** 第几代 */
    private Long iteration;

    /** 头像 */
//    @Excel(name = "头像")
    private String headimg;

    /** 名字 */
    private String name;

    /** 性别 */
    private Long sex;

    /** 生日 */
    private Date birthday;

    /** 学历 */
    private String education;

    /** 血型 */
    private String blood;

    /** 电话 */
    private String phone;

    /** 是否上门女婿 */
    private Long isVisit;

    //    是否在世
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

    private Long mateId;

    private Long marriage;

    private Date updateTime;

    private List<FamilyPedigreeRecursionVo> children;

    private FamilyPedigree wife;

    private Boolean isCenter;

    private Boolean isLocateCenter;

    public FamilyPedigree getWife() {
        return wife;
    }

    public void setWife(FamilyPedigree wife) {
        this.wife = wife;
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

    @Override
    public String toString() {
        return "FamilyPedigreeRecursionVo{" +
                "id=" + id +
                ", uuid=" + uuid +
                ", pid=" + pid +
                ", familyId=" + familyId +
                ", isDirect=" + isDirect +
                ", iteration=" + iteration +
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
                ", mateId=" + mateId +
                ", marriage=" + marriage +
                ", updateTime=" + updateTime +
                ", children=" + children +
                ", wife=" + wife +
                ", isCenter=" + isCenter +
                ", isLocateCenter=" + isLocateCenter +
                '}';
    }

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

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Long getIsVisit() {
        return isVisit;
    }

    public void setIsVisit(Long isVisit) {
        this.isVisit = isVisit;
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

    public Boolean getCenter() {
        return isCenter;
    }

    public void setCenter(Boolean center) {
        isCenter = center;
    }

    public Boolean getLocateCenter() {
        return isLocateCenter;
    }

    public void setLocateCenter(Boolean locateCenter) {
        isLocateCenter = locateCenter;
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

    public List<FamilyPedigreeRecursionVo> getChildren() {
        return children;
    }

    public void setChildren(List<FamilyPedigreeRecursionVo> children) {
        this.children = children;
    }

}
