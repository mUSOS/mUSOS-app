package pl.edu.amu.usos.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import pl.edu.amu.usos.R;
import pl.edu.amu.usos.api.model.University;

public class UniversityAdapter extends BaseAdapter {

    public static final University[] mData = new University[]{
            new University(0, "Uniwersytet im. Adama Mickiewicza",
                    "https://usosapps.amu.edu.pl",
                    "CONSUMER_KEY",
                    "CONSUMER_SECRET"),

            new University(1, "Uniwersytet Jagielloński",
                    "https://apps.usos.uj.edu.pl",
                    "CONSUMER_KEY",
                    "CONSUMER_SECRET"),

            new University(2, "Uniwersytet Mikołaja Kopernika",
                    "https://usosapps.umk.pl",
                    "CONSUMER_KEY",
                    "CONSUMER_SECRET"),

            new University(3, "Uniwersytet w Białymstoku",
                    "https://usosapps.uwb.edu.pl",
                    "CONSUMER_KEY",
                    "CONSUMER_SECRET"),

            new University(4, "Uniwersytet Warszawski",
                    "https://usosapps.uw.edu.pl",
                    "CONSUMER_KEY",
                    "CONSUMER_SECRET"),

            new University(5, "Uniwersytet Wrocławski",
                    "https://usosapps.uni.wroc.pl",
                    "CONSUMER_KEY",
                    "CONSUMER_SECRET"),

            new University(7, "Politechnika Białostocka",
                    "https://api.uci.pb.edu.pl",
                    "CONSUMER_KEY",
                    "CONSUMER_SECRET"),

            new University(8, "AWF Katowice",
                    "https://usosapps.awf.katowice.pl",
                    "CONSUMER_KEY",
                    "CONSUMER_SECRET"),

            new University(9, "Uniwersytet Kazimierza Wielkiego",
                    "https://api.ukw.edu.pl",
                    "CONSUMER_KEY",
                    "CONSUMER_SECRET"),

            new University(10, "Politechnika Świętokrzyska",
                    "https://api.usos.tu.kielce.pl",
                    "CONSUMER_KEY",
                    "CONSUMER_SECRET"),

            new University(11, "Wojskowa Akademia Techniczna",
                    "https://usosapps.wat.edu.pl",
                    "CONSUMER_KEY",
                    "CONSUMER_SECRET"),

            new University(12, "Uniwersytet Opolski",
                    "https://usosapps.uni.opole.pl",
                    "CONSUMER_KEY",
                    "CONSUMER_SECRET"),

            new University(13, "Uniwersytet Śląski",
                    "https://usosapps.us.edu.pl",
                    "CONSUMER_KEY",
                    "CONSUMER_SECRET"),

            new University(14, "Politechnika Warszawska",
                    "https://apps.usos.pw.edu.pl",
                    "CONSUMER_KEY",
                    "CONSUMER_SECRET"),

            new University(15, "Politechnika Rzeszowska",
                    "https://usosapps.prz.edu.pl",
                    "CONSUMER_KEY",
                    "CONSUMER_SECRET"),

            new University(16, "Uczelnia Techniczno-Handlowa",
                    "https://usosapps.uth.edu.pl",
                    "CONSUMER_KEY",
                    "CONSUMER_SECRET"),

            new University(17, "Uniwersytet Technologiczno-Przyrodniczy",
                    "https://usosapps.utp.edu.pl",
                    "CONSUMER_KEY",
                    "CONSUMER_SECRET"),

            new University(18, "Uniwersytet Warmińsko-Mazurski",
                    "https://apps.uwm.edu.pl",
                    "CONSUMER_KEY",
                    "CONSUMER_SECRET")
    };

    public static University getUniversity(int id) {
        for (University university : mData) {
            if (university.id == id) {
                return university;
            }
        }

        throw new IllegalArgumentException("Wrong university id");
    }

    private LayoutInflater mInflater;

    public UniversityAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return mData.length;
    }

    @Override
    public Object getItem(int position) {
        return mData[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getView(position, convertView, parent);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.university_item, parent, false);
        }

        University item = mData[position];
        ((TextView) convertView).setText(item.name);

        return convertView;
    }


}
