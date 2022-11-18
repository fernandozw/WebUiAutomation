package com.thinkingdata.response;

/**
 * @author Fernando Zhu
 * @version 1.0
 * @date 2020/5/21 17:45
 */
public enum ResultEnums {
    SUCCESS("1000", "操作成功"), ERROR("1001", "系统内部错误"), ADD_SUCCESS("1000", "新增成功"), ADD_FAIL("1002", "新增失败"), UPDATE_SUCCESS("1000", "更新成功"), UPDATE_FAIL("1002", "操作的数据不存在或其他异常"), DELETE_SUCCESS("1000", "删除成功"), DELETE_FAIL("1002", "删除失败"), WAIT("1003", "操作执行中,请稍后再试~~~");
    // 返回状态码
    private String code;
    // 返回信息
    private String msg;

    ResultEnums(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
