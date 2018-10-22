package cn.yejh.mobilelearning.activity;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.*;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import cn.yejh.mobilelearning.db.DBManager;
import cn.yejh.mobilelearning.db.Grade;
import cn.yejh.mobilelearning.R;

import java.util.ArrayList;
import java.util.List;

public class AllHistoryActivity extends Activity {
    TextView tvHistory, tvColumnsName;
    ListView lvpersonal;

    DBManager dbm;
    List<Grade> list;
    List<String> strings;
    String data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gradehistory);
        dbm = new DBManager(this);// 启动数据库

        tvHistory = findViewById(R.id.tvHistory);
        tvColumnsName = findViewById(R.id.tvColumnsName);
        lvpersonal = findViewById(R.id.lv01);
        list = dbm.queryGrade();
        strings = new ArrayList<>();
        tvHistory.setText("所有成绩");
        for (int i = 0; i < list.size(); i++) {
            data = " " + list.get(i).getGid() + "\t    "
                    + list.get(i).getUsername() + "\t  "
                    + list.get(i).getTime() + "\t " + list.get(i).getScore();
            strings.add(data);
        }
        // 准备数据
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.list_item_1, strings);
        lvpersonal.setAdapter(adapter);// 设置适配器
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
}
