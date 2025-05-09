package com.wx.genealogy.system.vo.req;

import lombok.Data;

import java.util.Date;

/**
 * @ClassName GetLogReqVo
 * @Author hangyi
 * @Data 2020/7/22 12:33
 * @Description
 * @Version 1.0
 **/
@Data
public class GetLogReqVo {

    private Integer page;

    private Integer limit;

    private Date startTime;

    private Date endTime;
}
