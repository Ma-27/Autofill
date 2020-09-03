package com.example.autofill.setting;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreference;

import com.example.autofill.MainActivity;
import com.example.autofill.R;
import com.example.autofill.network.prepareRequest.FillStationParse;
import com.example.autofill.storage.InformationDao;
import com.example.autofill.storage.InformationRepository;
import com.example.autofill.storage.InformationRoomDatabase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import static com.example.autofill.ui.main.MrdkListAdapter.xh;

public class SettingsFragment extends PreferenceFragmentCompat {

    private static InformationRoomDatabase INSTANCE;
    private static final String TAG = "SettingsFragment成功";
    //SwitchPreference GmsPreference;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.root_preferences,rootKey);
       // GmsPreference = findPreference("retrieve_registration_switch");
    }

    /**
     * 设置监听
     * @param preference
     * @return
     */
    @Override
    public boolean onPreferenceTreeClick(Preference preference) {
        switch (preference.getKey()){
            case "retrieve_registration_switch":
                /**
                 * 检索当前令牌
                 */
                FirebaseInstanceId.getInstance().getInstanceId()
                        .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                            @Override
                            public void onComplete(@NonNull Task<InstanceIdResult> task) {
                                if (!task.isSuccessful()) {
                                    Log.w(TAG, "getInstanceId 失败", task.getException());
                                    Toast.makeText(getContext(), "getInstanceId 失败", Toast.LENGTH_SHORT).show();
                                    return;
                                }

                                // Get new Instance ID token
                                String token = task.getResult().getToken();

                                // Log and toast
                                String msg = getString(R.string.msg_token_fmt, token);
                                Log.d(TAG, msg);
                                Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
                            }
                        });
            break;


            case "send_service":
                /**
                 * 启用gms
                 */
                Toast.makeText(getContext(), R.string.not_available_show, Toast.LENGTH_SHORT).show();
                /*
                GmsPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                    @Override
                    public boolean onPreferenceChange(Preference preference, Object newValue) {
                        //启动统一推送服务，有缘再更
                        return false;
                    }
                });

                 */
                break;
            case "title_empty_data":
                /**
                 * 清空所有数据
                 */
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle(R.string.empty_data_warning);
                builder.setMessage(R.string.empty_data_warning_content);
                builder.setPositiveButton(R.string.menu_confirm, new
                        DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                /**
                                 * 下面进行删除数据操作
                                 */
                                INSTANCE = InformationRoomDatabase.getDatabase(getContext());
                                if(INSTANCE!=null){
                                    InformationDao informationDao = INSTANCE.informationDao();
                                    new InformationRepository.deleteAllStateAsyncTask(informationDao).execute();
                                    Toast.makeText(getContext(), R.string.empty_data_successful,Toast.LENGTH_SHORT).show();
                                }else{
                                    Toast.makeText(getContext(), R.string.empty_data_fail,Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                builder.setNegativeButton(R.string.menu_cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                builder.show();

                break;

            case "dark_mode":
                Toast.makeText(getContext(), R.string.not_available_show, Toast.LENGTH_SHORT).show();
                /*
                SwitchPreference darkMoodPreference  = findPreference("dark_mode");
                darkMoodPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                    @Override
                    public boolean onPreferenceChange(Preference preference, Object newValue) {
                        return false;
                    }
                });
                // Get the night mode state of the app.
                int nightMode = AppCompatDelegate.getDefaultNightMode();
                //Set the theme mode for the restarted activity
                if (nightMode == AppCompatDelegate.MODE_NIGHT_YES) {
                    AppCompatDelegate.setDefaultNightMode
                            (AppCompatDelegate.MODE_NIGHT_NO);
                }
                 */
                break;
            default:

        }
        return super.onPreferenceTreeClick(preference);
    }
}