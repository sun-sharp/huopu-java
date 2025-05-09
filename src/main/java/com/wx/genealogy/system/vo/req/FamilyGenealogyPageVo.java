package com.wx.genealogy.system.vo.req;

import com.wx.genealogy.system.entity.FamilyGenealogy;

import java.util.List;

public class FamilyGenealogyPageVo {

    private List<FamilyGenealogy> list;

    private Long total;


    public List<FamilyGenealogy> getList() {
        return list;
    }

    public void setList(List<FamilyGenealogy> list) {
        this.list = list;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    class InnerClass{


    }
}


