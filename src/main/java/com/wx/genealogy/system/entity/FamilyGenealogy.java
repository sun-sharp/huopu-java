package com.wx.genealogy.system.entity;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.wx.genealogy.common.util.BizUtil;
import com.wx.genealogy.system.vo.res.FamilyGenealogyImgResVo;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 家谱图
 * </p>
 *
 * @author ${author}
 * @since 2021-10-20
 */
@EqualsAndHashCode
public class FamilyGenealogy extends Model<FamilyGenealogy> {

    private static final long serialVersionUID = 1L;


    @ExcelIgnore
    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;

    @ExcelIgnore
    private Integer uid;

    @ExcelProperty({"${family}", "${exportTime}", "家族代数"})
    @ColumnWidth(20)
    private Integer generation;

    @ExcelProperty({"${family}", "${exportTime}", "状态"})
    @TableField(exist = false)
    private String isAliveDesc;

    @ExcelProperty({"${family}", "${exportTime}", "家族人员姓名"})
    @ColumnWidth(25)
    private String genealogyName;

    @ExcelProperty({"${family}", "${exportTime}", "人员家谱"})
    @ColumnWidth(80)
    private String chart;

    /**
     *是否在世（0否 1是 默认1）
     */
    @ExcelIgnore
    private Integer isAlive;

    /**
     * 家族id
     */
    @ExcelIgnore
    private Integer familyId;

    /**
     * 家族成员id
     */
    @ExcelIgnore
    private Integer familyUserId;

    /**
     * 用户id
     */
    @ExcelIgnore
    private Integer userId;

    /**
     * 和家族关系1直亲2婚姻3表亲
     */
    @ExcelIgnore
    private Integer relation;

    /**
     * 身份：1：直系，2：婚姻
     */
    @ExcelIgnore
    private Integer identity;

    @ExcelIgnore
    private String audio;

    /**
     * 第几代
     */


    /**
     * 性别1男2女
     */
    @ExcelIgnore
    private Integer sex;

    @ExcelIgnore
    private Date createTime;

    @ExcelIgnore
    private Integer familyLianId;

    /**
     * 审核状态（0待审核 1审核通过 2拒绝）
     */
    @ExcelIgnore
    private Integer status;

    /**
     * 申请备注
     */
    @ExcelIgnore
    private String applyRemark;

    /**
     * 拒绝原因
     */
    @ExcelIgnore
    private String refuse;

    /**
     *申请人名称
     */
    @ExcelIgnore
    private String applyName;

    /**
     *上级id
     */
//    @ExcelProperty("上一代编号")
    @ExcelIgnore
    private Integer parentId;

    /**
     *头像图
     */
    @ExcelIgnore
    private String headImg;




    @ExcelIgnore
    private Integer relevance;

    /**
     * 代数人数
     */
    @ExcelIgnore
    @TableField(exist = false)
    private Integer generationNum;



    /**
     * 认领审核人
     */
    @TableField(exist = false)
    @ExcelIgnore
    private FamilyGenealogyReceive familyGenealogyReceive;


    /**
     * 排行
     */
    @ExcelIgnore
    private Integer ranking;
    /** 生日 */
    @ExcelIgnore
    private String birthday;
    /** 忌日 */
    @ExcelIgnore
    private String mourningDay;

    @TableField(exist = false)
    private String mourningDayStr;

    @TableField(exist = false)
    private String rankingStr;

    /** 配偶 */
    @ExcelIgnore
    private String spouse;


    @TableField(exist = false)
    private Integer distance;

    private String introduction;//简介

    private String tags;//标签

    private String resume;//简历

    @TableField(exist = false)
    private List<FamilyGenealogyImgResVo> imgList;

    @TableField(exist = false)
    private FamilyGenealogyEditApply apply;

    @TableField(exist = false)
    private FamilyGenealogyEditApply applyPu;

    public FamilyGenealogyEditApply getApplyPu() {
        return applyPu;
    }

    public void setApplyPu(FamilyGenealogyEditApply applyPu) {
        this.applyPu = applyPu;
    }

    public FamilyGenealogyEditApply getApply() {
        return apply;
    }

    public void setApply(FamilyGenealogyEditApply apply) {
        this.apply = apply;
    }

    public List<FamilyGenealogyImgResVo> getImgList() {
        return imgList;
    }

    public void setImgList(List<FamilyGenealogyImgResVo> imgList) {
        this.imgList = imgList;
    }

    public String getResume() {
        return resume;
    }

