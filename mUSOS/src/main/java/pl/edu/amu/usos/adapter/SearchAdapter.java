package pl.edu.amu.usos.adapter;


import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import pl.edu.amu.usos.R;
import pl.edu.amu.usos.api.model.TeacherSearchItem;

import static com.google.common.base.Preconditions.checkNotNull;

public class SearchAdapter extends BaseAdapter {

    private List<TeacherSearchItem> mTeachers = new ArrayList<TeacherSearchItem>();
    private LayoutInflater mInflater;

    public SearchAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
    }

    public void swapData(List<TeacherSearchItem> data) {
        mTeachers = checkNotNull(data);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mTeachers.size();
    }

    @Override
    public TeacherSearchItem getItem(int position) {
        return mTeachers.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView = mInflater.inflate(R.layout.search_item, parent, false);
        TextView tv = (TextView) rowView.findViewById(R.id.teacher_name);
        TeacherSearchItem teacher = getItem(position);
        tv.setText(Html.fromHtml(teacher.match));

        return rowView;
    }

    public String getTeacherId(int position) {
        TeacherSearchItem teacher = mTeachers.get(position);
        return teacher.user.id;
    }
}
