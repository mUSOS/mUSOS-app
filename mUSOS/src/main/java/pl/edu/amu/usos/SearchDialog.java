package pl.edu.amu.usos;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class SearchDialog extends DialogFragment {

    public interface SearchDialogInputListener {
        void onFinishEditSearchDialog(String inputText);
    }

    public static SearchDialog newInstance() {
        return new SearchDialog();
    }

    @InjectView(R.id.search_query)
    EditText mQuery;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.search_dialog, container, false);
        ButterKnife.inject(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getDialog().setTitle("Szukaj");
    }

    @OnClick(R.id.search_btn)
    void onSearchClick() {
        String query = mQuery.getText().toString();
        SearchDialogInputListener activity = (SearchDialogInputListener) getActivity();
        activity.onFinishEditSearchDialog(query);
        this.dismiss();
    }

}
