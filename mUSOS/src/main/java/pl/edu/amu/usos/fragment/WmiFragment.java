package pl.edu.amu.usos.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import butterknife.ButterKnife;
import butterknife.OnClick;
import pl.edu.amu.usos.R;

public class WmiFragment extends BaseFragment implements GoogleMap.OnMapClickListener {

    private SupportMapFragment mMapFragment;
    private GoogleMap mMap;

    @OnClick(R.id.phone_btn)
    void onPhoneClick() {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:618295308"));
        startActivity(Intent.createChooser(intent, getString(R.string.make_call)));
    }

    @OnClick(R.id.email_btn)
    void onEmailClick() {
        final Intent intent = new Intent(android.content.Intent.ACTION_SEND);
        intent.setType("plain/text");
        intent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{"wmiuam@amu.edu.pl"});
        startActivity(Intent.createChooser(intent, getString(R.string.send_email)));
    }

    private static final LatLng WMI_LAT_LNG = new LatLng(52.467146, 16.927477);

    public static Fragment newInstance() {
        return new WmiFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.wmi_layout, container, false);
        ButterKnife.inject(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        FragmentManager fm = getChildFragmentManager();
        mMapFragment = (SupportMapFragment) fm.findFragmentById(R.id.map_container);
        if (mMapFragment == null) {
            mMapFragment = new SupportMapFragment().newInstance();
            fm.beginTransaction().replace(R.id.map_container, mMapFragment).commit();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        initMap();
    }

    private void initMap() {
        if (mMap == null) {
            mMap = mMapFragment.getMap();
        }

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(WMI_LAT_LNG, 14));
        mMap.addMarker(new MarkerOptions()
                        .position(WMI_LAT_LNG)
                        .title("WMI"));

        final UiSettings uiSettings = mMap.getUiSettings();
        uiSettings.setAllGesturesEnabled(false);
        uiSettings.setZoomControlsEnabled(false);
        mMap.setOnMapClickListener(this);
    }

    @Override
    public void onMapClick(LatLng latLng) {
        String label = "WMI";
        String uriBegin = "geo:" + WMI_LAT_LNG.latitude + "," + WMI_LAT_LNG.longitude;
        String query = WMI_LAT_LNG.latitude + "," + WMI_LAT_LNG.longitude + "(" + label + ")";
        String encodedQuery = Uri.encode(query);
        String uriString = uriBegin + "?q=" + encodedQuery + "&z=16";
        Uri uri = Uri.parse(uriString);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }
}
