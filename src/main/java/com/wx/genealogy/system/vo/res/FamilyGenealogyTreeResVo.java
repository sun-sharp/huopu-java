package com.wx.genealogy.system.vo.res;

import com.wx.genealogy.common.util.BizUtil;

import java.util.List;

/**
 * @ClassName FamilyUserAndUserSelectResVo
 * @Author weixin
 * @Data 2021/9/9 12:29
 * @Description
 * @Version 1.0
 **/
public class FamilyGenealogyTreeResVo {

    private Integer familyId;

    private String name;

    private Integer sex;

    private Integer generation;

    private Integer parentId;

    private Integer uid;

    private String headImg;

    private Integer ranking;

    private Integer color;

    private String rankingStr;

    private Integer total;

    private List<FamilyGenealogyTreeResVo> children;

    public Integer getFamilyId() {
        return familyId;
    }

    public void setFamilyId(Integer familyId) {
        this.familyId = familyId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public Integer getGeneration() {
        return generation;
    }

    public void setGeneration(Integer generation) {
        this.generation = generation;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public String getHeadImg() {
        return headImg;
    }

    public void setHeadImg(String headImg) {
        this.headImg = headImg;
    }

    public Integer getRanking() {
        return ranking;
    }

    public void setRanking(Integer ranking) {
        this.ranking = ranking;
    }

    public Integer getColor() {
        return color;
    }

    public void setColor(Integer color) {
        this.color = color;
    }

    public String getRankingStr() {
        return BizUtil.getRankingStr(this.ranking);
    }

    public void setRankingStr(String rankingStr) {
        this.rankingStr = rankingStr;
    }

    public List<FamilyGenealogyTreeResVo> getChildren() {
        return children;
    }

    public void setChildren(List<FamilyGenealogyTreeResVo> children) {
        this.children = children;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }
}
