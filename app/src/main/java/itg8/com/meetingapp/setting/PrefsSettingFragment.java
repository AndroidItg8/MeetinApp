package itg8.com.meetingapp.setting;


import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.util.Log;

import itg8.com.meetingapp.R;
import itg8.com.meetingapp.common.CommonMethod;
import itg8.com.meetingapp.common.Prefs;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PrefsSettingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PrefsSettingFragment extends PreferenceFragmentCompat {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Preference pref;


    public PrefsSettingFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PrefsSettingFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PrefsSettingFragment newInstance(String param1, String param2) {
        PrefsSettingFragment fragment = new PrefsSettingFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {

        addPreferencesFromResource(R.xml.app_pref);

        setMeetingMode();
        setPrioritySetting();
        setNotificationSetting();

    }

    private void setNotificationSetting() {
        final Preference notificationSoundPref = findPreference(getString(R.string.pref_notification_sound));
        final Preference notificationVibratePref = findPreference(getString(R.string.pref_notification_vibrate));
        final Preference notificationTogglePref = findPreference(getString(R.string.pref_notification_vibrate));
        final String[] singleChoiceItems = getResources().getStringArray(R.array.dialog_single_choice_array);
        final String title = getString(R.string.notificatin_ringtone);
        setNotificationVibratePref(notificationVibratePref);
        setNotificationTogglePref(notificationTogglePref);


        notificationSoundPref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {

            @Override
            public boolean onPreferenceClick(Preference preference) {

                openPrefDialogue(singleChoiceItems, title, notificationSoundPref);
                return false;
            }
        });

        notificationVibratePref.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                Prefs.putBoolean(CommonMethod.SETTING_PREF_NOTIFICATION_VIBRATE, (Boolean) newValue);
                setNotificationVibratePref(notificationVibratePref);
                Log.d("TAG", "Prefs News Values:" + newValue.toString());
                return true;
            }
        });


        notificationTogglePref.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                Prefs.putBoolean(CommonMethod.SETTING_PREF_NOTIFICATION_TOGGLE, (Boolean) newValue);
                setNotificationTogglePref(notificationTogglePref);
                Log.d("TAG", "Prefs News Values:" + newValue.toString());
                return true;
            }
        });



    }

    private void setNotificationTogglePref(Preference notificationTogglePref) {
        if (Prefs.getBoolean(CommonMethod.SETTING_PREF_NOTIFICATION_TOGGLE, false)) {
            notificationTogglePref.setSummary("Hide quick menu on notification bar");
        } else {
            notificationTogglePref.setSummary("Show quick menu on notification bar ");
        }
    }

    private void setNotificationVibratePref(Preference postureKey) {
        if (Prefs.getBoolean(CommonMethod.SETTING_PREF_NOTIFICATION_VIBRATE, false)) {
            postureKey.setSummary("UnCheck For Vibration at Meeting Mode");
        } else {
            postureKey.setSummary("Check For Vibration at Meeting Mode");
        }
    }

    private void setPrioritySetting() {
//        final Preference reminderPrioritiesPref = findPreference(getString(R.string.pref_reminder_priories));
        final Preference highPrioritiesPref = findPreference(getString(R.string.pref_high_priories));
        final Preference mediumPrioritiesPref = findPreference(getString(R.string.pref_medium_priories));
        final Preference lowPrioritiesPref = findPreference(getString(R.string.pref_low_priories));


        final String[] singleChoiceItems = getResources().getStringArray(R.array.dialog_single_choice_array_priorities);
        final String[] highSingleChoiceItems = getResources().getStringArray(R.array.dialog_single_choice_array_high_priorities);
        final String[] mediumSingleChoiceItems = getResources().getStringArray(R.array.dialog_single_choice_array_medium_priorities);
        final String[] lowSingleChoiceItems = getResources().getStringArray(R.array.dialog_single_choice_array_low_priorities);
        final String titleHigh = getString(R.string.pref_high_priories);
        final String titleMedium = getString(R.string.pref_medium_priories);
        final String titleLow = getString(R.string.pref_low_priories);
        final String title = getString(R.string.pref_reminder_priories);
//        reminderPrioritiesPref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
//            @Override
//            public boolean onPreferenceClick(Preference preference) {
//
//                pref = reminderPrioritiesPref;
//                openPrefDialogue(singleChoiceItems, title, reminderPrioritiesPref);
//                return false;
//            }
//        });

        highPrioritiesPref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {

                openPrefDialogue(highSingleChoiceItems, titleHigh, highPrioritiesPref);
                return false;
            }
        });


        mediumPrioritiesPref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
//                int itemSelected = checkItemSelect(titleMedium, mediumSingleChoiceItems);
//                checkTitle(titleMedium, itemSelected, mediumSingleChoiceItems, mediumPrioritiesPref);
                openPrefDialogue(mediumSingleChoiceItems, titleMedium, mediumPrioritiesPref);
                return false;
            }
        });


        lowPrioritiesPref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {



                openPrefDialogue(lowSingleChoiceItems, titleLow, lowPrioritiesPref);
                return false;
            }
        });






    }



    private void setMeetingMode() {
//        Preference meetingPref = findPreference(getString(R.string.pref_meeting));
        final Preference doNotDisturbPref = findPreference(getString(R.string.pref_do_not_disturb));
        final Preference vibrateMeetingPref = findPreference(getString(R.string.pref_do_not_vibrate));
        final Preference soundMeetingPref = findPreference(getString(R.string.pref_meeting_sound));
        final String[] singleChoiceItems = getResources().getStringArray(R.array.dialog_single_choice_array);
        final String title = getString(R.string.select_ringtone);
        setMeetingNotDisturbPref(doNotDisturbPref);
        setMeetingVibratePref(vibrateMeetingPref);

        soundMeetingPref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
//                SecurityFragment fragment = SecurityFragment.newInstance("", "");
//                android.support.v4.app.FragmentTransaction ft = getFragmentManager().beginTransaction();
//                ft.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);
//                ft.replace(R.id.frame_container, fragment, fragment.getClass().getSimpleName());
//                ft.addToBackStack(fragment.getClass().getSimpleName());
//                ft.commit();
                openPrefDialogue(singleChoiceItems, title, soundMeetingPref);
                return false;
            }
        });
