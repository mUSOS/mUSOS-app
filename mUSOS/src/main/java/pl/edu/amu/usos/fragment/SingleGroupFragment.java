package pl.edu.amu.usos.fragment;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.Arrays;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import pl.edu.amu.usos.R;

public class SingleGroupFragment extends DialogFragment {

    public static final String ARGS_NAME = "args_name";
    public static final String ARGS_STUDENTS = "args_students";

    @InjectView(R.id.list_view)
    ListView mListView;

    private String mName;
    private ArrayAdapter<String> mAdapter;
    private List<String> mStudents;

    @OnClick(R.id.ok_btn)
    void onOkClick() {
        dismiss();
    }

    public static DialogFragment newInstance(String name, List<String> students) {
        final Bundle args = new Bundle();
        args.putString(ARGS_NAME, name);
        args.putStringArray(ARGS_STUDENTS, students.toArray(new String[students.size()]));
        DialogFragment dialog = new SingleGroupFragment();
        dialog.setArguments(args);
        return dialog;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.single_group_layout, container, false);
        ButterKnife.inject(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle args = getArguments();
        String[] st = args.getStringArray(ARGS_STUDENTS);
        mStudents = Arrays.asList(st);
        mName = args.getString(ARGS_NAME);
        getDialog().setTitle(mName);

        mAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1,
                android.R.id.text1, mStudents);
        mListView.setAdapter(mAdapter);
    }
}
