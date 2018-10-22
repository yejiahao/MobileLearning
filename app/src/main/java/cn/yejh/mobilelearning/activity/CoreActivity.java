package cn.yejh.mobilelearning.activity;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import cn.yejh.mobilelearning.R;
import cn.yejh.mobilelearning.db.DBManager;
import cn.yejh.mobilelearning.db.Grade;
import cn.yejh.mobilelearning.global.GlobalValue;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;
import java.util.Random;

public class CoreActivity extends Activity {
    TextView tvCoreQuestion, tvCoreExplain;// 问题和解释
    TextView tvCoreNum;// 数字标题
    Button btnCoreA, btnCoreB, btnCoreC, btnCoreD;// ABCD选项
    Button btnPre, btnSubmit, btnNext;// 上一题、交卷、下一题
    static int index = 1;// 题目号码
    static int count, n;// 试卷题目总数、随机数
    InputStream mInputStream;
    Workbook wb;
    Sheet mSheet;
    DBManager dbm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.core);
        dbm = new DBManager(this);// 启动数据库
        GlobalValue.sTime = new Date().getTime();
        tvCoreQuestion = findViewById(R.id.tvCoreQuestion);
        tvCoreExplain = findViewById(R.id.tvCoreExplain);
        tvCoreNum = findViewById(R.id.tvCoreNum);
        btnCoreA = findViewById(R.id.btnCoreA);
        btnCoreB = findViewById(R.id.btnCoreB);
        btnCoreC = findViewById(R.id.btnCoreC);
        btnCoreD = findViewById(R.id.btnCoreD);

        btnPre = findViewById(R.id.btnPre);
        btnSubmit = findViewById(R.id.btnSubmit);
        btnNext = findViewById(R.id.btnNext);
        ReadExcel("JavaExam.xls");// 读试卷
        index = 1;
        show(index);// 展示题目

        btnCoreA.setOnClickListener(new ClickEvent());
        btnCoreB.setOnClickListener(new ClickEvent());
        btnCoreC.setOnClickListener(new ClickEvent());
        btnCoreD.setOnClickListener(new ClickEvent());

        btnNext.setOnClickListener(new ClickEvent2());
        btnPre.setOnClickListener(new ClickEvent2());
        btnSubmit.setOnClickListener(new ClickEvent2());
    }

    /**
     * A placeholder fragment containing a simple view.
     */
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
        String tigan, ca, cb, cc, cd, correct;
        String choose = "";
    }

    private ArrayList<CurCell> mArrayList = new ArrayList<>();

    private void ReadExcel(String fileName) {
        try {
            mInputStream = getResources().getAssets().open(fileName);
            wb = Workbook.getWorkbook(mInputStream);
            Random random = new Random();
            n = random.nextInt(3);
            mSheet = wb.getSheet(n);
            switch (n) {
                case 0:
                    tvCoreNum.setTextColor(Color.RED);
                    break;
                case 1:
                    tvCoreNum.setTextColor(Color.GREEN);
                    break;
                case 2:
                    tvCoreNum.setTextColor(Color.BLUE);
                    break;
            }
            int row = mSheet.getRows();
            count = row;

            for (int i = 1, j = 0; i < row; i++) {
                CurCell mCell = new CurCell();
                mCell.tigan = mSheet.getCell(j, i).getContents();
                mCell.ca = mSheet.getCell((j + 1), i).getContents();
                mCell.cb = mSheet.getCell((j + 2), i).getContents();
                mCell.cc = mSheet.getCell((j + 3), i).getContents();
                mCell.cd = mSheet.getCell((j + 4), i).getContents();
                mCell.correct = mSheet.getCell((j + 5), i).getContents();
                mArrayList.add(mCell);
            }

        } catch (BiffException | IndexOutOfBoundsException | IOException e) {
            e.printStackTrace();
        }
    }

    private void show(int index) {
        tvCoreNum.setText(index + " / " + String.valueOf(count - 1) + " 题");
        btnPre.setVisibility(View.VISIBLE);
        btnNext.setVisibility(View.VISIBLE);
        int a = index - 1;
        tvCoreQuestion.setText(index + ". " + mArrayList.get(a).tigan);
        btnCoreA.setText("A.  " + mArrayList.get(a).ca);
        btnCoreB.setText("B.  " + mArrayList.get(a).cb);
        btnCoreC.setText("C.  " + mArrayList.get(a).cc);
        btnCoreD.setText("D.  " + mArrayList.get(a).cd);
        tvCoreExplain.setText("你选择的是： " + mArrayList.get(index - 1).choose);

        if (index == 1) {
            btnPre.setVisibility(View.INVISIBLE);
            showToast("已经是第一题");
        } else if (index == count - 1) {
            btnNext.setVisibility(View.INVISIBLE);
            showToast("已经是最后一题");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            wb.close();
            mInputStream.close();
            dbm.closeDB();// 关闭数据库
        } catch (IOException e) {

            e.printStackTrace();
        }
    }

    public void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();// Toast提示
    }

    // 显示对话框
    protected void dialog() {
        Builder builder = new Builder(CoreActivity.this);
        int c = 0;
        for (int i = 0; i < mArrayList.size(); i++)
            if ("".equals(mArrayList.get(i).choose))
                c++;
        builder.setMessage("确认交卷吗？还有 " + c + " 题未完成");
        builder.setIcon(android.R.drawable.ic_dialog_info);
        builder.setTitle("提示");
        builder.setPositiveButton("确定", (dialog, which) -> {
            // 点击“确认”后的操作
            Grade grade = new Grade(GlobalValue.name, GlobalValue.score);
            dbm.addGrade(grade);// 数据库插入操作
            startActivity(new Intent(CoreActivity.this, GradeActivity.class));
            CoreActivity.this.finish();
        });
        builder.setNegativeButton("取消", (dialog, which) -> {
            // 点击“取消”后的操作
        });
        builder.create().show();
    }

    class ClickEvent implements View.OnClickListener {
        int a;

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btnCoreA:
                    a = index - 1;
                    mArrayList.get(a).choose = "A";
                    tvCoreExplain.setText("你选择的是： " + mArrayList.get(a).choose);
                    break;
                case R.id.btnCoreB:
                    a = index - 1;
                    mArrayList.get(a).choose = "B";
                    tvCoreExplain.setText("你选择的是： " + mArrayList.get(a).choose);
                    break;
                case R.id.btnCoreC:
                    a = index - 1;
                    mArrayList.get(a).choose = "C";
                    tvCoreExplain.setText("你选择的是： " + mArrayList.get(a).choose);
                    break;
                case R.id.btnCoreD:
                    a = index - 1;
                    mArrayList.get(a).choose = "D";
                    tvCoreExplain.setText("你选择的是： " + mArrayList.get(a).choose);
                    break;
            }
        }
    }

    class ClickEvent2 implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btnPre:
                    show(--index);
                    break;
                case R.id.btnSubmit:
                    GlobalValue.eTime = new Date().getTime();
                    GlobalValue.score = 0;
                    for (int i = 0; i < count - 1; i++) {
                        if (Objects.equals(mArrayList.get(i).choose, mArrayList.get(i).correct)) {
                            GlobalValue.score += 100 / (count - 1);
                        }
                    }
                    dialog();
                    break;
                case R.id.btnNext:
                    show(++index);
                    break;
            }
        }
    }
}
