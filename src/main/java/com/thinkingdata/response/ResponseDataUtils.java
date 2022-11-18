package com.thinkingdata.response;

/**
 * @author Fernando Zhu
 * @version 1.0
 * @date 2020/5/21 17:45
 */
public class ResponseDataUtils {
    /**
     * 带实体的统一返回
     *
     * @param data 实体
     * @param <T>  实体类型
     * @return
     */

    // 操作成功的构造函数
    public static <T> ResponseData buildSuccess(T data) {

        return new ResponseData<T>(ResultEnums.SUCCESS, data);
    }

    public static ResponseData buildSuccess() {

        return new ResponseData(ResultEnums.SUCCESS);
    }

    public static ResponseData buildSuccess(String msg) {

        return new ResponseData(ResultEnums.SUCCESS.getCode(), msg);
    }

    public static ResponseData buildSuccess(String code, String msg) {

        return new ResponseData(code, msg);
    }

    public static <T> ResponseData buildSuccess(String code, String msg, T data) {
        return new ResponseData<T>(code, msg, data);
    }

    public static ResponseData buildSuccess(ResultEnums resultEnums) {
        return new ResponseData(resultEnums);
    }
    /*-------------------------分割线-----------------------------*/

    // 操作异常的构造函数
    public static <T> ResponseData buildError(T data) {
        return new ResponseData<T>(ResultEnums.ERROR, data);
    }

    public static ResponseData buildError() {
        return new ResponseData(ResultEnums.ERROR);
    }

    public static ResponseData buildError(String msg) {
        return new ResponseData(ResultEnums.ERROR.getCode(), msg);
    }

    public static ResponseData buildError(String code, String msg) {
        return new ResponseData(code, msg);
    }

    public static <T> ResponseData buildError(String code, String msg, T data) {
        return new ResponseData<T>(code, msg, data);
    }

    public static ResponseData buildError(ResultEnums resultEnums) {
        return new ResponseData(resultEnums);
    }
    /*-----------------------分割线---------------------------*/

    // 新增成功的构造函数
    public static <T> ResponseData buildAddSuccess(T data) {
        return new ResponseData<T>(ResultEnums.ADD_SUCCESS, data);
    }

    public static ResponseData buildAddSuccess() {

        return new ResponseData(ResultEnums.ADD_SUCCESS);
    }

    public static ResponseData buildAddSuccess(String msg) {

        return new ResponseData(ResultEnums.ADD_SUCCESS.getCode(), msg);
    }

    public static ResponseData buildAddSuccess(String code, String msg) {

        return new ResponseData(code, msg);
    }

    public static <T> ResponseData buildAddSuccess(String code, String msg, T data) {
        return new ResponseData<T>(code, msg, data);
    }

    // 新增失败的构造函数
    public static <T> ResponseData buildAddFail(T data) {
        return new ResponseData<T>(ResultEnums.ADD_FAIL, data);
    }

    public static ResponseData buildAddFail() {

        return new ResponseData(ResultEnums.ADD_FAIL);
    }

    public static ResponseData buildAddFail(String msg) {

        return new ResponseData(ResultEnums.ADD_FAIL.getCode(), msg);
    }

    public static ResponseData buildAddFail(String code, String msg) {

        return new ResponseData(code, msg);
    }

    public static <T> ResponseData buildAddFail(String code, String msg, T data) {
        return new ResponseData<T>(code, msg, data);
    }

    public static ResponseData buildAddFail(ResultEnums resultEnums) {

        return new ResponseData(resultEnums);
    }
    /*--------------------------分割线----------------------------*/

    // 编辑成功的构造函数
    public static <T> ResponseData buildUpdateSuccess(T data) {
        return new ResponseData<T>(ResultEnums.UPDATE_SUCCESS, data);
    }

    public static ResponseData buildUpdateSuccess() {

        return new ResponseData(ResultEnums.UPDATE_SUCCESS);
    }

