package com.example.bob.ui.ChatList;

public class ChatRoomDTO {

    String name;
    String place;
    String hour, min;
    String ageStart, ageEnd;
    String menu;
    int year, month, day;

    public ChatRoomDTO() {}
    public ChatRoomDTO(String name, String place, String hour, String min, String ageStart, String ageEnd, String menu, int year, int month, int day) {
        this.name = name;
        this.place = place;
        this.hour = hour;
        this.min = min;
        this.ageStart = ageStart;
        this.ageEnd = ageEnd;
        this.menu = menu;
        this.year = year;
        this.month = month;
        this.day = day;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getAgeStart() {
        return ageStart;
    }

    public void setAgeStart(String ageStart) {
        this.ageStart = ageStart;
    }

    public String getAgeEnd() {
        return ageEnd;
    }

    public void setAgeEnd(String ageEnd) {
        this.ageEnd = ageEnd;
    }

    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }

    public String getMin() {
        return min;
    }

    public void setMin(String min) {
        this.min = min;
    }

    public String getMenu() {
        return menu;
    }

    public void setMenu(String menu) {
        this.menu = menu;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

}
