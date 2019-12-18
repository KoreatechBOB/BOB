package com.example.bob.ui.Rating;

public class RatingDTO {

    String RoomName;
    String UserName;
    String Score;
    String Message;

    public RatingDTO() {}
    public RatingDTO(String RoomName, String UserName, String Score, String Message) {
        this.RoomName = RoomName;
        this.UserName = UserName;
        this.Score = Score;
        this.Message = Message;
    }

    public String getRoomName() {
        return RoomName;
    }

    public void setRoomName(String RoomName) {
        this.RoomName = RoomName;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String UserName) {
        this.UserName = UserName;
    }

    public String getScore() {
        return Score;
    }

    public void setScore(String Score) {
        this.Score = Score;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String Message) {
        this.Message = Message;
    }
}