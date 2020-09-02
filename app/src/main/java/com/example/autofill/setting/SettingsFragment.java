package com.example.autofill.setting;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

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

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.root_preferences,rootKey);
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

                break;
            case "title_empty_data":
                /**
                 * 清空所有数据
                 */
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("你确定要清空所有数据吗");
                builder.setMessage("清空的数据将无法恢复,清空数据后，重启APP生效");
                builder.setPositiveButton("确认", new
                        DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                /**
                                 * 下面进行删除数据操作
                                 */
                                INSTANCE = InformationRoomDatabase.getDatabase(getContext());
                                if(INSTANCE!=null){
                                    InformationDao informationDao = INSTANCE.informationDao();
                                    new InformationRepository.deleteAllStateAsyncTask(informationDao).execute();
                                    Toast.makeText(getContext(),"数据已清空",Toast.LENGTH_SHORT).show();
                                }else{
                                    Toast.makeText(getContext(),"数据未成功清空，请重试",Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                builder.show();

                break;
            default:

        }
        return super.onPreferenceTreeClick(preference);
    }
}