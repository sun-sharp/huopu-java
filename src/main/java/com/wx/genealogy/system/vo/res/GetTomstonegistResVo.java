package com.wx.genealogy.system.vo.res;

import com.wx.genealogy.system.entity.TombstoneGift;
import com.wx.genealogy.system.entity.TombstoneUsergift;
import com.wx.genealogy.system.entity.User;
import lombok.Data;

@Data
public class GetTomstonegistResVo {
    private TombstoneGift tombstoneGift;
    private TombstoneUsergift tombstoneUsergift;
    private User user;
}
