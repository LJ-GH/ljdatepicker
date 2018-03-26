package com.lj.ljdatepicker;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lj.ljdatepicker.widget.DatePickDialog;
import com.lj.ljdatepicker.widget.OnSureLisener;
import com.lj.ljdatepicker.widget.bean.DateType;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by linjie on 2017/11/28.
 */

public class SelectorOfRangeTime extends LinearLayout implements View.OnClickListener {
    private Context context;
    private View conentView;
    private View popupView;
    private PopupWindow popupWindow;

    private LinearLayout mTimeContainView;
    private TextView mTvShowSelectTime;
    private ImageView mButtonOpenTimeSelector;
    private TextView mTvBtnCurrentMonth;
    private TextView mTvBtnLastMonth;
    private TextView mTvBtnLast3Month;
    private TextView mTvBtnLast6Month;
    private TextView mTvBtnLastYear;
    private TextView mTvShowStartTime;
    private LinearLayout mLlBtnStartTime;
    private TextView mTvShowEndTime;
    private LinearLayout mLlBtnEndTime;
    private LinearLayout mLlBtnCancel;
    private LinearLayout mLlBtnConfirm;
    //日期变量
    private Calendar calendar;
    private String startTime;   //yyyy-MM-dd
    private String endTime;     //yyyy-MM-dd
    private String returnStartTime;   //yyyy-MM-dd
    private String returnEndTime;     //yyyy-MM-dd
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    //滚动 日期选择器
    private DatePickDialog startTimeDatePickDialog;
    private DatePickDialog endTimeDatePickDialog;
    private OnSelectConfirmListener onSelectConfirmListener;

    public interface OnSelectConfirmListener {
        void call(String startTime, String endTime);
    }

    public void setOnSelectConfirmListener(OnSelectConfirmListener onSelectConfirmListener) {
        this.onSelectConfirmListener = onSelectConfirmListener;
    }

    public SelectorOfRangeTime(Context context) {
        this(context, null);
    }

