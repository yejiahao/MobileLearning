package cn.yejh.mobilelearning.activity;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import cn.yejh.mobilelearning.R;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CorePracticeActivity extends Activity {
    TextView tvCorePracticeQuestion, tvCorePracticeExplain;// 问题和解释
    TextView tvCorePracticeNum;// 数字标题
    Button btnCorePracticeA, btnCorePracticeB, btnCorePracticeC,
            btnCorePracticeD;// ABCD选项
    Button btnPracticePre, btnPracticeReturn, btnPracticeNext;// 上一题、交卷、下一题
    static int index = 1;// 题目号码
    int count;// 试卷题目总数、随机数
    int selectNum, countNum;
    Intent intent;
    InputStream mInputStream;
    Workbook wb;
    Sheet mSheet;
    int a;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.corepractice);
        tvCorePracticeQuestion = findViewById(R.id.tvCorePracticeQuestion);
        tvCorePracticeExplain = findViewById(R.id.tvCorePracticeExplain);
        tvCorePracticeNum = findViewById(R.id.tvCorePracticeNum);
        btnCorePracticeA = findViewById(R.id.btnCorePracticeA);
        btnCorePracticeB = findViewById(R.id.btnCorePracticeB);
        btnCorePracticeC = findViewById(R.id.btnCorePracticeC);
        btnCorePracticeD = findViewById(R.id.btnCorePracticeD);

        btnPracticePre = findViewById(R.id.btnPracticePre);
        btnPracticeReturn = findViewById(R.id.btnPracticeReturn);
        btnPracticeNext = findViewById(R.id.btnPracticeNext);
        btnPracticeReturn.setVisibility(View.INVISIBLE);
        // 拿到PracticeActivity传来的Bundle
        intent = getIntent();
        selectNum = intent.getIntExtra("extraSelectNum", 0);
        countNum = intent.getIntExtra("extraCountNum", 0);

        ReadExcel("JavaExam.xls");// 读试卷
        index = 1;
        show(index);// 展示题目
        View.OnClickListener clickEvent = new ClickEvent();
        btnCorePracticeA.setOnClickListener(clickEvent);
        btnCorePracticeB.setOnClickListener(clickEvent);
        btnCorePracticeC.setOnClickListener(clickEvent);
        btnCorePracticeD.setOnClickListener(clickEvent);

        View.OnClickListener clickEvent2 = new ClickEvent2();
        btnPracticePre.setOnClickListener(clickEvent2);
        btnPracticeNext.setOnClickListener(clickEvent2);
    }

    // 判断选择是否正确
    public void judge(int i) {
        if (Objects.equals(mArrayList.get(i).choose, mArrayList.get(i).correct)) {
            tvCorePracticeExplain.append("\t\t\t\t正确！");
        } else {
            tvCorePracticeExplain.append("\t\t\t\t错误！");
            tvCorePracticeExplain.append("\n答案解析： " + mArrayList.get(a).explain);
        }
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

    private class CurCell {
        String stem, ca, cb, cc, cd, correct, chapter, explain;
        String choose = "";
    }

    private List<CurCell> mArrayList = new ArrayList<>();

    // 读Excel文件
    private void ReadExcel(String fileName) {
        try {
            mInputStream = getResources().getAssets().open(fileName);
            wb = Workbook.getWorkbook(mInputStream);
            mSheet = wb.getSheet(3);
            int row = mSheet.getRows();// 76行8列
            count = row;

            for (int i = 1, j = 0; i < row; i++) {
                CurCell mCell = new CurCell();
                mCell.stem = mSheet.getCell(j, i).getContents();
                mCell.ca = mSheet.getCell((j + 1), i).getContents();
                mCell.cb = mSheet.getCell((j + 2), i).getContents();
                mCell.cc = mSheet.getCell((j + 3), i).getContents();
                mCell.cd = mSheet.getCell((j + 4), i).getContents();
                mCell.correct = mSheet.getCell((j + 5), i).getContents();
                mCell.chapter = mSheet.getCell((j + 6), i).getContents();
                mCell.explain = mSheet.getCell((j + 7), i).getContents();
                mArrayList.add(mCell);
            }
        } catch (BiffException | IndexOutOfBoundsException | IOException e) {
            e.printStackTrace();
        }
    }

    // 逐页展示题目
    private void show(int index) {
        tvCorePracticeNum.setText("第 " + (selectNum + 1) + " 章");
        btnPracticePre.setVisibility(View.VISIBLE);
        btnPracticeNext.setVisibility(View.VISIBLE);
        a = (count - 1) / countNum * selectNum + index - 1;
        tvCorePracticeQuestion.setText(index + ". " + mArrayList.get(a).stem);
        btnCorePracticeA.setText("A.  " + mArrayList.get(a).ca);
        btnCorePracticeB.setText("B.  " + mArrayList.get(a).cb);
        btnCorePracticeC.setText("C.  " + mArrayList.get(a).cc);
        btnCorePracticeD.setText("D.  " + mArrayList.get(a).cd);
        tvCorePracticeExplain.setText("");

        if (index == 1) {
            btnPracticePre.setVisibility(View.INVISIBLE);
            showToast("已经是第一题");
        } else if (index == (count - 1) / countNum) {
            btnPracticeNext.setVisibility(View.INVISIBLE);
            showToast("已经是最后一题");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            wb.close();
            mInputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    class ClickEvent implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btnCorePracticeA:
                    a = (count - 1) / countNum * selectNum + index - 1;
                    mArrayList.get(a).choose = "A";
                    tvCorePracticeExplain.setText("你选择的是： " + mArrayList.get(a).choose);
                    judge(a);
                    break;
                case R.id.btnCorePracticeB:
                    a = (count - 1) / countNum * selectNum + index - 1;
                    mArrayList.get(a).choose = "B";
                    tvCorePracticeExplain.setText("你选择的是： " + mArrayList.get(a).choose);
                    judge(a);
                    break;
                case R.id.btnCorePracticeC:
                    a = (count - 1) / countNum * selectNum + index - 1;
                    mArrayList.get(a).choose = "C";
                    tvCorePracticeExplain.setText("你选择的是： " + mArrayList.get(a).choose);
                    judge(a);
                    break;
                case R.id.btnCorePracticeD:
                    a = (count - 1) / countNum * selectNum + index - 1;
                    mArrayList.get(a).choose = "D";
                    tvCorePracticeExplain.setText("你选择的是： " + mArrayList.get(a).choose);
                    judge(a);
                    break;
            }
        }
    }

    class ClickEvent2 implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btnPracticePre:
                    show(--index);
                    break;
                case R.id.btnPracticeNext:
                    show(++index);
                    break;
            }
        }
    }
}
