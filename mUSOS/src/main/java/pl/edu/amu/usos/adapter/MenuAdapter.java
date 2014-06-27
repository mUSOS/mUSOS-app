package pl.edu.amu.usos.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import pl.edu.amu.usos.R;

public class MenuAdapter extends BaseAdapter {

    public static final int DASHBOARD = 0;
    public static final int SCHEDULE = 1;
    public static final int REMINDER = 2;
    public static final int SUBJECTS = 3;
    public static final int GROUPS = 4;
    public static final int SETTINGS = 5;
    public static final int LOGOUT = 6;

    public static class MenuItem {
        private int mIconResId;
        private int mTextResId;

        public MenuItem(int iconResId, int textResId) {
            mIconResId = iconResId;
            mTextResId = textResId;
        }

        public int getIconResId() {
            return mIconResId;
        }

        public int getTextResId() {
            return mTextResId;
        }
    }

    private List<MenuItem> mMenuItems = new ArrayList<MenuItem>();
    private LayoutInflater mInflater;

    public MenuAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
        initItems();
    }

    private void initItems() {
        mMenuItems.add(new MenuItem(R.drawable.ic_menu_dashboard, R.string.menu_dashboard));
        mMenuItems.add(new MenuItem(R.drawable.ic_menu_plan, R.string.menu_plan));
        mMenuItems.add(new MenuItem(R.drawable.ic_plus, R.string.menu_add_reminder));
        mMenuItems.add(new MenuItem(R.drawable.ic_menu_grades, R.string.menu_subjects));
        mMenuItems.add(new MenuItem(R.drawable.ic_menu_groups, R.string.menu_groups));
        mMenuItems.add(new MenuItem(R.drawable.ic_menu_settings, R.string.menu_settings));
        mMenuItems.add(new MenuItem(R.drawable.ic_menu_logout, R.string.menu_logout));
    }

    @Override
    public int getCount() {
        return mMenuItems.size();
    }

    @Override
    public Object getItem(int position) {
        return mMenuItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final View view = mInflater.inflate(R.layout.menu_item, parent, false);
        TextView tv = (TextView) view.findViewById(android.R.id.text1);
        ImageView iv = (ImageView) view.findViewById(android.R.id.icon);

        final MenuItem item = mMenuItems.get(position);

        tv.setText(item.getTextResId());
        if (item.getIconResId() > 0) {
            iv.setImageResource(item.getIconResId());
        }

        return view;
    }
}
