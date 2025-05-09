package com.wx.genealogy.system.vo.res;


import com.wx.genealogy.system.entity.FamilyUser;
import com.wx.genealogy.system.entity.Tombstone;
import lombok.Data;

@Data
public class SelectTomstoneByfamilyIdResVo {
    private FamilyUser familyUser;

    private Tombstone tombstone;

}
