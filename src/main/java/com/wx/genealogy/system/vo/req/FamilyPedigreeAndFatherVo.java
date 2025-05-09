package com.wx.genealogy.system.vo.req;

import lombok.Data;

@Data
public class FamilyPedigreeAndFatherVo {

    private static final long serialVersionUID = 1L;

    /** id */
    private Long id;
    /** 姓名 */
    private String name;
    /** 父级名字 */
    private String father;
    /** 家族关系 */
    private Long isDirect;

    private Long marriage;

    private String mateName;

    /** 家族代数*/
    private Long iteration;

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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFather() {
        return father;
    }

    public void setFather(String father) {
        this.father = father;
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


    @Override
    public String toString() {
        return "FamilyPedigreeAndFatherVo{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", father='" + father + '\'' +
                ", isDirect=" + isDirect +
                ", marriage=" + marriage +
                ", mateName='" + mateName + '\'' +
                ", iteration=" + iteration +
                '}';
    }
}
