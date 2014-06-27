package pl.edu.amu.usos.helper;

import android.content.Intent;

import pl.edu.amu.usos.AppConsts;

public class IntentHelper {

    public static Intent getDashboardIntent() {
        return new Intent(AppConsts.DASHBOARD_ACTION);
    }

    public static Intent getReminderIntent() {
        return new Intent(AppConsts.REMINDER_ACTION);
    }

    public static Intent getScheduleIntent() {
        return new Intent(AppConsts.SCHEDULE_ACTION);
    }

    public static Intent getSubjectListIntent() {
        return new Intent(AppConsts.SUBJECT_LIST_ACTION );
    }

    public static Intent getGroupsIntent() {
        return new Intent(AppConsts.GROUPS_ACTION);
    }

    public static Intent getSearchIntent() { return new Intent(AppConsts.SEARCH_ACTION); }

    public static Intent getSettingsIntent() {
        return new Intent(AppConsts.PREFERENCE_ACTION);
    }

    public static Intent getWmiIntent() {
        return new Intent(AppConsts.WMI_ACTION);
    }
}
