package pl.edu.amu.usos;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.DatePicker;
import android.widget.TimePicker;

import java.util.Calendar;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import static com.google.common.base.Preconditions.checkNotNull;

public class DateTimePickerDialog extends DialogFragment {

    public interface OnDateSetListener {

        public void onDateSet(int id, long time);

    }

    public static final String ARG_ID = "arg_id";
    public static final String ARG_TIME = "arg_time";

    public static DateTimePickerDialog newInstance(int id) {
        return newInstance(id, 0);
    }

    public static DateTimePickerDialog newInstance(int id, long time) {
        DateTimePickerDialog dialog = new DateTimePickerDialog();
        Bundle args = new Bundle();
        args.putInt(ARG_ID, id);
        args.putLong(ARG_TIME, time);
        dialog.setArguments(args);
        return dialog;
    }

    @InjectView(R.id.date_picker)
    DatePicker mDatePicker;
    @InjectView(R.id.time_picker)
    TimePicker mTimePicker;

    private int mId;
    private long mDate;

    private OnDateSetListener mListener;

    @OnClick(R.id.ok_btn)
    void onDoneClick() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MINUTE, mTimePicker.getCurrentMinute());
        calendar.set(Calendar.HOUR_OF_DAY, mTimePicker.getCurrentHour());
        calendar.set(Calendar.DAY_OF_MONTH, mDatePicker.getDayOfMonth());
        calendar.set(Calendar.MONTH, mDatePicker.getMonth());
        calendar.set(Calendar.YEAR, mDatePicker.getYear());

        final long time = calendar.getTimeInMillis();

        if (mListener != null) {
            mListener.onDateSet(mId, time);
        }

        dismiss();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = checkNotNull(getArguments());
        mId = args.getInt(ARG_ID);
        mDate = args.getLong(ARG_TIME);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.date_time_picker, container, false);
        ButterKnife.inject(this, view);
        return view;
    }

    public void setListener(OnDateSetListener listener) {
        mListener = listener;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);

        final Calendar calendar = Calendar.getInstance();

        if (mDate > 0) {
            calendar.setTimeInMillis(mDate);
        }

        mDatePicker.updateDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));

        mTimePicker.setIs24HourView(true);
        mTimePicker.setCurrentHour(calendar.get(Calendar.HOUR_OF_DAY));
        mTimePicker.setCurrentMinute(calendar.get(Calendar.MINUTE));
    }
}
