package com.example.bob;

public class RegisterDTO {

    String id;
    String pass;
    String name;
    String nick;
    String year;
    String month;
    String day;
    String sex;
    String comment;
    int point;
    int num;

    public RegisterDTO() {}
    public RegisterDTO(String id, String pass, String name, String nick, String year, String month, String day, String sex, String comment, int point, int num) {
        this.id = id;
        this.pass = pass;
        this.name = name;
        this.nick = nick;
        this.year = year;
        this.month = month;
        this.day = day;
        this.sex = sex;
        this.comment = comment;
        this.point = point;
        this.num = num;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getPoint() {
        return point;
    }

    public void setPoint(int point) {
        this.point = point;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }
}
