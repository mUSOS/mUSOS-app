package pl.edu.amu.usos.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import pl.edu.amu.usos.R;
import pl.edu.amu.usos.content.SingleCourse;

import static com.google.common.base.Preconditions.checkNotNull;

public class DayAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private List<SingleCourse> mData;
    private SimpleDateFormat mDateFormat;

    public DayAdapter(Context context, List<SingleCourse> courses) {
        mInflater = LayoutInflater.from(context);
        mData = checkNotNull(courses);
        mDateFormat = new SimpleDateFormat("HH:mm");
    }

    public void swapData(List<SingleCourse> courses) {
        mData = checkNotNull(courses);
        notifyDataSetInvalidated();
    }

    static class ViewHolder {
        @InjectView(R.id.time_label)
        TextView time;
        @InjectView(R.id.class_label)
        TextView name;
        @InjectView(R.id.class_place)
        TextView place;
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
            convertView = mInflater.inflate(R.layout.class_item, parent, false);
            ViewHolder holder = new ViewHolder();
            ButterKnife.inject(holder, convertView);
            convertView.setTag(holder);
        }

        ViewHolder holder = (ViewHolder) convertView.getTag();
        SingleCourse course = mData.get(position);

        String start = mDateFormat.format(new Date(course.start));
        String end = mDateFormat.format(new Date(course.end));

        holder.time.setText(start + " - " + end);
        holder.name.setText(course.name);
        holder.place.setText(course.room_number);

        return convertView;
    }
}
