package cn.yejh.mobilelearning.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.*;
import android.widget.*;
import cn.yejh.mobilelearning.db.DBManager;
import cn.yejh.mobilelearning.db.User;
import cn.yejh.mobilelearning.global.GlobalValue;
import cn.yejh.mobilelearning.R;

public class LoginActivity extends Activity {
    public static final String SP_INFOS = "SPDATA_Files";
    public static final String USERID = "UserID";
    public static final String PASSWORD = "PassWord";
    private static CheckBox cb;
    private static String uidStr;
    private static String pwdStr;
    private long ExitTime;
    DBManager dbm;
    EditText etUid, etPwd;
    Button btnLogin, btnReg;
    TextView tv1;
    String username, password;

    int l_count = 0;
    ProgressDialog l_pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        dbm = new DBManager(this);// 启动数据库
        etUid = findViewById(R.id.etUid);
        etPwd = findViewById(R.id.etPwd);
        btnLogin = findViewById(R.id.btnLogin);
        btnReg = findViewById(R.id.btnReg);
        tv1 = findViewById(R.id.tv1);
        cb = findViewById(R.id.cbRemember);
        tv1.setVisibility(View.INVISIBLE);
        checkIfRemember();
        btnLogin.setOnClickListener(v -> {
            username = etUid.getText().toString().trim();
            password = etPwd.getText().toString().trim();
            User user = new User(username, password);
            l_count = 0;
            l_pDialog = new ProgressDialog(LoginActivity.this);
            l_pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            l_pDialog.setTitle("logining...");
            l_pDialog.setMessage("正在登录，请稍后...");
            l_pDialog.setProgress(100);
            l_pDialog.setIndeterminate(false);
            l_pDialog.setCancelable(true);
            l_pDialog.show();
            if (dbm.validateUser(user)) {
                GlobalValue.name = etUid.getText().toString().trim();

                new Thread(() -> {
                    try {
                        while (l_count <= 100) {
                            l_pDialog.setProgress(l_count++);
                            Thread.sleep(40);
                        }
                        Message message = new Message();
                        message.what = 0;
                        mHandler.sendMessage(message);
                    } catch (InterruptedException e) {
                        l_pDialog.cancel();
                    }
                }).start();

            } else {
                new Thread(() -> {
                    try {
                        while (l_count <= 100) {
                            l_pDialog.setProgress(l_count++);
                            Thread.sleep(40);
                        }
                        Message message = new Message();
                        message.what = 1;
                        mHandler.sendMessage(message);
                    } catch (InterruptedException e) {
                        l_pDialog.cancel();
                    }
                }).start();
            }
        });

        btnReg.setOnClickListener(v -> {
            Intent intent2 = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent2);
        });

    }

    public static class PlaceholderFragment extends Fragment {
        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_login, container, false);
            return rootView;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        username = etUid.getText().toString().trim();
        password = etPwd.getText().toString().trim();
        User user = new User(username, password);
        if (cb.isChecked() && dbm.validateUser(user)) {
            uidStr = etUid.getText().toString().trim();
            pwdStr = etPwd.getText().toString().trim();
            rememberMe(uidStr, pwdStr);
        } else {
            uidStr = null;
            pwdStr = null;
            rememberMe(uidStr, pwdStr);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dbm.closeDB();// 关闭数据库
    }

    public void rememberMe(String uid, String pwd) {
        SharedPreferences sp = getSharedPreferences(SP_INFOS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(USERID, uid);
        editor.putString(PASSWORD, pwd);
        editor.commit();
    }

    public void checkIfRemember() {
        SharedPreferences sp = getSharedPreferences(SP_INFOS, MODE_PRIVATE);
        uidStr = sp.getString(USERID, null);
        pwdStr = sp.getString(PASSWORD, null);
        if (uidStr != null && pwdStr != null) {
            etUid.setText(uidStr);
            etPwd.setText(pwdStr);
            cb.setChecked(true);
        } else if (uidStr == null && pwdStr == null) {
            etUid.setText("");// 给EditText控件赋帐号
            etPwd.setText("");// 给EditText控件赋密码
            cb.setChecked(false);
        }
    }

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            if (msg.what == 0) {
                l_pDialog.dismiss();
                Intent intent = new Intent(LoginActivity.this,
                        MenuActivity.class);
                startActivity(intent);// 跳转到MenuActivity
                finish();// 下一个页面按返回键直接跳出应用程序
            } else if (msg.what == 1) {
                l_pDialog.dismiss();
                tv1.setTextColor(Color.RED);
                tv1.setVisibility(View.VISIBLE);
            }
        }
    };

    public void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (System.currentTimeMillis() - ExitTime > 2000) {
                showToast("再按一次退出程序");
                ExitTime = System.currentTimeMillis();
            } else {
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
