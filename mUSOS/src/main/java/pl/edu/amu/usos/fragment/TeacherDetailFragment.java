package pl.edu.amu.usos.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import pl.edu.amu.usos.R;
import pl.edu.amu.usos.api.model.Teacher;
import pl.edu.amu.usos.loaders.TeacherLoader;

import static com.google.common.base.Preconditions.checkNotNull;

public class TeacherDetailFragment extends DialogFragment implements LoaderCallbacks<Teacher> {

    private static final String ARG_ID = "arg_id";
    private static final int LOADER_ID = 1;

    public static DialogFragment newInstance(String id) {
        final Bundle args = new Bundle();
        args.putString(ARG_ID, id);
        DialogFragment fragment = new TeacherDetailFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private String mTeacherId;

    @InjectView(R.id.progress)
    View mProgressBar;
    @InjectView(R.id.content)
    View mContent;
    @InjectView(R.id.person_name)
    TextView mName;
    @InjectView(R.id.person_function)
    TextView mFunction;
    @InjectView(R.id.person_room)
    TextView mRoom;
    @InjectView(R.id.person_www)
    TextView mWebsite;

    public TeacherDetailFragment() {
        super();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Bundle args = checkNotNull(getArguments());
        mTeacherId = args.getString(ARG_ID);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.teacher_detail_fragment, container, false);
        ButterKnife.inject(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);

        getLoaderManager().initLoader(LOADER_ID, null, this).forceLoad();
    }

    @Override
    public Loader<Teacher> onCreateLoader(int id, Bundle args) {
        showProgress(true);
        return new TeacherLoader(getActivity(), mTeacherId);
    }

    @Override
    public void onLoadFinished(Loader<Teacher> loader, final Teacher data) {
        showProgress(false);
        if (data != null) {
            mName.setText(data.firstName + " " + data.lastName);
            if (data.employmentPositions.size() > 0) {
                mFunction.setText("Funkcja: " + data.employmentPositions.get(0).position.name.pl);
            } else {
                mFunction.setText("Funkcja: brak danych");
            }

            if (TextUtils.isEmpty(data.room)) {
                mRoom.setText("Pokój: brak danych");
            } else {
                mRoom.setText("Pokój: " + data.room);
            }

            if (TextUtils.isEmpty(data.profileUrl)) {
                mWebsite.setText("www: brak danych");
                mWebsite.setOnClickListener(null);
            } else {
                mWebsite.setText(getUrl(data.profileUrl));
                mWebsite.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(Intent.ACTION_VIEW);
                        i.setData(Uri.parse(getUrl(data.profileUrl)));
                        startActivity(Intent.createChooser(i, getString(R.string.make_call)));
                    }
                });
            }
        }
    }

    @Override
    public void onLoaderReset(Loader<Teacher> loader) {

    }

    private void showProgress(boolean show) {
        mProgressBar.setVisibility(show ? View.VISIBLE : View.GONE);
        mContent.setVisibility(show ? View.GONE : View.VISIBLE);
    }

    private String getUrl(String url) {
        if (TextUtils.isEmpty(url)) {
            return url;
        }

        if (!url.contains("usosweb.amu.edu.pl/")) {
            url = url.replace("usosweb.amu.edu.pl", "usosweb.amu.edu.pl/");
        }

        return url;
    }
}
