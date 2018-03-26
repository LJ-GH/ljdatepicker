# 日期选择控件

- 可以选择本月，上个月，上三个月，上半年，上一年以及其他日期


## 使用方法

复制aar文件到app/libs文件夹中

在App的build.gradle

```
android{
  ...
  repositories{
        flatDir{
            dirs 'libs'
        }
    }
  ...
}

...
dependencies{
  ...
  compile (name:'ljdatepicker',ext:'aar')
  ...
}
```

添加监听
```java
SelectorOfRangeTime mSelectOfTime = (SelectorOfRangeTime) findViewById(R.id.select_of_time);
mSelectOfTime.setOnSelectConfirmListener(new SelectorOfRangeTime.OnSelectConfirmListener() {
    @Override
    public void call(String startTime, String endTime) {
        final String ss = startTime + "~-----~" + endTime;
        Toast.makeText(context, ss, Toast.LENGTH_SHORT).show();
    }
});
```




![myDatePicker](myDatePicker.gif)