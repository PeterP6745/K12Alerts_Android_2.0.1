package messagelogix.com.k12campusalerts.activities;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TimePicker;

public class CustomTimePickerDialog extends TimePickerDialog {

    private final static int TIME_PICKER_INTERVAL = 5;
    private TimePicker mTimePicker;
    private OnTimeSetListener mTimeSetListener;
    private TimePicker.OnTimeChangedListener mOnTimeChangedListener;
    private Button okayButton;

    public CustomTimePickerDialog(Context context, DialogInterface.OnShowListener onShowListener, TimePicker.OnTimeChangedListener onTimeChangedListener, TimePickerDialog.OnTimeSetListener onTimeSetListener, int hourOfDay, int minute, boolean is24HourView) {
        super(context, TimePickerDialog.THEME_HOLO_LIGHT, null, hourOfDay, minute/* / TIME_PICKER_INTERVAL*/, is24HourView);
        Log.d("CustomTimePickerDialog","minutes passed to super construct --> "+minute);
        this.setOnShowListener(onShowListener);
        this.setCancelable(true);
        this.setCanceledOnTouchOutside(true);

        mTimeSetListener = onTimeSetListener;
        mOnTimeChangedListener = onTimeChangedListener;

        //mTimePicker = new TimePicker(context);
        //this.getTimePicker().setOnTimeChangedListener(onTimeChangedListener);
//        mTimePicker.setOnTimeChangedListener(mOnTimeChangedListener);

    }

    @Override
    public void updateTime(int hourOfDay, int minutes) {
//        Log.d("TimePicker","updateTime --> hour is: "+hourOfDay);
        mTimePicker.setCurrentHour(hourOfDay);
        mTimePicker.setCurrentMinute(returnBufferedMinutes(minutes)/* / TIME_PICKER_INTERVAL*/);
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        switch (which) {
            case BUTTON_POSITIVE:
                if (mTimeSetListener != null) {
                    mTimeSetListener.onTimeSet(mTimePicker, mTimePicker.getCurrentHour(), mTimePicker.getCurrentMinute() * TIME_PICKER_INTERVAL);
                }
                break;
            case BUTTON_NEGATIVE:
                cancel();
                break;
        }
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        try {
            Class<?> classForid = Class.forName("com.android.internal.R$id");
            Field timePickerField = classForid.getField("timePicker");
            mTimePicker = (TimePicker) findViewById(timePickerField.getInt(null));
            mTimePicker.setOnTimeChangedListener(mOnTimeChangedListener);

            Field field = classForid.getField("minute");

            NumberPicker minuteSpinner = (NumberPicker) mTimePicker.findViewById(field.getInt(null));
            minuteSpinner.setMinValue(0);
            minuteSpinner.setMaxValue(11);

            List<String> displayedValues = new ArrayList<>();
            for (int i = 0; i < 12; i++) {
                displayedValues.add(String.valueOf(i * TIME_PICKER_INTERVAL));
            }
//            for (int i = 0; i < 60; i += TIME_PICKER_INTERVAL) {
//                displayedValues.add(String.format("%02d", i));
//            }
            minuteSpinner.setDisplayedValues(displayedValues.toArray(new String[displayedValues.size()]));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public TimePicker getTimePicker(){
        return this.mTimePicker;
    }

    private int returnBufferedMinutes(int minutes) {
        Log.d("CustomTimePickerDialog","minutes passed to returnBufferedMinutes() function --> "+minutes);
        int timeSlot = minutes/5;
        Log.d("CustomTimePickerDialog","minutes after manipulated in returnBufferedMinutes() function --> "+timeSlot);
        if(timeSlot == 12)
            return 0;
        else
            return timeSlot;
    }

//    public void (){
//        okayButton = getButton(BUTTON_POSITIVE);
//        okayButton.setTextColor(Color.CYAN);
//    }
}