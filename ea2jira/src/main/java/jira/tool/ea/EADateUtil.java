package jira.tool.ea;

import dbtools.common.utils.DateUtils;
import ea.tool.api.EAHeaderEnum;

import java.util.Date;
import java.util.List;

public class EADateUtil {
    private static String[] EA_Date_Format_Array = new String[] {
            "dd-MMM-yyyy HH:mm:ss", "dd-MM月-yyyy HH:mm:ss",
            "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd", "yyyyMMdd",
    };

    private static Date today = DateUtils.parse(DateUtils.format(new Date(), "yyyy-MM-dd"), "yyyy-MM-dd");
    private static String strToday = DateUtils.format(new Date(), "yyyyMMdd");
    public static String Date_Skip = "20171123";

    public static Date parse(String strDate) {
        if (strDate == null || strDate.trim().length() <= 0) {
            return null;
        }
        return DateUtils.parse(strDate.trim(), EA_Date_Format_Array);
    }

    public static String format(String strDate) {
        return format(strDate, "yyyyMMdd");
    }

    public static String format(String strDate, String format) {
        Date date = parse(strDate);
        if (date == null) {
            System.out.printf("Error when parse date: %s\r\n", strDate);
            return null;
        }
        return DateUtils.format(date, format);
    }

    public static boolean needsToBeProcessed(String strDate) {
        return needsToBeProcessed(strDate, strToday);
    }

    public static boolean needsToBeProcessed(String modifyDate, String processDate) {
        Date date = parse(modifyDate);
        if (date == null) {
            System.out.printf("Error when parse modify date: %s\r\n", modifyDate);
            return false;
        }
        modifyDate = DateUtils.format(date, "yyyyMMdd");

        date = parse(processDate);
        if (date == null) {
            System.out.printf("Error when parse process date: %s\r\n", processDate);
            return false;
        }
        processDate = DateUtils.format(date, "yyyyMMdd");

        return processDate.compareTo(modifyDate) >= 0 && Date_Skip.compareTo(modifyDate) <= 0;
    }

    public static String getQADate(String dueDate) {
        return getQADate(dueDate, today);
    }

    public static String getQADate(String dueDate, Date today) {
        Date date = DateUtils.parse(dueDate, EA2Jira.Jira_Date_Format);
        if (date == null) {
            return null;
        }

        int days = DateUtils.diffDates(date, today);
        if (days > 3) {
            days = 2;
        } else if (days > 1) {
            days = 1;
        } else {
            days = 0;
        }

        if (days > 0) {
            date = DateUtils.adjustDate(date, -days);
            dueDate = DateUtils.format(date, EA2Jira.Jira_Date_Format);
        }
        return dueDate;
    }

    public static String processDueDate(String value) {
        return processDueDate(value, today);
    }

    public static String processDueDate(String value, Date today) {
        if (value == null || value.trim().length() <= 0) {
            return null;
        }

        value = value.trim();
        if ("1.0".equalsIgnoreCase(value) || "1.".equalsIgnoreCase(value) || "1".equalsIgnoreCase(value)) {
            return null;
        }

        for (String format : new String[]{
                "yyyy.MM.dd", "yyyy-MM-dd", "yyyy/MM/dd", "yyyy年MM月dd日", "yyyy年MM月dd号",
                "yy.MM.dd", "yy-MM-dd", "yy/MM/dd", "yy年MM月dd日", "yy年MM月dd号",
                "MM.dd", "MM-dd", "MM/dd", "MM月dd日", "MM月dd号",
                "yyyyMMdd", "yyMMdd", "yyyy/MMdd", "yy/MMdd", "MMdd",
        }) {
            Date date = DateUtils.parse(value, format, false);
            if (date != null) {
                // Adjust the year if it's not set
                String strYear = DateUtils.format(date, "yyyy");
                if ("2017".compareTo(strYear) > 0) {
                    String strMonth = DateUtils.format(date, "MM-dd");
                    String strDay = DateUtils.format(date, "MM-dd");
                    int year = Integer.valueOf(DateUtils.format(today, "yyyy"));
                    int month = Integer.valueOf(DateUtils.format(today, "MM"));
                    if (month >= 12 && strMonth.compareTo("03") < 0) {
                        year++;
                    }
                    date = DateUtils.parse(String.format("%4d-%s-%s", year, strMonth, strDay), "yyyy-MM-dd");
                }

                // Format date
                return DateUtils.format(date, EA2Jira.Jira_Date_Format);
            }
        }
        return null;
    }

    public static void formatDate(List<String[]> elementList) {
        if (elementList == null || elementList.size() <= 0) {
            return;
        }

        int rowStart = 0;
        int rowEnd = elementList.size() - 1;

        // Headers
        ea.tool.api.EAHeaderEnum.fillIndex(elementList.get(rowStart++));

        // Data
        int createIndex = ea.tool.api.EAHeaderEnum.CreatedDate.getIndex();
        int modifyIndex = EAHeaderEnum.ModifiedDate.getIndex();
        while (rowStart <= rowEnd) {
            String[] element = elementList.get(rowStart++);
            if (element == null || element.length <= 0) {
                continue;
            }

            String strDate = element[createIndex];
            if (strDate != null && strDate.trim().length() > 0) {
                element[createIndex] = EADateUtil.format(strDate.trim(), "yyyy-MM-dd HH:mm:ss");
            }

            strDate = element[modifyIndex];
            if (strDate != null && strDate.trim().length() > 0) {
                element[modifyIndex] = EADateUtil.format(strDate.trim(), "yyyy-MM-dd HH:mm:ss");
            }
        }
    }
}
