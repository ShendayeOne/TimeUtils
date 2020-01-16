package com.sy.timeutis;

import android.app.Activity;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.necer.calendar.BaseCalendar;
import com.necer.calendar.Miui10Calendar;
import com.necer.entity.CalendarDate;
import com.necer.entity.Lunar;
import com.necer.listener.OnCalendarChangedListener;
import com.necer.utils.CalendarUtil;

import org.joda.time.LocalDate;

import java.util.Calendar;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.Random;

public class MainActivity extends Activity {

    private TextView msgTv;
    private Button copyBt;
    private LinearLayout LL;
    private String msg;

    private TextView tv_data;
    private TextView tv_desc;
    private Miui10Calendar miui10Calendar;

    private Context mContext;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /************************ 初始化 ************************/

        mContext = this;


        tv_data = findViewById(R.id.tv_data);
        tv_desc = findViewById(R.id.tv_desc);

        miui10Calendar = findViewById(R.id.miui10Calendar);

        /************************ 核心功能 ************************/

        Feb = getFebDays();//先得知道2月是多少天
        msg =   "今天是本月的第 " + getToday() + " 天。" + "\n" +
                "距离本月结束仅剩 " + getTimeRemaining() + " 天！" + "\n" +
                "距离本季度结束仅剩 " + getQuarterRemainingDays() + "  天！" ;

