package com.wx.genealogy.system.vo.req;

import com.wx.genealogy.common.util.ExcelUtils.ExcelImport;
import lombok.Data;

import java.util.Date;

@Data
public class FamilyPedigreeExcelVo {

    private static final long serialVersionUID = 1L;


    public FamilyPedigreeExcelVo() {
    }

    /** 人员父亲 */
    @ExcelImport("上级人员")
    private String father;


    @ExcelImport("家族代数")
    private Long iteration;

    /** 直属亲属 */
    @ExcelImport(value = "家族关系" ,kv = "0-直系;1-婚姻;2-表亲")
    private Long isDirect;

    /** 名字 */
    @ExcelImport("人员姓名")
    private String name;

    /** 性别 */
    @ExcelImport(value = "性别",kv = "0-男;1-女")
    private Long sex;

    /** 生日 */
    @ExcelImport("生日")
    private Date birthday;

    /** 学历 */
    @ExcelImport("学历")
    private String education;


    /** 血型 */
    @ExcelImport("血型")
    private String blood;

    /** 电话 */
    @ExcelImport(value = "电话" ,maxLength = 11)
    private String phone;

    /** 是否上门女婿 */
    @ExcelImport(value = "是否上门女婿" ,kv = "0-否;1-是")
    private Long isVisit;

    @ExcelImport(value = "婚姻" ,kv = "0-未婚;1-已婚")
    private Long marriage;

    @ExcelImport(value = "配偶名字")
    private String mateName;

    @Override
    public String toString() {
        return "FamilyPedigreeExcelVo{" +
                "father='" + father + '\'' +
                ", iteration=" + iteration +
                ", isDirect=" + isDirect +
                ", name='" + name + '\'' +
                ", sex=" + sex +
                ", birthday=" + birthday +
                ", education='" + education + '\'' +
                ", blood='" + blood + '\'' +
                ", phone='" + phone + '\'' +
                ", isVisit=" + isVisit +
                ", marriage=" + marriage +
                ", mateName=" + mateName +
                '}';
    }

    public Long getMarriage() {
        return marriage;
    }

    public void setMarriage(Long marriage) {
        this.marriage = marriage;
    }

    public String getMateName() {
        return mateName;
    }

    public void setMateName(String mateName) {
        this.mateName = mateName;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getFather() {
        return father;
    }

    public void setFather(String father) {
        this.father = father;
    }

    public Long getIteration() {
        return iteration;
    }

    public void setIteration(Long iteration) {
        this.iteration = iteration;
    }

    public Long getIsDirect() {
        return isDirect;
    }

    public void setIsDirect(Long isDirect) {
        this.isDirect = isDirect;
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
}