//        meetingPref.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
//            @Override
//            public boolean onPreferenceChange(Preference preference, Object newValue) {
//                Prefs.putBoolean(CommonMethod.SETTING_PREF_MEETING, (Boolean) newValue);
//                Log.d("TAG","Prefs News Values:"+newValue.toString());
//                return true;
//            }
//        });

        doNotDisturbPref.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                Prefs.putBoolean(CommonMethod.SETTING_PREF_MEETING_NOT_DISTURB, (Boolean) newValue);
                Log.d("TAG", "Prefs News Values:" + newValue.toString());
                setMeetingNotDisturbPref(doNotDisturbPref);
                return true;
            }
        });

        vibrateMeetingPref.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                Prefs.putBoolean(CommonMethod.SETTING_PREF_MEETING_VIBRATE, (Boolean) newValue);
                Log.d("TAG", "Prefs NewsVibrate  Values:" + newValue.toString());
                setMeetingVibratePref(vibrateMeetingPref);
                return true;
            }
        });


    }

    private void setMeetingVibratePref(Preference vibrateMeetingPref) {
        if (Prefs.getBoolean(CommonMethod.SETTING_PREF_MEETING_VIBRATE, false)) {
            vibrateMeetingPref.setSummary("UnCheck For Vibration at Meeting Mode");
        } else {
            vibrateMeetingPref.setSummary("Check For Vibration at Meeting Mode");
        }
    }

    private void setMeetingNotDisturbPref(Preference doNotDisturbPref) {
        if (Prefs.getBoolean(CommonMethod.SETTING_PREF_MEETING_NOT_DISTURB, false)) {
            doNotDisturbPref.setSummary("Switching For  Disturb At Meeting Mode");
        } else {
            doNotDisturbPref.setSummary("Switching For  Do NOt Disturb At Meeting Mode");
        }
    }

    private void openPrefDialogue(final String[] singleChoiceItems, final String title, final Preference preference) {
        int itemSelected = checkItemSelect(title, singleChoiceItems);
        int item;
        if (itemSelected > 0) {
            item = itemSelected;
        } else
            item = 0;

        new AlertDialog.Builder(getActivity())
                .setTitle(title)
                .setSingleChoiceItems(singleChoiceItems, item, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int selectedIndex) {
                        checkTitle(title, selectedIndex, singleChoiceItems, preference);

                    }
                })
//                .setPositiveButton("Ok", null)
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .show();
    }

    private int checkItemSelect(String title, String[] singleChoiceItems) {
        int value = 0;
        if (title.equalsIgnoreCase(getString(R.string.pref_low_priories))) {
            value = Prefs.getInt(CommonMethod.PRIORITY_LOW);

        }
        if (title.equalsIgnoreCase(getString(R.string.pref_medium_priories))) {
            value = Prefs.getInt(CommonMethod.PRIORITY_MEDIUM);
        }
        if (title.equalsIgnoreCase(getString(R.string.pref_high_priories))) {
            value = Prefs.getInt(CommonMethod.PRIORITY_HIGH);
        }

//        int position =new ArrayList<String>(Arrays.asList(singleChoiceItems)).indexOf(value);
        int position = getItemValue(singleChoiceItems, value);
        Log.d("TAG", "Prefs position  Values:" + position);
        return position;
    }

    private int getItemValue(String[] singleChoiceItems, int value) {
        int position = 0;

        for (int i = 0; i <= singleChoiceItems.length - 1; i++) {
            if (singleChoiceItems[i].equalsIgnoreCase(String.valueOf(value))) {
                position = i;

            }
        }
        return position;
    }

    private void checkTitle(String title, int position, String[] singleChoiceItems, Preference preference) {
        if (title.equalsIgnoreCase(getString(R.string.pref_low_priories))) {
            Prefs.putInt(CommonMethod.PRIORITY_LOW, Integer.parseInt(singleChoiceItems[position]));
            preference.setSummary("You Have Set " + Integer.parseInt(singleChoiceItems[position])+" Notifications For Low Level");

            Log.d("TAG", "Prefs NewsVibrate  Values:" + singleChoiceItems[position]);
        }
        if (title.equalsIgnoreCase(getString(R.string.pref_medium_priories))) {
            Prefs.putInt(CommonMethod.PRIORITY_MEDIUM, Integer.parseInt(singleChoiceItems[position]));
            preference.setSummary(" You Have Set " + Integer.parseInt(singleChoiceItems[position])+" Notifications For Medium Level");

        }
        if (title.equalsIgnoreCase(getString(R.string.pref_high_priories))) {
            Prefs.putInt(CommonMethod.PRIORITY_HIGH, Integer.parseInt(singleChoiceItems[position]));
            preference.setSummary(" You have Set " + Integer.parseInt(singleChoiceItems[position])+" Notifications For High Level");

        }
         if(title.equalsIgnoreCase(getString(R.string.notificatin_ringtone)))
         {
             Prefs.putInt(CommonMethod.NOTIFICATION_RINGTONE, Integer.parseInt(singleChoiceItems[position]));
             preference.setSummary(" You have Set " + Integer.parseInt(singleChoiceItems[position])+" Notifications For High Level");
         }

    }


}
