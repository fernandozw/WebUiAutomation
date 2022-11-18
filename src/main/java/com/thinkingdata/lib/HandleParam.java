package com.thinkingdata.lib;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.springframework.stereotype.Component;
import org.testng.annotations.Test;

import com.jayway.jsonpath.JsonPath;

/**
 * @author Fernando Zhu
 * @version 1.0
 * @date 2020/5/21 17:45
 */
@Component
public class HandleParam {
    // 正则取值规则，以{开始，以}结尾，中间不要空和{,可实现找出所有占位符
    private String REG_ROLES = "\\{[^\\s\\{]*\\}";

    // 获取从原始数据中获取key取值列表
    public List<String> keyArray(String sourceStr, String reg) {
        List<String> target = new ArrayList<String>();
        // 定义规则
        Pattern p = Pattern.compile(reg);
        Matcher m = p.matcher(sourceStr);
        while (m.find()) {
            target.add(m.group());
        }

        return target;
    }

    public String replaceValue(Map<String, String> map, String source) {
        // 获取key列表
        List<String> keyLsit = keyArray(source, REG_ROLES);
        // 遍历并替换占位符
        for (int i = 0; i < keyLsit.size(); i++) {
            // 开始位置
            int begin = keyLsit.get(i).indexOf("{") + 1;
            // 结束位置
            int end = keyLsit.get(i).indexOf("}");
            // 占位符中取出map的key
            String key = keyLsit.get(i).substring(begin, end);
            // 源字符串替换占位符
            source = source.replace(keyLsit.get(i), map.get(key));
        }
        return source;
    }

    /**
     * 保存输入参数
     *
     * @param map   数据存储在map中
     * @param key   键
     * @param value 值
     */
    public void saveParam(Map<String, Object> map, String key, String value) {
        map.put(key, value);
    }

    public List<String> valueArray(String sourceJson, List<String> keyArray) {
        List<String> target = new ArrayList<String>();
        for (String key : keyArray) {
            String value = JsonPath.read(sourceJson, "$" + key.substring(2, key.length() - 1)).toString();
            target.add(value);
        }

        return target;
    }

    /**
     * 处理参数
     *
     * @param valueStr
     * @param keyStr
     * @return
     */
    public String setParam(String valueStr, String keyStr) {
        // 格式为$<.data.result>
        String reg = "\\$\\<[^\\{\\$\\<]*\\>";
        List<String> keyArray = keyArray(keyStr, reg);
        List<String> valueArray = valueArray(valueStr, keyArray(keyStr, reg));
        for (int i = 0; i < keyArray.size(); i++) {
            // int index = keyStr.indexOf(keyArray[i]);
            keyStr = keyStr.replace(keyArray.get(i), valueArray.get(i));
        }
        // System.out.println("最终:"+keyStr);
        return keyStr;
    }


    /**
     * 从map中获取值赋值给动态参数
     *
     * @param map    存放需要的值的map
     * @param keyStr 需要赋值的动态参数
     * @return
     */
    public String setValue(Map<String, String> map, String keyStr) {
       String reg = "\\$\\{[^\\{\\$\\{]*\\}";
        //String reg = "\\$\\![^\\{\\$\\!]*\\!";
        List<String> keyArray = keyArray(keyStr, reg);
        for (int i = 0; i < keyArray.size(); i++) {
            String item = keyArray.get(i);
            String key = item.substring(2, item.length() - 1);
            // System.out.println(key);
            keyStr = keyStr.replace(item, map.get(key));
        }
        // System.out.println("最终:"+keyStr);
        return keyStr;
    }

    /**
     * 按照格式生成日期
     *
     * @param format 日期格式
     * @param setoff 日期的坐标
     * @return
     */
    public String timeFormat(String format, int setoff) {
        Date date = new Date();// 取时间
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        if (setoff != 0) {
            calendar.add(calendar.DATE, setoff);
        }
        // 把日期往后增加一天.整数往后推,负数往前移动
        date = calendar.getTime(); // 这个时间就是日期往后推一天的结果
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        String dateString = formatter.format(date);
        return dateString;
    }

    // 将map形式字符串转换成Map
    public Map<String, Object> getValue(String param) {
        Map<String, Object> map = new HashMap<String, Object>();
        String str = "";
        String key = "";
        Object value = "";
        char[] charList = param.toCharArray();
        boolean valueBegin = false;
        for (int i = 0; i < charList.length; i++) {
            char c = charList[i];
            if (c == '{') {
                if (valueBegin == true) {
                    value = getValue(param.substring(i, param.length()));
                    i = param.toString().indexOf('}', i) + 1;
                    map.put(key, value);
                }
            } else if (c == '=') {
                valueBegin = true;
                key = str;
                str = "";
            } else if (c == ',') {
                valueBegin = false;
                value = str;
                str = "";
                map.put(key, value);
            } else if (c == '}') {
                if (str != "") {
                    value = str;
                }
                map.put(key, value);
                return map;
            } else if (c != ' ') {
                str += c;
            }
        }
        return map;
    }

    @Test
    public void test1() {
        // keyArray("q${.status}eq${.result.endDate}qwrqweq${.result.currentDate}ada${.successMsg}sd","\\$\\{[^\\$\\{]*\\}");
        // String[]
        // keyArray={"$.status","$.result.endDate","$.result.currentDate","$.successMsg"};
        // valueArray("{\"status\":\"SUCCESS\",\"result\":{\"endDate\":1520503200000,\"currentDate\":1521525602398,\"startDate\":1517796000000},\"successMsg\":\"获取活动开始结束时间成功\",\"errorCode\":null,\"errorMsg\":null,\"warningMsg\":null}",keyArray);
        // setParameter("{\"status\":\"SUCCESS\",\"result\":{\"endDate\":1520503200000,\"currentDate\":1521525602398,\"startDate\":1517796000000},\"successMsg\":\"获取活动开始结束时间成功\",\"errorCode\":null,\"errorMsg\":null,\"warningMsg\":null}","{qeqqwrqweqadasd}");
        // setTimeStamp("1234567890",
        // "ab$!timeStamp!cdefghi$!timeStamp!jklmnopqrs$!timeStamp!tuvwxyz");
        Map<String, String> map = new HashMap<String, String>();
        /*
         * map.put("cookie", "111"); map.put("timeStamp", "222"); map.put("secrect",
         * "333"); setCookie(map,
         * "ab$!timeStamp!cdefghi$!cookie!jklmnopqrs$!secrect!tuvwxyz");
         */

        // String source = "{\"data\":{\"result\":\"a\",\"detail\":\"b\"}}";
        // String field = "a:$.data.result;b:$.data.detail";
        /*
         * Map<String,String> paramMap = new HashMap<String,String>();
         * paramMap.put("timeStamp",String.valueOf(System.currentTimeMillis()));
         * System.out.println(setCookie(paramMap, "ha$!timeStamp!hha"));
         */
        // getResult(source, field, map);
        // System.out.println(keyArray("{hahah} 1{hehe}", REG_ROLES));
        Map<String, String> map2 = new HashMap<String, String>();
        map2.put("second", "1");
        String source = "${second}";
        System.out.println(setValue(map2, source));

    }
}
