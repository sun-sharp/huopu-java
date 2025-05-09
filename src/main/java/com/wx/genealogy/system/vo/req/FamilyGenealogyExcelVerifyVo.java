package com.wx.genealogy.system.vo.req;

import lombok.Data;

/**
 * @ClassName FamilyGenealogyInsertReqVo
 * @Author weixin
 * @Data 2021/10/21 14:38
 * @Description
 * @Version 1.0
 **/
@Data
public class FamilyGenealogyExcelVerifyVo {

    private static final long serialVersionUID = 1L;


    public FamilyGenealogyExcelVerifyVo() {
    }
    private Integer id;

    // 父级名字
    private String father;

    /**
     * 和家族关系1直亲2婚姻3表亲
     */
    private Integer relation;

    /**
     * 身份：1：直系，2：婚姻
     */
    private Integer identity;

    /**
     * 第几代
     */
    private Integer generation;

    /**
     * 家谱名
     */
    private String genealogyName;

    /**
     * 性别1男2女
     */
    private Integer sex;


    public FamilyGenealogyExcelVerifyVo(Integer relation, Integer identity, Integer generation, String genealogyName, Integer sex) {
        this.relation = relation;
        this.identity = identity;
        this.generation = generation;
        this.genealogyName = genealogyName;
        this.sex = sex;
    }
    public Integer getRelation() {
        return relation;
    }

    public void setRelation(Integer relation) {
        this.relation = relation;
    }


    @Override
    public String toString() {
        return "FamilyGenealogyExcelVerifyVo{" +
                "id=" + id +
                ", father='" + father + '\'' +
                ", relation=" + relation +
                ", identity=" + identity +
                ", generation=" + generation +
                ", genealogyName='" + genealogyName + '\'' +
                ", sex=" + sex +
                '}';
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Integer getId() {
        return id;
    }

    public FamilyGenealogyExcelVerifyVo(Integer id, String father, Integer relation, Integer identity, Integer generation, String genealogyName, Integer sex) {
        this.id = id;
        this.father = father;
        this.relation = relation;
        this.identity = identity;
        this.generation = generation;
        this.genealogyName = genealogyName;
        this.sex = sex;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFather() {
        return father;
    }

    public void setFather(String father) {
        this.father = father;
    }

    public Integer getIdentity() {
        return identity;
    }

    public void setIdentity(Integer identity) {
        this.identity = identity;
    }

    public Integer getGeneration() {
        return generation;
    }

    public void setGeneration(Integer generation) {
        this.generation = generation;
    }

    public String getGenealogyName() {
        return genealogyName;
    }

    public void setGenealogyName(String genealogyName) {
        this.genealogyName = genealogyName;
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }


}
