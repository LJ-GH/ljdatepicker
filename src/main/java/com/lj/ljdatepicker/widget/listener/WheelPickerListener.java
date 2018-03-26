package com.lj.ljdatepicker.widget.listener;

import com.lj.ljdatepicker.widget.bean.DateBean;
import com.lj.ljdatepicker.widget.bean.DateType;

/**
 * Created by codbking on 2016/9/22.
 */

public interface WheelPickerListener {
     void onSelect(DateType type, DateBean bean);
}
