package pl.edu.amu.usos.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import pl.edu.amu.usos.R;
import pl.edu.amu.usos.adapter.ReminderListAdapter;
import pl.edu.amu.usos.content.Reminder;

public class ReminderListFragment extends BaseFragment {

    @InjectView(R.id.list_view)
    ListView mListView;
    @InjectView(R.id.no_reminders_label)
    TextView mEmptyLabel;

    private ReminderListAdapter mAdapter;

    public static BaseFragment newInstance() {
        return new ReminderListFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.reminder_list_fragment, container, false);
        ButterKnife.inject(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mAdapter = new ReminderListAdapter(getActivity());
        mListView.setAdapter(mAdapter);
        final List<Reminder> reminders = getReminders();
        mAdapter.swapData(reminders);
        mEmptyLabel.setVisibility(reminders.size() == 0 ? View.VISIBLE : View.GONE);
    }

    private List<Reminder> getReminders() {
        final long time = System.currentTimeMillis();
        return Reminder.findWithQuery(Reminder.class, "Select * from Reminder where " +
                "start > ? ORDER BY start ASC LIMIT 10", String.valueOf(time));
    }
}
