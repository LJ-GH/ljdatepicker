package com.lj.ljdatepicker;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.lj.ljdatepicker.widget.DatePickDialog;
import com.lj.ljdatepicker.widget.OnSureLisener;
import com.lj.ljdatepicker.widget.bean.DateType;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by linjie on 2017/12/5.
 */

public class SelectorOfSingleTime extends LinearLayout implements View.OnClickListener, OnSureLisener {
    private Context mContext;
    private View view;
    private RadioButton mDay;
    private RadioButton mMonth;
    private RadioButton mYear;
    private LinearLayout mBtnSelectTime;
    private RadioGroup mRadioGroup;
    private DatePickDialog dayPickDialog;
    private SimpleDateFormat daySdf;
    private DatePickDialog monthPickDialog;
    private SimpleDateFormat monthSdf;
    private DatePickDialog yearPickDialog;
    private SimpleDateFormat yearSdf;
    private SimpleDateFormat ymdhmsSdf;
    private OnConfirmListener onConfirmListener;
    private SimpleDateFormat currentFormat;
    private TextView showTime;
    private SelectorOfSingleTimeMode currentMode;
    private DatePickDialog fullTimePickDialog;

    public interface OnConfirmListener {
        void onConfirm(Date date, String formatDateStr);
    }

    public void SetOnConfirmListener(OnConfirmListener onConfirmListener) {
        this.onConfirmListener = onConfirmListener;
    }

    public SelectorOfSingleTime(Context context) {
        this(context, null);
    }

    public SelectorOfSingleTime(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SelectorOfSingleTime(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        initView();
    }

    private void initView() {
        this.view = LayoutInflater.from(mContext).inflate(R.layout.selector_of_daymonthyear, this);
        this.mDay = (RadioButton) view.findViewById(R.id.day);
        this.mMonth = (RadioButton) view.findViewById(R.id.month);
        this.mYear = (RadioButton) view.findViewById(R.id.year);
        this.mBtnSelectTime = (LinearLayout) view.findViewById(R.id.btn_select_time);
        this.mRadioGroup = (RadioGroup) view.findViewById(R.id.radio_group);
        this.showTime = (TextView) view.findViewById(R.id.tv_show_time);
        mBtnSelectTime.setOnClickListener(this);
        daySdf = new SimpleDateFormat("yyyy-MM-dd");
        monthSdf = new SimpleDateFormat("yyyy-MM");
        yearSdf = new SimpleDateFormat("yyyy");
        ymdhmsSdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        showTime.setText(daySdf.format(new Date()));
    }

    public void setMode(SelectorOfSingleTimeMode mode){
        currentMode = mode;
        if (mode == SelectorOfSingleTimeMode.MODE_YMD){
            mDay.setVisibility(VISIBLE);
            mMonth.setVisibility(VISIBLE);
            mYear.setVisibility(VISIBLE);
            mDay.setChecked(true);
            showTime.setText(daySdf.format(new Date()));
        }else if(mode == SelectorOfSingleTimeMode.MODE_YM){
            mDay.setVisibility(GONE);
            mMonth.setVisibility(VISIBLE);
            mYear.setVisibility(VISIBLE);
            mMonth.setChecked(true);
            showTime.setText(monthSdf.format(new Date()));
        }else if(mode == SelectorOfSingleTimeMode.MODE_Y){
            mDay.setVisibility(GONE);
            mMonth.setVisibility(GONE);
            mYear.setVisibility(VISIBLE);
            mYear.setChecked(true);
            showTime.setText(yearSdf.format(new Date()));
        }else if(mode == SelectorOfSingleTimeMode.MODE_M){
            mDay.setVisibility(GONE);
            mMonth.setVisibility(VISIBLE);
            mYear.setVisibility(GONE);
            mMonth.setChecked(true);
            showTime.setText(monthSdf.format(new Date()));
        }else if(mode == SelectorOfSingleTimeMode.MODE_D){
            mDay.setVisibility(VISIBLE);
            mMonth.setVisibility(GONE);
            mYear.setVisibility(GONE);
            mDay.setChecked(true);
            showTime.setText(daySdf.format(new Date()));
        }else if(mode == SelectorOfSingleTimeMode.MODE_YMD_HMS){
            mDay.setVisibility(INVISIBLE);
            mMonth.setVisibility(GONE);
            mYear.setVisibility(GONE);
            mDay.setChecked(true);
            showTime.setText(ymdhmsSdf.format(new Date()));
        }
    }


    @Override
    public void onClick(View view) {
        int i = view.getId();
        if(i == R.id.btn_select_time){
            if(currentMode == SelectorOfSingleTimeMode.MODE_YMD_HMS){
                if(null == fullTimePickDialog){
                    fullTimePickDialog = new DatePickDialog(mContext);
                    fullTimePickDialog.setTitle("请选择日期");
                    fullTimePickDialog.setType(DateType.TYPE_ALL);
                    fullTimePickDialog.setCanceledOnTouchOutside(true);
                    fullTimePickDialog.setOnSureLisener(this);
                    fullTimePickDialog.show();
                }else{
                    fullTimePickDialog.show();
                }
                currentFormat = ymdhmsSdf;
                return;
            }


            int id = mRadioGroup.getCheckedRadioButtonId();
            if(id == R.id.day){
                if(null == dayPickDialog){
                    dayPickDialog =  new DatePickDialog(mContext);
                    dayPickDialog.setTitle("请选择日期");
                    dayPickDialog.setType(DateType.TYPE_YMD);
                    dayPickDialog.setCanceledOnTouchOutside(true);
                    dayPickDialog.setOnSureLisener(this);
                    dayPickDialog.show();
                }else{
                    dayPickDialog.show();
                }
                currentFormat = daySdf;
            }else if(id == R.id.month){
                if(null == monthPickDialog){
                    monthPickDialog =  new DatePickDialog(mContext);
                    monthPickDialog.setTitle("请选择月份");
                    monthPickDialog.setType(DateType.TYPE_YM);
                    monthPickDialog.setCanceledOnTouchOutside(true);
                    monthPickDialog.setOnSureLisener(this);
                    monthPickDialog.show();
                }else{
                    monthPickDialog.show();
                }
                currentFormat = monthSdf;
            }else if(id == R.id.year){
                if(null == yearPickDialog){
                    yearPickDialog =  new DatePickDialog(mContext);
                    yearPickDialog.setTitle("请选择年份");
                    yearPickDialog.setType(DateType.TYPE_Y);
                    yearPickDialog.setCanceledOnTouchOutside(true);
                    yearPickDialog.setOnSureLisener(this);
                    yearPickDialog.show();
                }else{
                    yearPickDialog.show();
                }
                currentFormat = yearSdf;
            }
        }
    }

    @Override
    public void onSure(Date date) {
        String s = currentFormat.format(date);
        this.showTime.setText(s);
        if(onConfirmListener!=null){
            this.onConfirmListener.onConfirm(date,s);
        }
    }
}
