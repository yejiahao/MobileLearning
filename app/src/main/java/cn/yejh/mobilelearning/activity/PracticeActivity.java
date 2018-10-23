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
import android.widget.*;
import cn.yejh.mobilelearning.global.GlobalValue;
import cn.yejh.mobilelearning.R;

public class PracticeActivity extends Activity {
    int selectNum, countNum;
    TextView tvName, text;
    ImageButton btnBackPractice;
    Button btnStartPractice;
    public Spinner spinner;

    int p_count = 0;
    ProgressDialog p_pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.practice);
        tvName = findViewById(R.id.namePractice);
        text = findViewById(R.id.chapter_show);
        spinner = findViewById(R.id.spinner_chapter);
        btnBackPractice = findViewById(R.id.btnBackPractice);
        btnStartPractice = findViewById(R.id.btnStartPractice);
        // 定义适配器
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.chapters, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);// 设置适配器
        spinner.setPrompt("请选择练习章节： ");
        spinner.setSelection(0, true);
        selectNum = spinner.getSelectedItemPosition();
        countNum = spinner.getCount();
        spinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                text.setText("你选择的章节是： " + arg0.getItemAtPosition(arg2).toString());
                arg0.setVisibility(View.VISIBLE);
                selectNum = spinner.getSelectedItemPosition();
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }

        });
        tvName.setText(GlobalValue.name + " 同学, 你好");// 取得登录的用户名
        btnStartPractice.setOnClickListener(v -> {
            p_count = 0;
            p_pDialog = new ProgressDialog(PracticeActivity.this);
            p_pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            p_pDialog.setTitle("loading...");
            p_pDialog.setMessage("正在加载试题，请稍后...");
            p_pDialog.setProgress(100);
            p_pDialog.setIndeterminate(false);
            p_pDialog.setCancelable(true);
            p_pDialog.show();
            new Thread(() -> {
                try {
                    while (p_count <= 100) {
                        p_pDialog.setProgress(p_count++);
                        Thread.sleep(80);
                    }
                    Message message = new Message();
                    message.what = 0;
                    mHandler.sendMessage(message);
                } catch (InterruptedException e) {
                    p_pDialog.cancel();
                }
            }).start();
        });
        btnBackPractice.setOnClickListener(v -> finish());
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
                p_pDialog.dismiss();
                Intent intent = new Intent();
                intent.setClass(PracticeActivity.this, CorePracticeActivity.class);
                intent.putExtra("extraSelectNum", selectNum);// intent传Bundle
                intent.putExtra("extraCountNum", countNum);// intent传Bundle
                startActivity(intent);
            }
        }
    };
}
