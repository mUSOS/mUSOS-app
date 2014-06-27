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
import pl.edu.amu.usos.content.SingleGroup;

public class GroupAdapter extends BaseAdapter {

    private List<SingleGroup> mGroups;
    private final LayoutInflater mInflater;

    public GroupAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
    }

    static class ViewHolder {
        @InjectView(R.id.subject_label)
        TextView label;
        @InjectView(R.id.subject_type)
        TextView type;
    }


    @Override
    public int getCount() {
        return mGroups == null ? 0 : mGroups.size();
    }

    @Override
    public Object getItem(int position) {
        return mGroups.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.subject_item, parent, false);
            ViewHolder holder = new ViewHolder();
            ButterKnife.inject(holder, convertView);
            convertView.setTag(holder);
        }

        SingleGroup group = mGroups.get(position);
        ViewHolder holder = (ViewHolder) convertView.getTag();
        holder.label.setText(group.groupName);
        holder.type.setText(group.getType());
        holder.type.setVisibility(View.VISIBLE);

        return convertView;
    }

    public void swapData(List<SingleGroup> data) {
        mGroups = data;
        notifyDataSetChanged();
    }
}
