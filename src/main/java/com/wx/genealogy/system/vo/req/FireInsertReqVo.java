package com.wx.genealogy.system.vo.req;
import lombok.Data;

@Data
public class FireInsertReqVo {
    private Integer userId;
    private Integer firenumber;

    private String nickname;

    private String familyname;

    private Integer dounumber;
    private Integer year;
    private Integer amount;




}
