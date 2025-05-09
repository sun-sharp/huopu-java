package com.wx.genealogy.system.vo.req;

import com.wx.genealogy.system.entity.FamilyUser;

import java.util.List;

public class FamilyUserPageVo {
    List<FamilyUser> list;

    Long total;

    public List<FamilyUser> getList() {
        return list;
    }

    public void setList(List<FamilyUser> familyUser) {
        this.list = familyUser;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }
}
