package cn.yejh.mobilelearning.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.*;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import cn.yejh.mobilelearning.global.GlobalValue;
import cn.yejh.mobilelearning.R;

public class ExamActivity extends Activity {
    TextView tvName;
    ImageButton btnBackExam;
    Button btnStartExam;

    int e_count = 0;
    ProgressDialog e_pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.exam);
        tvName = findViewById(R.id.nameExam);
        btnBackExam = findViewById(R.id.btnBackExam);
        tvName.setText(GlobalValue.name + " 同学, 你好");// 取得登录的用户名
        btnStartExam = findViewById(R.id.btnStartExam);

        btnBackExam.setOnClickListener(v -> finish());

        btnStartExam.setOnClickListener(v -> {
            e_count = 0;
            e_pDialog = new ProgressDialog(ExamActivity.this);
            e_pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            e_pDialog.setTitle("loading...");
            e_pDialog.setMessage("正在加载试题，请稍后...");
            e_pDialog.setProgress(100);
            e_pDialog.setIndeterminate(false);
            e_pDialog.setCancelable(true);
            e_pDialog.show();
            new Thread(() -> {
                try {
                    while (e_count <= 100) {
                        e_pDialog.setProgress(e_count++);
                        Thread.sleep(80);
                    }
                    Message message = new Message();
                    message.what = 0;
                    mHandler.sendMessage(message);
                } catch (InterruptedException e) {
                    e_pDialog.cancel();
                }
            }).start();
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

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            if (msg.what == 0) {
                e_pDialog.dismiss();
                Intent intent = new Intent(ExamActivity.this, CoreActivity.class);
                startActivity(intent);
                finish();
            }
        }
    };

}
