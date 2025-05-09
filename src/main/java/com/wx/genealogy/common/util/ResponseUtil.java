package com.wx.genealogy.common.util;

import com.wx.genealogy.common.domin.JsonResult;

import java.util.HashMap;

/**
 * @ClassName ResponseUtil
 * @Author hangyi
 * @Data 2020/5/26 16:10
 * @Description 响应请求的工具类。简化返回编码和消息的封装。
 * @Version 1.0
 **/
public class ResponseUtil {
    public static final int OK = 200;
    public static final String OK_MSG = "成功";

    public static final int UNKOWN_EXCEPTION = -1;
    public static final String EXCEPTION_MSG = "操作失败";

    public static final int NOT_LOGIN = 401;
    public static final String NOT_LOGIN_MSG = "未登录";

    public static final int PARAMETER_NOT_VALID = 400;
    public static final String PARAMETER_NOT_VALID_MSG = "参数不合法";

    public static final int FAIL = 405;
    public static final String FAIL_MSG = "失败";

    public static final int NOT_FOUND = 404;
    public static final String FOUND_MSG = "未发现";


    public static final int NOT_DATA = 1101;
    public static final String NOT_DATA_MSG = "查询无结果！";

    public static final int ERROR_ADD = 1102;
    public static final String ERROR_ADD_MSG = "添加失败";

    public static final int ERROR_UPDATE = 1103;
    public static final String ERROR_UPDATE_MSG = "修改失败";

    public static final int ERROR_DELETE = 1104;
    public static final String ERROR_DELETE_MSG = "删除失败";

    public static final int ERROR_OPERATE = 1105;
    public static final String ERROR_OPERATE_MSG = "操作失败";

    public static final int ERROR_EXPORT_NO_DATA = 1106;
    public static final String ERROR_EXPORT_NO_DATA_MSG = "导出数据为空！";

    public static final int ERROR_EXPORT_FAILD = 1107;
    public static final String ERROR_EXPORT_FAILD_MSG = "导出文件失败，请联系网站管理员！";

    private static final HashMap<Integer, String> CODE_MAP;

    static {
        CODE_MAP = init();
    }

    /**
     * 根据响应码生成JsonResult对象
     *
     * @param code
     * @return
     */
    public static JsonResult buildResult(Integer code) {
        return new JsonResult(code);
    }

    public static JsonResult buildResult(Integer code, String msg) {
        return new JsonResult(code, msg);
    }

    public static JsonResult buildResult(Integer code, String msg, Object data) {
        return new JsonResult(code, msg, data);
    }

    /**
     * 获得对应的响应消息
     *
     * @param code
     * @return
     */
    public static String getMsg(int code) {
        return CODE_MAP.get(code);
    }

    /**
     * @return com.wx.genealogy.common.domin.JsonResult
     * @Author hangyi
     * @Description 成功
     * @Date 16:38 2020/5/27
     * @Param []
     **/
    public static JsonResult ok() {
        return new JsonResult(OK, getMsg(OK));
    }

    public static JsonResult ok(String msg) {
        JsonResult jsonResult = new JsonResult(OK);
        jsonResult.setMsg(msg);
        return jsonResult;
    }

    public static JsonResult ok(String msg, Object data) {
        JsonResult jsonResult = new JsonResult(OK);
        jsonResult.setMsg(msg);
        jsonResult.setData(data);
        return jsonResult;
    }

    public static JsonResult ok(int i) {
        JsonResult jsonResult = new JsonResult(OK);
        jsonResult.setMsg(i > 0 ? OK_MSG : EXCEPTION_MSG);
        return jsonResult;
    }


    /**
     * @return com.wx.genealogy.common.domin.JsonResult
     * @Author hangyi
     * @Description 失败
     * @Date 16:39 2020/5/27
     * @Param []
     **/
    public static JsonResult fail() {
        return new JsonResult(FAIL, getMsg(FAIL));
    }

    public static JsonResult fail(String msg) {
        JsonResult jsonResult = new JsonResult(FAIL);
        jsonResult.setMsg(msg);
        return jsonResult;
    }

    public static JsonResult fail(int code,String msg) {
        JsonResult jsonResult = new JsonResult(code);
        jsonResult.setMsg(msg);
        return jsonResult;
    }

    /**
     * @return com.wx.genealogy.common.domin.JsonResult
     * @Author hangyi
     * @Description 未登陆
     * @Date 16:40 2020/5/27
     * @Param []
     **/
    public static JsonResult noLogin() {
        return new JsonResult(NOT_LOGIN, getMsg(NOT_LOGIN));
    }

    /**
     * @return com.wx.genealogy.common.domin.JsonResult
     * @Author hangyi
     * @Description 未知错误
     * @Date 16:41 2020/5/27
     * @Param []
     **/
    public static JsonResult unkownException(String msg) {
        return new JsonResult(UNKOWN_EXCEPTION, msg);
    }

    /**
     * @return com.wx.genealogy.common.domin.JsonResult
     * @Author hangyi
     * @Description 参数不合法
     * @Date 16:46 2020/5/27
     * @Param []
     **/
    public static JsonResult parameterNotValid() {
        return new JsonResult(PARAMETER_NOT_VALID, getMsg(PARAMETER_NOT_VALID));
    }

    private static HashMap<Integer, String> init() {
        HashMap<Integer, String> map = new HashMap<>(64);

        map.put(NOT_DATA, NOT_DATA_MSG);
        map.put(UNKOWN_EXCEPTION, EXCEPTION_MSG);
        map.put(ERROR_UPDATE, ERROR_UPDATE_MSG);
        map.put(ERROR_DELETE, ERROR_DELETE_MSG);
        map.put(ERROR_ADD, ERROR_ADD_MSG);
        map.put(ERROR_OPERATE, ERROR_OPERATE_MSG);
        map.put(ERROR_EXPORT_NO_DATA, ERROR_EXPORT_NO_DATA_MSG);
        map.put(ERROR_EXPORT_FAILD, ERROR_EXPORT_FAILD_MSG);
        map.put(PARAMETER_NOT_VALID, PARAMETER_NOT_VALID_MSG);
        map.put(NOT_FOUND, FOUND_MSG);
        map.put(OK, OK_MSG);
        map.put(NOT_LOGIN, NOT_LOGIN_MSG);
        map.put(FAIL, FAIL_MSG);
        return map;
    }


}
