package com.steel.common;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * @ProjectName: building-material
 * @Package: com.steel.common
 * @ClassName: MonthAndYearDealTools
 * @Author: Hero
 * @Description: 年或月处理工具类
 * @Date: 2020/5/1 9:31
 * @Version: 1.0
 */
public class MonthAndYearDealTools {
    // 2010-02转换为2010年
    public static String yearAndMonthToYear(String date) throws Exception {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy");
        Date realDate = simpleDateFormat.parse(date);
        return simpleDateFormat.format(realDate);
    }

    // 月份加1:2010-12  -> 2011-01
    public static String monthAddOne(String date) throws Exception {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM");
        Date realDate = simpleDateFormat.parse(date);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(realDate);
        // 增加一个月
        calendar.add(Calendar.MONTH, 1);
        return simpleDateFormat.format(calendar.getTime());
    }

    // 从startDate月份加1，直至endDate,返回list<String>
    public static ArrayList<String> monthRangeFromStartDateToEndDate(String startDate, String endDate) throws Exception {
        ArrayList<String> list = new ArrayList<>();
        list.add(startDate);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM");
        Calendar cal = Calendar.getInstance();
        Date tempDate = simpleDateFormat.parse(startDate);
        cal.setTime(tempDate);
        while (true) {
            cal.add(Calendar.MONTH, 1);
            String temp = simpleDateFormat.format(cal.getTime());
            if (temp.compareTo(endDate) >= 0) {
                break;
            } else {
                list.add(temp);
            }
        }
        return list;
    }
}
