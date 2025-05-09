package com.wx.genealogy.system.vo.res;
import com.wx.genealogy.system.entity.Trade;
import com.wx.genealogy.system.entity.User;
import lombok.Data;

import java.util.List;

@Data
public class TradeResVo {

    Trade trade;

    User user;
}