        SpannableString spanStr = new SpannableString(msg);
        //设置文字的大小
        spanStr.setSpan(new ForegroundColorSpan(Color.RED), msg.indexOf("第 ")+2, msg.indexOf(" 天。"), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spanStr.setSpan(new AbsoluteSizeSpan(55), msg.indexOf("第 ")+2, msg.indexOf(" 天。"), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        spanStr.setSpan(new ForegroundColorSpan(Color.RED), msg.indexOf("本月结束仅剩 ")+7, msg.indexOf(" 天！"), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spanStr.setSpan(new AbsoluteSizeSpan(65), msg.indexOf("本月结束仅剩 ")+7,  msg.indexOf(" 天！"), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        spanStr.setSpan(new ForegroundColorSpan(Color.RED), msg.indexOf("季度结束仅剩 ")+7, msg.indexOf("  天！"), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spanStr.setSpan(new AbsoluteSizeSpan(75), msg.indexOf("季度结束仅剩 ")+7, msg.indexOf("  天！"), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        msgTv = findViewById(R.id.mTextViewLog);
        msgTv.setText(spanStr);


        /************************ 优化内容 ************************/
//        copyBt = findViewById(R.id.copyBtn);
//        copyBt.setOnClickListener(new OnClickListener() {
//
//            @Override
//            public void onClick(View arg0) {
//                copy(msg);
//            }
//        });

        miui10Calendar.setOnCalendarChangedListener(new OnCalendarChangedListener() {
            @Override
            public void onCalendarChange(BaseCalendar baseCalendar, int year, int month, LocalDate localDate) {
                if (localDate != null) {
                    CalendarDate calendarDate = CalendarUtil.getCalendarDate(localDate);
                    Lunar lunar = calendarDate.lunar;
                    tv_data.setText(localDate.toString("yyyy年MM月dd日"));
                    tv_desc.setText(lunar.chineseEra + lunar.animals + "年" + lunar.lunarMonthStr + lunar.lunarDayStr);
                }
            }
        });

        //背景随机轮播
        LL = findViewById(R.id.LL);
        LL.setBackgroundResource(NBUtilResource.getId(this,"drawable","background"+getRandom()));
        LL.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                LL.setBackgroundResource(NBUtilResource.getId(mContext,"drawable","background"+getRandom()));
            }
        });
        /************************ 测试日志 ************************/
        //测试日志
        mLog("今年的2月有多少天：" + getFebDays());
        mLog("现在是几月份：" + getMonth());
        mLog("当前日期：" + getCurrentTime());
        mLog("本月一共有几日：" + getNumberdaysMonth());
        mLog("今天为本月的第几天：" + getToday());
        mLog("距离本月结束还有几天：" + getTimeRemaining());
        mLog("当前季度为本年第 " + getQuarter() + " 季度");
        mLog("本季度一共多少天：" + getQuarterAllTodays());
        mLog("本季度结束还剩多少天：" + getQuarterRemainingDays());


    }

    /************************ 辅助内容 ************************/

    //日志打印
    private void mLog(String msg) {
        Log.e("NB", msg + "");
    }

    //复制tv文本
    private void copy(String msg) {
        ClipboardManager cmb = (ClipboardManager) getApplicationContext().getSystemService(getApplicationContext().CLIPBOARD_SERVICE);
        cmb.setText(msg); //将内容放入粘贴管理器,在别的地方长按选择"粘贴"即可L
        Toast.makeText(getApplicationContext(), "复制成功", Toast.LENGTH_LONG).show();
    }

    //获取1-32的随机数
    public int getRandom() {
        Random rand = new Random();
        return rand.nextInt(32) + 1;
    }

    /************************ 核心辅助内容 ************************/
    //1-12月有多少天
    int Jan = 31;
    int Feb = 29;//此值需要判断重新赋值
    int Mar = 31;
    int Apr = 30;
    int May = 31;
    int Jun = 30;
    int Jul = 31;
    int Aug = 31;
    int Sept= 30;
    int Oct = 31;
    int Nov = 30;
    int Dec = 31;

    //判断是否是闰年
    private boolean isALeapYear(int year) {
        if (year % 4 == 0 && year % 100 != 0 || year % 400 == 0) {
            return true;
        } else {
            return false;
        }
    }

    //获取2月有多少天
    private int getFebDays() {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        return isALeapYear(year) ? 29 : 28;
    }

    //获取当前日期
    private String getCurrentTime() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
        return df.format(new Date());
    }

    //获取今日为本月第几天
    @SuppressWarnings("deprecation")
    private int getToday() {
        Date dt = new Date();
        int iDay = dt.getDate();
        return iDay;
    }

    //获取当月一共几天
    private int getNumberdaysMonth() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, 2007);
        cal.set(Calendar.MONTH, 5 - 1);//Java月份才0开始算
        int dateOfMonth = cal.getActualMaximum(Calendar.DATE);
        return dateOfMonth;
    }

    //获取当月还剩几天
    private int getTimeRemaining() {
        return (getNumberdaysMonth() - getToday());
    }


    //获取当前季度
    public int getQuarter() {
        Calendar c = Calendar.getInstance();
        int month = c.get(c.MONTH) + 1;
        int quarter = 0;
        if (month >= 1 && month <= 3) {
            quarter = 1;
        } else if (month >= 4 && month <= 6) {
            quarter = 2;
        } else if (month >= 7 && month <= 9) {
            quarter = 3;
        } else {
            quarter = 4;
        }
        return quarter;
    }

    //获取当前月
    private int getMonth() {
        Calendar cal = Calendar.getInstance();
        int month = cal.get(Calendar.MONTH) + 1;
        return month;
    }

    //获取本季度一共多少天
    private int getQuarterAllTodays() {
        //几月
        int month = getMonth();
        if (month >= 1 && month <= 3) {
            return Jan + getFebDays() + Mar;
        } else if (month >= 4 && month <= 6) {
            return Apr + May + Jun;
        } else if (month >= 7 && month <= 9) {
            return Jul + Aug + Sept;
        } else {
            return Oct + Nov + Dec;
        }
    }

    //获取本季度还剩多少天
    private int getQuarterRemainingDays() {
        int quarter = getQuarter();
        int month = getMonth();
        int remainingDays = 0;
        int quarterAllTodays = getQuarterAllTodays();

        if (quarter == 1) {
            if (month == 1) {
                remainingDays = quarterAllTodays - getToday();
            }
            if (month == 2) {
                remainingDays = quarterAllTodays - Jan - getToday();
            }
            if (month == 3) {
                remainingDays = quarterAllTodays - Jan - Feb - getToday();
            }
        } else if (quarter == 2) {
            if (month == 4) {
                remainingDays = quarterAllTodays - getToday();
            }
            if (month == 5) {
                remainingDays = quarterAllTodays - Apr - getToday();
            }
            if (month == 6) {
                remainingDays = quarterAllTodays - Apr - May - getToday();
            }
        } else if (quarter == 3) {
            if (month == 7) {
                remainingDays = quarterAllTodays - getToday();
            }
            if (month == 8) {
                remainingDays = quarterAllTodays - Jul - getToday();
            }
            if (month == 9) {
                remainingDays = quarterAllTodays - Jul - Aug - getToday();
            }
        } else if (quarter == 4) {
            if (month == 10) {
                remainingDays = quarterAllTodays - getToday();
            }
            if (month == 11) {
                remainingDays = quarterAllTodays - Oct - getToday();
            }
            if (month == 12) {
                remainingDays = quarterAllTodays - Oct - Nov - getToday();
            }
        } else {
            Toast.makeText(getApplicationContext(), "获取季度出现问题：" + quarter, Toast.LENGTH_LONG).show();
        }

        return remainingDays;

    }

}
