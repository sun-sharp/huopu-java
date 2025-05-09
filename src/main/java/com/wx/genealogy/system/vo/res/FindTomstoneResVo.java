package com.wx.genealogy.system.vo.res;

import com.wx.genealogy.system.entity.*;
import lombok.Data;

@Data
public class FindTomstoneResVo {
    private Tombstone tombstone;

    private TombstoneSweep tombstoneSweep;
    private TombstoneMessage tombstoneMessage;
    private TombstoneFlower tombstoneFlower;
    private FamilyUser familyUser;

}
