package com.rilintech.unit;

import java.lang.reflect.Field;

import android.app.DatePickerDialog;  
import android.content.Context;  
import android.view.View;  
import android.view.ViewGroup;  
import android.widget.DatePicker;  
import android.widget.LinearLayout;  
/** 
 * @author joker 
 * 自定义的日期控件，只有年和月，没有日 
 *  
 */  
public class mDatePickerDialog extends DatePickerDialog {  
     public mDatePickerDialog(Context context,  
             OnDateSetListener callBack, int year, int monthOfYear) {  
         super(context, callBack, year, monthOfYear, 3);  
         this.setTitle(year+"年"+(monthOfYear+1) + "月" );  
     }  
  
     @Override  
     public void onDateChanged(DatePicker view, int year, int month, int day) {  
         super.onDateChanged(view, year, month, day);  
         this.setTitle(year+"年"+(month+1)+ "月" );  
     }  
  
    /* (non-Javadoc) 
     * @see android.app.DatePickerDialog#show() 
     */  
    @Override  
    public void show() {  
        // TODO Auto-generated method stub  
        super.show();  
         DatePicker dp = findDatePicker((ViewGroup) this.getWindow().getDecorView());  
            if (dp != null) {  
                Class c=dp.getClass();  
                Field f;  
                try {  
                        if(GlobalVar.getAndroidSDKVersion()>4.0){  
                            f = c.getDeclaredField("mDaySpinner");  
                            f.setAccessible(true);    
                            LinearLayout l= (LinearLayout)f.get(dp);     
                            l.setVisibility(View.GONE);  
                        }else{  
                            f = c.getDeclaredField("mDayPicker");  
                            f.setAccessible(true);    
                            LinearLayout l= (LinearLayout)f.get(dp);     
                            l.setVisibility(View.GONE);  
                        }  
                } catch (SecurityException e) {  
                    // TODO Auto-generated catch block  
                    e.printStackTrace();  
                } catch (NoSuchFieldException e) {  
                    // TODO Auto-generated catch block  
                    e.printStackTrace();  
                } catch (IllegalArgumentException e) {  
                    // TODO Auto-generated catch block  
                    e.printStackTrace();  
                } catch (IllegalAccessException e) {  
                    // TODO Auto-generated catch block  
                    e.printStackTrace();  
                }    
                  
            }   
    }  
    /** 
     * 从当前Dialog中查找DatePicker子控件 
     *  
     * @param group 
     * @return 
     */  
    private DatePicker findDatePicker(ViewGroup group) {  
        if (group != null) {  
            for (int i = 0, j = group.getChildCount(); i < j; i++) {  
                View child = group.getChildAt(i);  
                if (child instanceof DatePicker) {  
                    return (DatePicker) child;  
                } else if (child instanceof ViewGroup) {  
                    DatePicker result = findDatePicker((ViewGroup) child);  
                    if (result != null)  
                        return result;  
                }   
            }  
        }  
        return null;  
  
    }   
        
}  