    public void setResume(String resume) {
        this.resume = resume;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public Integer getDistance() {
        return distance;
    }

    public void setDistance(Integer distance) {
        this.distance = distance;
    }

    public String getSpouse() {
        return spouse;
    }

    public void setSpouse(String spouse) {
        this.spouse = spouse;
    }

    public String getMourningDayStr() {
        return StringUtils.isNotBlank(mourningDay)?" 至 "+mourningDay:"";
    }

    public String getRankingStr() {
        String rankingStr = BizUtil.getRankingStr(getRanking());
        return rankingStr;
    }

    public Integer getRanking() {
        return ranking;
    }

    public void setRanking(Integer ranking) {
        this.ranking = ranking;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getMourningDay() {
        return mourningDay;
    }

    public void setMourningDay(String mourningDay) {
        this.mourningDay = mourningDay;
    }

    public Integer getId() {
        return id;
    }

    public FamilyGenealogy setId(Integer id) {
        this.id = id;
        return this;
    }

    public String getIsAliveDesc() {
        return isAliveDesc;
    }

    public void setIsAliveDesc(String isAliveDesc) {
        this.isAliveDesc = isAliveDesc;
    }

    public Integer getFamilyId() {
        return familyId;
    }

    public FamilyGenealogy setFamilyId(Integer familyId) {
        this.familyId = familyId;
        return this;
    }

    public Integer getFamilyUserId() {
        return familyUserId;
    }

    public FamilyGenealogy setFamilyUserId(Integer familyUserId) {
        this.familyUserId = familyUserId;
        return this;
    }

    public Integer getUserId() {
        return userId;
    }

    public FamilyGenealogy setUserId(Integer userId) {
        this.userId = userId;
        return this;
    }

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public Integer getGeneration() {
        return generation;
    }

    public FamilyGenealogy setGeneration(Integer generation) {
        this.generation = generation;
        return this;
    }

    public String getGenealogyName() {
        return genealogyName;
    }

    public FamilyGenealogy setGenealogyName(String genealogyName) {
        this.genealogyName = genealogyName;
        return this;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getChart() {
        return chart;
    }

    public void setChart(String chart) {
        this.chart = chart;
    }

    public FamilyGenealogy() {
    }

    public String getAudio() {
        return audio;
    }

    public void setAudio(String audio) {
        this.audio = audio;
    }

    @Override
    public String toString() {
        return "FamilyGenealogy{" +
                "id=" + id +
                ", generation=" + generation +
                ", uid=" + uid +
                ", familyId=" + familyId +
                ", familyUserId=" + familyUserId +
                ", userId=" + userId +
                ", relation=" + relation +
                ", identity=" + identity +
                ", audio='" + audio + '\'' +
                ", genealogyName='" + genealogyName + '\'' +
                ", sex=" + sex +
                ", createTime=" + createTime +
                ", familyLianId=" + familyLianId +
                ", status=" + status +
                ", applyRemark='" + applyRemark + '\'' +
                ", refuse='" + refuse + '\'' +
                ", applyName='" + applyName + '\'' +
                ", parentId=" + parentId +
                ", headImg='" + headImg + '\'' +
                ", isAlive=" + isAlive +
                ", relevance=" + relevance +
                ", generationNum=" + generationNum +
                ", chart='" + chart + '\'' +
                ", familyGenealogyReceive=" + familyGenealogyReceive +
                ", spouse=" + spouse +
                ", ranking=" + ranking +
                ", birthday=" + birthday +
                ", mourningDay=" + mourningDay +
                '}';
    }

    public FamilyGenealogyReceive getFamilyGenealogyReceive() {
        return familyGenealogyReceive;
    }

    public void setFamilyGenealogyReceive(FamilyGenealogyReceive familyGenealogyReceive) {
        this.familyGenealogyReceive = familyGenealogyReceive;
    }

    public void setRelation(Integer relation) {
        this.relation = relation;
    }

    public void setIdentity(Integer identity) {
        this.identity = identity;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }


    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getRelation() {
        return relation;
    }

    public Integer getIdentity() {
        return identity;
    }

    public Integer getSex() {
        return sex;
    }
    public Integer getFamilyLianId() {
        return familyLianId;
    }

    public void setFamilyLianId(Integer familyLianId) {
        this.familyLianId = familyLianId;
    }
    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    public Integer getGenerationNum() {
        return generationNum;
    }

    public void setGenerationNum(Integer generationNum) {
        this.generationNum = generationNum;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getApplyRemark() {
        return applyRemark;
    }

    public void setApplyRemark(String applyRemark) {
        this.applyRemark = applyRemark;
    }

    public String getRefuse() {
        return refuse;
    }

    public void setRefuse(String refuse) {
        this.refuse = refuse;
    }

    public String getApplyName() {
        return applyName;
    }

    public void setApplyName(String applyName) {
        this.applyName = applyName;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }


    public String getHeadImg() {
        return headImg;
    }

    public void setHeadImg(String headImg) {
        this.headImg = headImg;
    }

    public Integer getIsAlive() {
        return isAlive;
    }

    public void setIsAlive(Integer isAlive) {
        this.isAlive = isAlive;
    }

    public Integer getRelevance() {
        return relevance;
    }

    public void setRelevance(Integer relevance) {
        this.relevance = relevance;
    }
}
