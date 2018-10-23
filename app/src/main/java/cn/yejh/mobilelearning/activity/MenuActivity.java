package cn.yejh.mobilelearning.activity;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;
import cn.yejh.mobilelearning.R;

public class MenuActivity extends Activity {
    Button btnStart, btnIntroduce, btnExit;// 开始答题、软件介绍、软件退出 按钮
    private long ExitTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu);
        btnStart = findViewById(R.id.btnStart);
        btnIntroduce = findViewById(R.id.btnIntroduce);
        btnExit = findViewById(R.id.btnExit);

        View.OnClickListener clickEvent = new ClickEvent();
        btnStart.setOnClickListener(clickEvent);
        btnIntroduce.setOnClickListener(clickEvent);
        btnExit.setOnClickListener(clickEvent);

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

    class ClickEvent implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btnStart:
                    Intent intent = new Intent(MenuActivity.this, Menu2Activity.class);
                    startActivity(intent);
                    break;
                case R.id.btnIntroduce:
                    Intent intent2 = new Intent(MenuActivity.this, HelpActivity.class);
                    startActivity(intent2);
                    break;
                case R.id.btnExit:
                    finish();
                    break;
            }
        }
    }

    public void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    // 按两次退出程序
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
