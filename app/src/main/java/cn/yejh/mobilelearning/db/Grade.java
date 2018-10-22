package cn.yejh.mobilelearning.db;

public class Grade {
    public int gid;// 成绩编号
    public String username;// 用户名
    public String time;// 提交成绩时间
    public int score;// 考试分数

    public Grade() {
    }

    public Grade(String username, int score) {
        this.username = username;
        this.score = score;
    }

    public int getGid() {
        return gid;
    }

    public void setGid(int gid) {
        this.gid = gid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    @Override
    public String toString() {
        return "Grade{" +
                "gid=" + gid +
                ", username='" + username + '\'' +
                ", time='" + time + '\'' +
                ", score=" + score +
                '}';
    }
}
