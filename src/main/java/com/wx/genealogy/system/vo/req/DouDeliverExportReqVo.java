package com.wx.genealogy.system.vo.req;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 斗投递对象 dou_deliver
 *
 * @author leo
 * @date 2024-07-05
 */
public class DouDeliverExportReqVo {

    @NotNull(message = "请选择投递记录！")
    private List<Long> ids;

    public List<Long> getIds() {
        return ids;
    }

    public void setIds(List<Long> ids) {
        this.ids = ids;
    }
}
