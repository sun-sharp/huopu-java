package com.wx.genealogy.common.exception;

import com.alibaba.excel.exception.ExcelAnalysisStopException;
import com.wx.genealogy.common.domin.JsonResult;
import com.wx.genealogy.common.util.ResponseUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MissingMatrixVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;

/**
 * @ClassName MyGlobalExceptionHandler
 * @Author hangyi
 * @Data 2020/5/26 15:58
 * @Description 全局异常处理
 * @Version 1.0
 **/
//@ControllerAdvice
//@ResponseBody
//@Slf4j
@RestControllerAdvice
public class MyGlobalExceptionHandler {

    /**
     * @return com.wx.genealogy.common.domin.JsonResult
     * @Author hangyi
     * @Description 处理参数缺失异常
     * @Date 16:58 2020/5/27
     * @Param [e]
     **/
    @ExceptionHandler(MissingMatrixVariableException.class)
    public JsonResult handleMissingMatrixVariableException(MissingMatrixVariableException e) {
        return ResponseUtil.parameterNotValid();
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public JsonResult handleMissingServletRequestParameterException(MissingServletRequestParameterException e) {
        return ResponseUtil.parameterNotValid();
    }

    /**
     * @return com.wx.genealogy.common.domin.JsonResult
     * @Author hangyi
     * @Description 处理空指针异常
     * @Date 17:02 2020/5/27
     * @Param [e]
     **/
    @ExceptionHandler(NullPointerException.class)
    public JsonResult handleNullPointerException(NullPointerException e) {

        return ResponseUtil.fail("数据不能为null1");
    }

    /**
     * @return com.wx.genealogy.common.domin.JsonResult
     * @Author hangyi
     * @Description 数据不是json格式
     * @Date 17:02 2020/5/27
     * @Param [e]
     **/
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public JsonResult handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        return ResponseUtil.fail("数据不是json格式");
    }

    @ExceptionHandler(DuplicateKeyException.class)
    public JsonResult handleDuplicateKeyException(DuplicateKeyException e) {
        return ResponseUtil.buildResult(ResponseUtil.ERROR_ADD, "数据重复");
    }

    /**
     * @return com.wx.genealogy.common.domin.JsonResult
     * @Author hangyi
     * @Description 处理未找到文件异常
     * @Date 17:44 2020/5/27
     * @Param [e]
     **/
    @ExceptionHandler(FileNotFoundException.class)
    public JsonResult handleFileNotFoundException(FileNotFoundException e) {
        return ResponseUtil.buildResult(ResponseUtil.NOT_FOUND, e.getMessage().substring(e.getMessage().indexOf(":") + 1));
    }

    /**
     * @return com.wx.genealogy.common.domin.JsonResult
     * @Author hangyi
     * @Description 处理文件写异常
     * @Date 17:44 2020/5/27
     * @Param [e]
     **/
    @ExceptionHandler(IOException.class)
    public JsonResult handleIOException(IOException e) {
        return ResponseUtil.buildResult(405, "文件上传失败");
    }

    @ExceptionHandler(AccessDeniedException.class)
    public JsonResult handleAccessDeniedExceptionException(AccessDeniedException e) {
        return ResponseUtil.buildResult(ResponseUtil.FAIL, "没有权限");
    }

    /**
     * easyExcel异常处理
     */
    @ExceptionHandler(ExcelAnalysisStopException.class)
    public JsonResult ExcelAnalysisStopException(ExcelAnalysisStopException e) {
        return ResponseUtil.fail(e.getMessage().substring(e.getMessage().indexOf(":") + 1));
    }

    /**
     * 数据库异常异常处理
     */
    @ExceptionHandler(SQLException.class)
    public JsonResult SQLException(ExcelAnalysisStopException e) {
        return ResponseUtil.fail("数据库操作异常");
    }

    @ExceptionHandler(Exception.class)
    public JsonResult handleException(Exception e) {
        return ResponseUtil.fail(e.getMessage().substring(e.getMessage().indexOf(":") + 1));
    }

    @ExceptionHandler(MyCustomException.class)
    public JsonResult myCustomException(Exception e) {
        return ResponseUtil.fail(e.getMessage());
    }

    @ExceptionHandler(ServiceException.class)
    public JsonResult serviceException(Exception e) {
        return ResponseUtil.fail(e.getMessage());
    }
}
