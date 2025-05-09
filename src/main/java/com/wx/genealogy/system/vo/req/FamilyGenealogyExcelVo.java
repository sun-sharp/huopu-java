package com.wx.genealogy.system.vo.req;

import com.wx.genealogy.common.util.ExcelUtils.ExcelImport;
import lombok.Data;

/**
 * @ClassName FamilyGenealogyInsertReqVo
 * @Author weixin
 * @Data 2021/10/21 14:38
 * @Description
 * @Version 1.0
 **/
@Data
public class FamilyGenealogyExcelVo {

    private static final long serialVersionUID = 1L;


    public FamilyGenealogyExcelVo() {
    }

//    @ExcelImport(value = "家族id" ,kv = "0-否;1-是")
//    private Integer familyId;

    @ExcelImport(value = "序号")
    private Integer uid;

    @ExcelImport(value = "父亲")
    private String father;

     @ExcelImport(value = "父级序号")
    private Integer fatherUid;

    /**
     * 和家族关系1直亲2婚姻3表亲
     */
    @ExcelImport(value = "家族关系" ,kv = "1-直亲;2-婚姻;3-表亲")
    private Integer relation;

    /**
     * 身份：1：直系，2：婚姻
     */


    /**
     * 第几代
     */
    @ExcelImport(value = "第几代")
    private Integer generation;

    /**
     * 家谱名
     */
    @ExcelImport(value = "成员姓名")
    private String genealogyName;

    /**
     * 性别1男2女
     */
    @ExcelImport(value = "性别" ,kv = "1-男;2-女")
    private Integer sex;

    @ExcelImport(value = "是否在世" ,kv = "0-否;1-是")
    private Integer isAlive;

//    @ExcelImport(value = "排行" ,kv = "1-老大;2-老二;3-老三;4-老四;5-老五;6-老六;7-老七;8-老八;9-老九;")
    @ExcelImport(value = "排行")
    private Integer ranking;
    /** 0妻子/1丈夫*/
    @ExcelImport(value = "丈夫妻子")
    private String spouse;
    /** 生日 */

    @ExcelImport(value = "生日")
    private String birthday;
    /** 忌日 */

    @ExcelImport(value = "去世日期")
    private String mourningDay;


    public FamilyGenealogyExcelVo( Integer relation, Integer generation, String genealogyName, Integer sex) {
        this.relation = relation;
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

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Integer getUid() {
        return uid;
    }




    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public String getFather() {
        return father;
    }

    public void setFather(String father) {
        this.father = father;
    }

    public FamilyGenealogyExcelVo(Integer uid, String father, Integer fatherUid, Integer relation, Integer identity, Integer generation, String genealogyName, Integer sex, Integer isAlive, Integer ranking, Integer type, String birthday, String mourningDay) {
        this.uid = uid;
        this.father = father;
        this.fatherUid = fatherUid;
        this.relation = relation;
        this.generation = generation;
        this.genealogyName = genealogyName;
        this.sex = sex;
        this.isAlive = isAlive;
        this.ranking = ranking;
        this.spouse = spouse;
        this.birthday = birthday;
        this.mourningDay = mourningDay;
    }


    @Override
    public String toString() {
        return "FamilyGenealogyExcelVo{" +
                "uid=" + uid +
                ", father='" + father + '\'' +
                ", fatherUid=" + fatherUid +
                ", relation=" + relation +
                ", generation=" + generation +
                ", genealogyName='" + genealogyName + '\'' +
                ", sex=" + sex +
                ", isAlive=" + isAlive +
                ", ranking=" + ranking +
                ", spouse=" + spouse +
                ", birthday=" + birthday +
                ", mourningDay=" + mourningDay +
                '}';
    }

    public Integer getFatherUid() {
        return fatherUid;
    }

    public void setFatherUid(Integer fatherUid) {
        this.fatherUid = fatherUid;
    }

    public Integer getIsAlive() {
        return isAlive;
    }

    public void setIsAlive(Integer isAlive) {
        this.isAlive = isAlive;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public Integer getRanking() {
        return ranking;
    }

    public void setRanking(Integer ranking) {
        this.ranking = ranking;
    }

    public String getSpouse() {
        return spouse;
    }

    public void setSpouse(String spouse) {
        this.spouse = spouse;
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
}