    public static ResponseData buildUpdateSuccess(String msg) {

        return new ResponseData(ResultEnums.UPDATE_SUCCESS.getCode(), msg);
    }

    public static ResponseData buildUpdateSuccess(String code, String msg) {

        return new ResponseData(code, msg);
    }

    public static <T> ResponseData buildUpdateSuccess(String code, String msg, T data) {
        return new ResponseData<T>(code, msg, data);
    }

    public static ResponseData buildUpdateSuccess(ResultEnums resultEnums) {
        return new ResponseData(resultEnums);
    }
    /*--------------------------分割线---------------------------*/

    // 编辑失败的构造函数
    public static <T> ResponseData buildUpdateFail(T data) {
        return new ResponseData<T>(ResultEnums.UPDATE_FAIL, data);
    }

    public static ResponseData buildUpdateFail() {

        return new ResponseData(ResultEnums.UPDATE_FAIL);
    }

    public static ResponseData buildUpdateFail(String msg) {

        return new ResponseData(ResultEnums.UPDATE_FAIL.getCode(), msg);
    }

    public static ResponseData buildUpdateFail(String code, String msg) {

        return new ResponseData(code, msg);
    }

    public static <T> ResponseData buildUpdateFail(String code, String msg, T data) {
        return new ResponseData<T>(code, msg, data);
    }

    public static ResponseData buildUpdateFail(ResultEnums resultEnums) {
        return new ResponseData(resultEnums);
    }

    /*--------------------------分割线-----------------------------*/
    // 操作执行中的构造函数
    public static <T> ResponseData buildWait(T data) {
        return new ResponseData<T>(ResultEnums.WAIT, data);
    }

    public static ResponseData buildWait() {

        return new ResponseData(ResultEnums.WAIT);
    }

    public static ResponseData buildWait(String msg) {

        return new ResponseData(ResultEnums.WAIT.getCode(), msg);
    }

    public static ResponseData buildWait(String code, String msg) {

        return new ResponseData(code, msg);
    }

    public static <T> ResponseData buildWait(String code, String msg, T data) {
        return new ResponseData<T>(code, msg, data);
    }

    public static ResponseData buildWait(ResultEnums resultEnums) {
        return new ResponseData(resultEnums);
    }

    /*--------------------------分割线-----------------------------*/
    // 删除成功的构造函数
    public static <T> ResponseData buildDeleteSuccess(T data) {
        return new ResponseData<T>(ResultEnums.DELETE_SUCCESS, data);
    }

    public static ResponseData buildDeleteSuccess() {

        return new ResponseData(ResultEnums.DELETE_SUCCESS);
    }

    public static ResponseData buildDeleteSuccess(String msg) {

        return new ResponseData(ResultEnums.DELETE_SUCCESS.getCode(), msg);
    }

    public static ResponseData buildDeleteSuccess(String code, String msg) {

        return new ResponseData(code, msg);
    }

    public static <T> ResponseData buildDeleteSuccess(String code, String msg, T data) {
        return new ResponseData<T>(code, msg, data);
    }

    public static ResponseData buildDeleteSuccess(ResultEnums resultEnums) {
        return new ResponseData(resultEnums);
    }

    /*--------------------------分割线-----------------------------*/
    // 删除失败的构造函数
    public static <T> ResponseData buildDeleteFail(T data) {
        return new ResponseData<T>(ResultEnums.DELETE_FAIL, data);
    }

    public static ResponseData buildDeleteFail() {

        return new ResponseData(ResultEnums.DELETE_FAIL);
    }

    public static ResponseData buildDeleteFail(String msg) {

        return new ResponseData(ResultEnums.DELETE_FAIL.getCode(), msg);
    }

    public static ResponseData buildDeleteFail(String code, String msg) {

        return new ResponseData(code, msg);
    }

    public static <T> ResponseData buildDeleteFail(String code, String msg, T data) {
        return new ResponseData<T>(code, msg, data);
    }

    public static ResponseData buildDeleteFail(ResultEnums resultEnums) {
        return new ResponseData(resultEnums);
    }
}
