package com.wx.genealogy.system.vo.req;

public class FamilyUserAndPedigreeVo {
    private Long userId;
    private String name;
    private Long pedigreeId;
    private Long familyId;

    @Override
    public String toString() {
        return "FamilyUserAndPedigreeVo{" +
                "userId=" + userId +
                ", name='" + name + '\'' +
                ", pedigreeId=" + pedigreeId +
                ", familyId=" + familyId +
                '}';
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getPedigreeId() {
        return pedigreeId;
    }

    public void setPedigreeId(Long pedigreeId) {
        this.pedigreeId = pedigreeId;
    }

    public Long getFamilyId() {
        return familyId;
    }

    public void setFamilyId(Long familyId) {
        this.familyId = familyId;
    }
}
