package cn.yejh.mobilelearning.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DBManager {
    private MyOpenHelper helper;
    private SQLiteDatabase db;

    // 启动数据库
    public DBManager(Context context) {
        helper = new MyOpenHelper(context);
        db = helper.getWritableDatabase();
    }

    // 添加用户
    public void addUser(User user) {
        db.beginTransaction();
        try {
            db.execSQL("INSERT INTO User VALUES(NULL,?,?)", new Object[]{user.getUsername(), user.getPassword()});
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }

    // 添加成绩
    public void addGrade(Grade grade) {
        DateFormat df = new SimpleDateFormat("yy-MM-dd HH:mm");
        String time = df.format(new Date());
        db.beginTransaction();
        try {
            db.execSQL("INSERT INTO Grade VALUES(NULL,?,?,?)", new Object[]{grade.getUsername(), time, grade.getScore()});
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }

    // 查询用户表
    public List<User> queryUser() {
        List<User> users = new ArrayList<>();
        Cursor c = db.rawQuery("SELECT * FROM USER", null);
        while (c.moveToNext()) {
            User user = new User();
            user.setUid(c.getInt(c.getColumnIndex("uid")));
            user.setUsername(c.getString(c.getColumnIndex("username")));
            user.setPassword(c.getString(c.getColumnIndex("password")));
            users.add(user);
        }
        c.close();
        return users;
    }

    // 查询成绩表
    public List<Grade> queryGrade() {
        List<Grade> grades = new ArrayList<>();
        Cursor c = db.rawQuery("SELECT * FROM GRADE", null);
        while (c.moveToNext()) {
            Grade grade = new Grade();
            grade.setGid(c.getInt(c.getColumnIndex("gid")));
            grade.setUsername(c.getString(c.getColumnIndex("username")));
            grade.setTime(c.getString(c.getColumnIndex("time")));
            grade.setScore(c.getInt(c.getColumnIndex("score")));
            grades.add(grade);
        }
        c.close();
        return grades;
    }

    // 查询一个用户的所有成绩
    public List<Grade> queryGradeOneMan(String s) {
        List<Grade> grades = new ArrayList<>();
        Cursor c = db.rawQuery("SELECT * FROM GRADE WHERE username= '" + s + "'", null);
        while (c.moveToNext()) {
            Grade grade = new Grade();
            grade.setGid(c.getInt(c.getColumnIndex("gid")));
            grade.setUsername(c.getString(c.getColumnIndex("username")));
            grade.setTime(c.getString(c.getColumnIndex("time")));
            grade.setScore(c.getInt(c.getColumnIndex("score")));
            grades.add(grade);
        }
        c.close();
        return grades;
    }

    // 关闭数据库
    public void closeDB() {
        db.close();
    }

    // 检验是否有相同的用户名在用户表中
    public boolean registerUser(User user) {
        String sqlReg = "SELECT * FROM USER WHERE username = '" + user.getUsername() + "'";
        try (Cursor c = db.rawQuery(sqlReg, null)) {
            while (c.moveToNext()) {
                return true;
            }
            return false;
        }
    }

    // 检验用户名和密码是否在用户表中
    public boolean validateUser(User user) {
        String sqlLogin = "SELECT * FROM USER WHERE username = '" + user.getUsername() + "'" + " AND password = '" + user.getPassword() + "'";
        try (Cursor c = db.rawQuery(sqlLogin, null)) {
            while (c.moveToNext()) {
                return true;
            }
            return false;
        }
    }
}
