package info.local.ridermemory.util;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class DateUtil {
    public static final Calendar NOW = Calendar.getInstance();

    public static List<String> getStartEndDayFromThisMonth() {
        List<String> startAndEndDay = new ArrayList<>();

        // 当月１日のオブジェクトを作成
        Calendar startDayOfMonth = DateUtil.getFirstDayOfTargetMonth(NOW);

        // 当月末日のオブジェクトを作成
        Calendar endDayOfMonth = DateUtil.getFirstDayOfTargetNextMonth(NOW);
        endDayOfMonth.add(Calendar.DATE, -1);

        // それぞれをリストに追加
        startAndEndDay.add(Constant.DB_DATE_FORMAT.format(startDayOfMonth.getTime()));
        startAndEndDay.add(Constant.DB_DATE_FORMAT.format(endDayOfMonth.getTime()));

        return startAndEndDay;
    }

    public static String getTodayAtInsertDateFormat() {
        return Constant.DB_DATE_FORMAT.format(NOW.getTime());
    }

    public static Calendar getFirstDayOfTargetMonth(Calendar targetCalendar) {
        Calendar cal = Calendar.getInstance();
        cal.set(targetCalendar.get(Calendar.YEAR), targetCalendar.get(Calendar.MONTH), 1);
        return cal;
    }
    public static Calendar getFirstDayOfTargetNextMonth(Calendar targetCalendar) {
        Calendar cal = Calendar.getInstance();
        cal.set(targetCalendar.get(Calendar.YEAR), targetCalendar.get(Calendar.MONTH) + 1, 1);
        return cal;
    }

    public static List<String> getStartEndDayFromTargetMonth(Calendar targetCalendar) {
        List<String> startAndEndDay = new ArrayList<>();

        // 指定月１日のオブジェクトを作成
        Calendar startDayOfMonth = DateUtil.getFirstDayOfTargetMonth(targetCalendar);

        // 指定月末日のオブジェクトを作成
        Calendar endDayOfMonth = DateUtil.getFirstDayOfTargetNextMonth(targetCalendar);
        endDayOfMonth.add(Calendar.DATE, -1);

        // それぞれをリストに追加
        startAndEndDay.add(Constant.DB_DATE_FORMAT.format(startDayOfMonth.getTime()));
        startAndEndDay.add(Constant.DB_DATE_FORMAT.format(endDayOfMonth.getTime()));

        return startAndEndDay;
    }
}
