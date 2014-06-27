package pl.edu.amu.usos.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.devspark.robototextview.widget.RobotoTextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import pl.edu.amu.usos.R;
import pl.edu.amu.usos.content.Reminder;

import static com.google.common.base.Preconditions.checkNotNull;

public class ReminderListAdapter extends BaseAdapter {

    private final LayoutInflater mInflater;
    private DateFormat mDateFormat = new SimpleDateFormat("EEEE, dd MMMM - HH:mm");
    private List<Reminder> mData = new ArrayList<Reminder>();

    public ReminderListAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
    }

    public void swapData(List<Reminder> reminders) {
        checkNotNull(reminders);
        mData = reminders;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.reminder_item, parent, false);
            ViewHolder holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }

        ViewHolder holder = (ViewHolder) convertView.getTag();
        Reminder reminder = (Reminder) getItem(position);
        holder.title.setText(reminder.title);
        holder.date.setText(mDateFormat.format(reminder.getStartDate()));

        return convertView;
    }

    class ViewHolder {
        @InjectView(R.id.reminder_date)
        RobotoTextView date;
        @InjectView(R.id.reminder_title)
        RobotoTextView title;

        ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }
}
