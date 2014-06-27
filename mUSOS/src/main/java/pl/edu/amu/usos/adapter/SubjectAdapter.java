package pl.edu.amu.usos.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import pl.edu.amu.usos.R;
import pl.edu.amu.usos.content.SingleCourse;

import static com.google.common.base.Preconditions.checkNotNull;

public class SubjectAdapter extends BaseAdapter {

    private final List<SingleCourse> mCourses;
    private final LayoutInflater mInflater;

    public SubjectAdapter(Context context, List<SingleCourse> courses) {
        mInflater = LayoutInflater.from(context);
        mCourses = checkNotNull(courses);
    }

    static class ViewHolder {
        @InjectView(R.id.subject_label)
        TextView label;
    }

    @Override
    public int getCount() {
        return mCourses.size();
    }

    @Override
    public Object getItem(int position) {
        return mCourses.get(position);
    }

    @Override
    public long getItemId(int position) {
        return mCourses.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.subject_item, parent, false);
            ViewHolder holder = new ViewHolder();
            ButterKnife.inject(holder, convertView);
            convertView.setTag(holder);
        }

        SingleCourse course = mCourses.get(position);
        ViewHolder holder = (ViewHolder) convertView.getTag();
        holder.label.setText(course.name);

        return convertView;
    }
}
