package com.thinkingdata.lib;

import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @author Fernando Zhu
 * @version 1.0
 * @date 2022/08/26 17:31
 */
@Component
public class SplitTime {

    /**
     * 日期正则表达式
     */
    public static String YEAR_REGEX = "^\\d{4}$";
    public static String MONTH_REGEX = "^\\d{4}(\\-|\\/|\\.)\\d{1,2}$";
    public static String DATE_REGEX = "^\\d{4}(\\-|\\/|\\.)\\d{1,2}\\1\\d{1,2}$";

    /**
     * 格式化日期
     * - yyyy-MM-dd HH:mm:ss
     *
     * @param date      日期
     * @param pattern   日期格式
     * @return          日期字符串
     */
    public static String format(Date date, String pattern) {
        SimpleDateFormat sd = new SimpleDateFormat(pattern);
        return sd.format(date);
    }

    /**
     * 格式化日期
     * - yyyy-MM-dd HH:mm:ss
     *
     * @param date              日期字符串
     * @param pattern           日期格式
     * @return                  日期
     * @throws ParseException   解析异常
     */
    public static Date parse(String date, String pattern) throws ParseException {
        SimpleDateFormat sd = new SimpleDateFormat(pattern);
        try {
            return sd.parse(date);
        } catch (ParseException e) {
            throw e;
        }
    }

    /**
     * 日期范围 - 切片
     * <pre>
     * -- eg:
     * 年 ----------------------- sliceUpDateRange("2018", "2020");
     * rs: [2018, 2019, 2020]
     *
     * 月 ----------------------- sliceUpDateRange("2018-06", "2018-08");
     * rs: [2018-06, 2018-07, 2018-08]
     *
     * 日 ----------------------- sliceUpDateRange("2018-06-30", "2018-07-02");
     * rs: [2018-06-30, 2018-07-01, 2018-07-02]
     * </pre>
     *
     * @param startDate 起始日期
     * @param endDate   结束日期
     * @return          切片日期
     */
    public  List<String> sliceUpDateRange(String startDate, String endDate) {
        List<String> rs = new ArrayList<>();
        try {
            int dt = Calendar.DATE;
            String pattern = "yyyy-MM-dd";
            if(startDate.matches(YEAR_REGEX)) {
                pattern = "yyyy";
                dt = Calendar.YEAR;
            } else if(startDate.matches(MONTH_REGEX)) {
                pattern = "yyyy-MM";
                dt = Calendar.MONTH;
            } else if(startDate.matches(DATE_REGEX)) {
                pattern = "yyyy-MM-dd";
                dt = Calendar.DATE;
            }
            Calendar sc = Calendar.getInstance();
            Calendar ec = Calendar.getInstance();
            sc.setTime(parse(startDate, pattern));
            ec.setTime(parse(endDate, pattern));
            while(sc.compareTo(ec) < 1){
                rs.add(format(sc.getTime(), pattern));
                sc.add(dt, 1);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return rs;
    }


    public static void main(String[] args) {
        SplitTime splitTime = new SplitTime();
        String start="2021-11-30";
        String end ="2022-08-26";
        System.out.println(splitTime.sliceUpDateRange(start,end));
        }
}