    public SelectorOfRangeTime(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SelectorOfRangeTime(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        this.context = context;
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        conentView = inflater.inflate(R.layout.selector_of_time, this);
        mTimeContainView = (LinearLayout)conentView.findViewById(R.id.time_contain_view);
        this.mTvShowSelectTime = (TextView) conentView.findViewById(R.id.tv_show_select_time);
        this.mButtonOpenTimeSelector = (ImageView) conentView.findViewById(R.id.button_open_time_selector);
        mButtonOpenTimeSelector.setOnClickListener(this);
        popupView = inflater.inflate(R.layout.selector_of_time_popup, null);
        this.mTvBtnCurrentMonth = (TextView) popupView.findViewById(R.id.tv_btn_current_month);
        this.mTvBtnLastMonth = (TextView) popupView.findViewById(R.id.tv_btn_last_month);
        this.mTvBtnLast3Month = (TextView) popupView.findViewById(R.id.tv_btn_last3_month);
        this.mTvBtnLast6Month = (TextView) popupView.findViewById(R.id.tv_btn_last6_month);
        this.mTvBtnLastYear = (TextView) popupView.findViewById(R.id.tv_btn_last_year);
        this.mTvShowStartTime = (TextView) popupView.findViewById(R.id.tv_show_start_time);
        this.mLlBtnStartTime = (LinearLayout) popupView.findViewById(R.id.ll_btn_start_time);
        this.mTvShowEndTime = (TextView) popupView.findViewById(R.id.tv_show_end_time);
        this.mLlBtnEndTime = (LinearLayout) popupView.findViewById(R.id.ll_btn_end_time);
        this.mLlBtnCancel = (LinearLayout) popupView.findViewById(R.id.ll_btn_cancel);
        this.mLlBtnConfirm = (LinearLayout) popupView.findViewById(R.id.ll_btn_confirm);
        mTvBtnCurrentMonth.setOnClickListener(this);
        mTvBtnLastMonth.setOnClickListener(this);
        mTvBtnLast3Month.setOnClickListener(this);
        mTvBtnLast6Month.setOnClickListener(this);
        mTvBtnLastYear.setOnClickListener(this);
        mLlBtnStartTime.setOnClickListener(this);
        mLlBtnEndTime.setOnClickListener(this);
        mLlBtnCancel.setOnClickListener(this);
        mLlBtnConfirm.setOnClickListener(this);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.SelectorOfRangeTime);
        int backgroundColor = typedArray.getColor(R.styleable.SelectorOfRangeTime_background_color, Color.WHITE);
        mTimeContainView.setBackgroundColor(backgroundColor);
        int dateTextColor = typedArray.getColor(R.styleable.SelectorOfRangeTime_date_range_text_color, getResources().getColor(R.color.font_color_default));
        int dateTextSize = typedArray.getDimensionPixelSize(R.styleable.SelectorOfRangeTime_date_range_text_size, 16);
        mTvShowSelectTime.setTextColor(dateTextColor);
        mTvShowSelectTime.setTextSize(dateTextSize);
        int dateLogoColor = typedArray.getColor(R.styleable.SelectorOfRangeTime_date_logo_color, getResources().getColor(R.color.font_color_default));
        final int defaultDateLogoWidth = (int)(getResources().getDisplayMetrics().density*30+0.5f);
        final int defaultDateLogoHeight = (int)(getResources().getDisplayMetrics().density*25+0.5f);
        int dateLogoWidth = typedArray.getDimensionPixelSize(R.styleable.SelectorOfRangeTime_date_logo_width, defaultDateLogoWidth);
        int dateLogoHeight = typedArray.getDimensionPixelSize(R.styleable.SelectorOfRangeTime_date_logo_height, defaultDateLogoHeight);
        mButtonOpenTimeSelector.setColorFilter(dateLogoColor);
        ViewGroup.LayoutParams layoutParams = mButtonOpenTimeSelector.getLayoutParams();
        layoutParams.width = dateLogoWidth;
        layoutParams.height = dateLogoHeight;
        mButtonOpenTimeSelector.setLayoutParams(layoutParams);
        popupWindow = new PopupWindow(popupView, RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        popupWindow.setTouchable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);

        //设置返回时间的初始值 本月初到现在
        calendar = Calendar.getInstance();
        returnEndTime = new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime());
        calendar.add(Calendar.MONTH, -1);
        returnStartTime = new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime());

    }

    public void showPopupWindow(View parent) {
        if (!popupWindow.isShowing()) {
            popupWindow.showAsDropDown(parent, 0, 0);
        } else {
            popupWindow.dismiss();
        }
    }

    private void changeTabColor(TextView textView){
        mTvBtnCurrentMonth.setTextColor(getResources().getColor(R.color.font_color_default));
        mTvBtnLastMonth.setTextColor(getResources().getColor(R.color.font_color_default));
        mTvBtnLast3Month.setTextColor(getResources().getColor(R.color.font_color_default));
        mTvBtnLast6Month.setTextColor(getResources().getColor(R.color.font_color_default));
        mTvBtnLastYear.setTextColor(getResources().getColor(R.color.font_color_default));
        textView.setTextColor(getResources().getColor(R.color.default_color));
    }


    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.button_open_time_selector) {
            this.showPopupWindow(view);

        } else if (i == R.id.tv_btn_current_month) {
            calendar = Calendar.getInstance();
            endTime = sdf.format(calendar.getTime());
            calendar.set(Calendar.DATE, 1);
            startTime = sdf.format(calendar.getTime());
            mTvShowStartTime.setText(startTime);
            mTvShowEndTime.setText(endTime);
            changeTabColor(mTvBtnCurrentMonth);

        } else if (i == R.id.tv_btn_last_month) {
            calendar = Calendar.getInstance();
            endTime = sdf.format(calendar.getTime());
            calendar.add(Calendar.MONTH, -1);
            startTime = sdf.format(calendar.getTime());
            mTvShowStartTime.setText(startTime);
            mTvShowEndTime.setText(endTime);
            changeTabColor(mTvBtnLastMonth);

        } else if (i == R.id.tv_btn_last3_month) {
            calendar = Calendar.getInstance();
            endTime = sdf.format(calendar.getTime());
            calendar.add(Calendar.MONTH, -3);
            startTime = sdf.format(calendar.getTime());
            mTvShowStartTime.setText(startTime);
            mTvShowEndTime.setText(endTime);
            changeTabColor(mTvBtnLast3Month);

        } else if (i == R.id.tv_btn_last6_month) {
            calendar = Calendar.getInstance();
            endTime = sdf.format(calendar.getTime());
            calendar.add(Calendar.MONTH, -6);
            startTime = sdf.format(calendar.getTime());
            mTvShowStartTime.setText(startTime);
            mTvShowEndTime.setText(endTime);
            changeTabColor(mTvBtnLast6Month);

        } else if (i == R.id.tv_btn_last_year) {
            calendar = Calendar.getInstance();
            endTime = sdf.format(calendar.getTime());
            calendar.add(Calendar.MONTH, -12);
            startTime = sdf.format(calendar.getTime());
            mTvShowStartTime.setText(startTime);
            mTvShowEndTime.setText(endTime);
            changeTabColor(mTvBtnLastYear);

        } else if (i == R.id.ll_btn_start_time) {
            startTimeDatePickDialog = new DatePickDialog(context);
            //设置上下年分限制
//                dialog.setYearLimt(1);
            //设置标题
            startTimeDatePickDialog.setTitle("选择开始时间");
            //设置类型
            startTimeDatePickDialog.setType(DateType.TYPE_YMD);
            //设置消息体的显示格式，日期格式
            startTimeDatePickDialog.setMessageFormat("yyyy-MM-dd");
            //设置选择回调
            startTimeDatePickDialog.setOnChangeLisener(null);
            //设置点击确定按钮回调
            startTimeDatePickDialog.setOnSureLisener(new OnSureLisener() {
                @Override
                public void onSure(Date date) {
                    String result = new SimpleDateFormat("yyyy-MM-dd").format(date);
                    startTime = result;
                    mTvShowStartTime.setText(result);
                }
            });
            //设置弹出的选择时间是当前选择的时间
            if(startTime!=null){
                try {
                    Date date = new SimpleDateFormat("yyyy-MM-dd").parse(startTime);
                    startTimeDatePickDialog.setStartDate(date);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            startTimeDatePickDialog.show();

        } else if (i == R.id.ll_btn_end_time) {
            endTimeDatePickDialog = new DatePickDialog(context);
            //设置上下年分限制
            endTimeDatePickDialog.setYearLimt(5);
            //设置标题
            endTimeDatePickDialog.setTitle("选择结束时间");
            //设置类型
            endTimeDatePickDialog.setType(DateType.TYPE_YMD);
            //设置消息体的显示格式，日期格式
            endTimeDatePickDialog.setMessageFormat("yyyy-MM-dd");
            //设置选择回调
            endTimeDatePickDialog.setOnChangeLisener(null);
            //设置点击确定按钮回调
            endTimeDatePickDialog.setOnSureLisener(null);
            endTimeDatePickDialog.setOnSureLisener(new OnSureLisener() {
                @Override
                public void onSure(Date date) {
                    String result = new SimpleDateFormat("yyyy-MM-dd").format(date);
                    endTime = result;
                    mTvShowEndTime.setText(result);
                }
            });
            if(endTime!=null){
                try {
                    Date date = new SimpleDateFormat("yyyy-MM-dd").parse(endTime);
                    endTimeDatePickDialog.setStartDate(date);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            endTimeDatePickDialog.show();

        } else if (i == R.id.ll_btn_cancel) {
            popupWindow.dismiss();

        } else if (i == R.id.ll_btn_confirm) {
            if (startTime == null || endTime == null) {//没有任何选择操作
                Toast.makeText(context.getApplicationContext(), "请选择时间", Toast.LENGTH_SHORT).show();
            } else {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                try {
                    long now = new Date().getTime();
                    long startDate = sdf.parse(startTime).getTime();
                    long endDate = sdf.parse(endTime).getTime();
                    if (startDate > now) {
                        Toast.makeText(context.getApplicationContext(), "开始时间大于现在时刻", Toast.LENGTH_SHORT).show();
                    } else if (endDate > now) {
                        Toast.makeText(context.getApplicationContext(), "结束时间大于现在时刻", Toast.LENGTH_SHORT).show();
                    } else if (startDate >= endDate) {
                        Toast.makeText(context.getApplicationContext(), "开始时间大于等于结束时间", Toast.LENGTH_SHORT).show();
                    } else {
                        returnStartTime = startTime;
                        returnEndTime = endTime;
                        mTvShowSelectTime.setText(startTime + " 至 " + endTime);
                        popupWindow.dismiss();
                        if(onSelectConfirmListener !=null){
                            onSelectConfirmListener.call(returnStartTime, returnEndTime);
                        }
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                    popupWindow.dismiss();
                }
            }

        }
    }

}
