package com.wx.genealogy.system.entity;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;

import java.io.Serializable;
import java.net.URL;
import java.util.Date;

/**
 * <p>
 * 用户
 * </p>
 *
 * @author ${author}
 * @since 2021-09-07
 */
public class User extends Model<User> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    @ExcelIgnore
    private Integer id;

    /**
     * openid
     */
    @ExcelIgnore
    private String openid;


    /**
     * 姓名
     */
    @ExcelIgnore
    private String nickName;

    /**
     * 真实姓名
     */
    @ExcelProperty("用户名称")
    @ColumnWidth(15)
    private String realName;

    /**
     * 性别
     */
    @ExcelProperty("用户性别")
    @ColumnWidth(15)
    @TableField(exist = false)
    private String sexStr;

    @ExcelIgnore
    private Integer sex;

    /**
     * 米
     */
    @ExcelProperty("大米数量")
    @ColumnWidth(15)
    private Integer rice;

    /**
     * 斗
     */
    @ExcelProperty("斗数量")
    @ColumnWidth(15)
    private Integer dou;

//    /**
//     * 头像
//     */
//    @ExcelProperty("用户头像")
//    @ColumnWidth(15)
//    @TableField(exist = false)
//    private URL avatarUrl;

    @ExcelIgnore
    private String avatar;

    /**
     * 是否实名认证（0待审核1通过 默认0）
     */
    @ExcelProperty("是否实名认证")
    @ColumnWidth(25)
    @TableField(exist = false)
    private String isCertificationStr;

    @ExcelIgnore
    private Integer isCertification;
    @ExcelIgnore
    private Integer familyIndex;

    /**
     * 身份证号码
     */
    @ExcelProperty("身份证号码")
    @ColumnWidth(20)
    private String idcardNo;

    /**
     * 正面
     */
    @ExcelProperty("身份证正面")
    @ColumnWidth(20)
    @TableField(exist = false)
    private URL idcardFrontImgUrl;

    @ExcelIgnore
    private String idcardFrontImg;

    /**
     * 背面
     */
    @ExcelProperty("身份证背面")
    @ColumnWidth(20)
    @TableField(exist = false)
    private URL idcardBehindImgUrl;

    @ExcelIgnore
    private String idcardBehindImg;


    @ExcelIgnore
    private Long status;




    @ExcelIgnore
    private String secondName;



    @ExcelIgnore
    private Integer clean;
    /**
     * 最后登录时间
     */
    @ExcelIgnore
    private Date lastLoginTime;

    @TableField(exist = false)
    @ExcelIgnore
    private DouDeliver douDeliver;

    public DouDeliver getDouDeliver() {
        return douDeliver;
    }

    public void setDouDeliver(DouDeliver douDeliver) {
        this.douDeliver = douDeliver;
    }

    public Integer getId() {
        return id;
    }

    public User setId(Integer id) {
        this.id = id;
        return this;
    }

    public String getOpenid() {
        return openid;
    }

    public User setOpenid(String openid) {
        this.openid = openid;
        return this;
    }

    public String getAvatar() {
        return avatar;
    }

    public User setAvatar(String avatar) {
        this.avatar = avatar;
        return this;
    }

    public Long getStatus() {
        return status;
    }

    public void setStatus(Long status) {
        this.status = status;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }
    public String getSecondName(){return secondName;}
    public void setSecondName(String secondName){this.secondName = secondName;}
    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public Integer getRice() {
        return rice;
    }

    public void setRice(Integer rice) {
        this.rice = rice;
    }

    public Integer getClean() {
        return clean;
    }
    public void setClean(Integer clean) {
        this.clean = clean;
    }

    public Integer getDou() {
        return dou;
    }

    public void setDou(Integer dou) {
        this.dou = dou;
    }

    public Date getLastLoginTime() {
        return lastLoginTime;
    }

    public User setLastLoginTime(Date lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
        return this;
    }

    public String getIdcardFrontImg() {
        return idcardFrontImg;
    }

    public void setIdcardFrontImg(String idcardFrontImg) {
        this.idcardFrontImg = idcardFrontImg;
    }

    public String getIdcardBehindImg() {
        return idcardBehindImg;
    }

    public void setIdcardBehindImg(String idcardBehindImg) {
        this.idcardBehindImg = idcardBehindImg;
    }

    public String getIdcardNo() {
        return idcardNo;
    }

    public void setIdcardNo(String idcardNo) {
        this.idcardNo = idcardNo;
    }

    public Integer getIsCertification() {
        return isCertification;
    }

    public void setIsCertification(Integer isCertification) {
        this.isCertification = isCertification;
    }



    public Integer getFamily_index() {
        return familyIndex;
    }

    public void setFamily_index(Integer familyIndex) {
        this.familyIndex = familyIndex;
    }




    public String getSexStr() {
        return null!=sex && !"".equals(sex) && sex.equals("1") ? "男" : "女";
    }

    public void setSexStr(String sexStr) {
        this.sexStr = sexStr;
    }

//    public URL getAvatarUrl() {
//        URL url = null;
//        if(null != this.avatar && !"".equals(this.avatar)) {
//            try {
//                url = new URL(this.avatar);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//        return url;
//    }
//
//    public void setAvatarUrl(URL avatarUrl) {
//        this.avatarUrl = avatarUrl;
//    }

    public URL getIdcardFrontImgUrl() {
        URL url = null;
        if(null != this.idcardFrontImg && !"".equals(this.idcardFrontImg)) {
            try {
                url = new URL(this.idcardFrontImg);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return url;
    }

    public void setIdcardFrontImgUrl(URL idcardFrontImgUrl) {
        this.idcardFrontImgUrl = idcardFrontImgUrl;
    }

    public URL getIdcardBehindImgUrl() {
        URL url = null;
        if(null != this.idcardBehindImg && !"".equals(this.idcardBehindImg)) {
            try {
                url = new URL(this.idcardBehindImg);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return url;
    }

    public void setIdcardBehindImgUrl(URL idcardBehindImgUrl) {
        this.idcardBehindImgUrl = idcardBehindImgUrl;
    }

    public String getIsCertificationStr() {
        return null == isCertification || isCertification == 0 ? "待审核" : "已通过";
    }

    public void setIsCertificationStr(String isCertificationStr) {
        this.isCertificationStr = isCertificationStr;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", openid=" + openid +
                ", avatar=" + avatar +
                ", sex=" + sex +
                ", lastLoginTime=" + lastLoginTime + ", status=" + status +
                "}";
    }
}
