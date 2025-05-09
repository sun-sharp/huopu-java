package com.wx.genealogy;


public class FamilyGenealogy {
    private Integer uid;
    private Integer parentId; // 使用 Integer 允许 parentId 为 null
    private int generationNum;
    private int generation;

    public FamilyGenealogy(int uid, Integer parentId, int generation) {
        this.uid = uid;
        this.parentId = parentId;
        this.generation = generation;
        this.generationNum = 0;
    }

    public Integer getUid() {
        return uid;
    }

    public Integer getParentId() {
        return parentId;
    }

    @Override
    public String toString() {
        return "FamilyGenealogy{" +
                "uid=" + uid +
                ", parentId=" + parentId +
                ", generationNum=" + generationNum +
                ", generation=" + generation +
                '}';
    }

    public int getGenerationNum() {
        return generationNum;
    }

    public int getGeneration() {
        return generation;
    }

    public void setGenerationNum(int generationNum) {
        this.generationNum = generationNum;
    }
}

