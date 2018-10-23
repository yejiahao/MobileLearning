package cn.yejh.mobilelearning.activity;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.view.*;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import cn.yejh.mobilelearning.db.DBManager;
import cn.yejh.mobilelearning.db.User;
import cn.yejh.mobilelearning.R;
import cn.yejh.mobilelearning.util.StringUtils;

import java.util.Objects;

public class RegisterActivity extends Activity {
    EditText etUid2, etPwd2, etPwdConfig2;
    Button btnReg2;
    String username, password, passwordConfig;
    DBManager dbm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);// 设置了register.xml布局
        etUid2 = findViewById(R.id.etUid2);// 用户输入框
        etPwd2 = findViewById(R.id.etPwd2);// 密码输入框
        etPwdConfig2 = findViewById(R.id.etPwdConfig2);// 确认输入框
        btnReg2 = findViewById(R.id.btnReg2);// 注册按钮
        dbm = new DBManager(this);// 启动数据库

        btnReg2.setOnClickListener(v -> {
            username = etUid2.getText().toString().trim();
            password = etPwd2.getText().toString().trim();
            passwordConfig = etPwdConfig2.getText().toString().trim();

            // 注册的逻辑判断
            if (Objects.equals(password, passwordConfig)) {
                if (StringUtils.isBlank(username) || StringUtils.isBlank(password)) {
                    showToast("用户名或密码不能为空！");
                } else {
                    User user = new User(username, password);
                    if (dbm.registerUser(user)) {
                        showToast("此用户已存在！");
                    } else {
                        dbm.addUser(user);
                        showToast("创建用户成功！");
                        new Handler().postDelayed(() -> finish(), 1000);
                    }
                }
            } else {
                showToast("密码确认有误！");
            }
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
    protected void onDestroy() {
        super.onDestroy();
        dbm.closeDB();// 关闭数据库
    }

    public void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();// Toast消息显示
    }
}
