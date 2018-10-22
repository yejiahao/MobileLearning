package cn.yejh.mobilelearning.activity;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.*;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import cn.yejh.mobilelearning.global.GlobalValue;
import cn.yejh.mobilelearning.R;

public class GradeActivity extends Activity {
    TextView nameGrade, numGrade, comment, comment2;
    ImageButton btnBackGrade;
    Button btnHistory, btnAllHistory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.grade);
        nameGrade = findViewById(R.id.nameGrade);
        numGrade = findViewById(R.id.numGrade);
        comment = findViewById(R.id.comment);
        comment2 = findViewById(R.id.comment2);

        btnBackGrade = findViewById(R.id.btnBackGrade);
        btnHistory = findViewById(R.id.btnHistory);
        btnAllHistory = findViewById(R.id.btnAllHistory);

        nameGrade.setText(GlobalValue.name + " 同学, 你的考试成绩为：");
        numGrade.setText(GlobalValue.score + " 分");
        comment.setText("用时 " + (GlobalValue.eTime - GlobalValue.sTime) / 1000 + " 秒");// 显示考试用时
        if (GlobalValue.score >= 90) {
            comment2.setText("Good!\t\to(∩_∩)o");
        } else if (GlobalValue.score >= 75) {
            comment2.setText("Better!\t\t(*^__^*)");
        } else if (GlobalValue.score >= 60) {
            comment2.setText("OK!\t\t(┬＿┬)↘");
        } else {
            comment2.setText("Bad!\t\to(︶︿︶)o");
        }

        btnBackGrade.setOnClickListener(v -> finish());

        btnHistory.setOnClickListener(new ClickEvent());
        btnAllHistory.setOnClickListener(new ClickEvent());
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
                case R.id.btnHistory:
                    startActivity(new Intent(GradeActivity.this, HistoryActivity.class));// 跳转到个人历史成绩
                    break;
                case R.id.btnAllHistory:
                    startActivity(new Intent(GradeActivity.this, AllHistoryActivity.class));// 跳转到所有人的成绩
                    break;
            }
        }
    }
}
