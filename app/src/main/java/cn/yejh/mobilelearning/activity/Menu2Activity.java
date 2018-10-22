package cn.yejh.mobilelearning.activity;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import cn.yejh.mobilelearning.R;

public class Menu2Activity extends Activity {
    Button btnPractice, btnExam;// 练习模式、模拟考试 按钮

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu2);
        btnPractice = findViewById(R.id.btnPractice);
        btnExam = findViewById(R.id.btnExam);

        btnExam.setOnClickListener(new ClickEvent());
        btnPractice.setOnClickListener(new ClickEvent());
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
                case R.id.btnPractice:
                    startActivity(new Intent(Menu2Activity.this, PracticeActivity.class));// 进入练习模式
                    break;
                case R.id.btnExam:
                    startActivity(new Intent(Menu2Activity.this, ExamActivity.class));// 进入模拟考试
                    break;
            }
        }
    }
}
