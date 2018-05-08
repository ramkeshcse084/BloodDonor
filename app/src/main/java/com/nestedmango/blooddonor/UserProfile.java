package com.nestedmango.blooddonor;

public class UserProfile {
    String userName, userCity,userBloodgroup,userAge,userMobileNo,userId,userEmailId , userCondition;

        public UserProfile()
        {

        }

    public UserProfile(String userAge, String userBloodgroup, String userCity, String userId, String userMobileNo, String userName, String userEmailId , String userCondition) {
        this.userName = userName;
        this.userCity = userCity;
        this.userBloodgroup = userBloodgroup;
        this.userAge = userAge;
        this.userMobileNo = userMobileNo;
        this.userId = userId;
        this.userEmailId = userEmailId;
        this.userCondition = userCondition;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserCity() {
        return userCity;
    }

    public String getUserBloodgroup() {
        return userBloodgroup;
    }

    public String getUserAge() {
        return userAge;
    }

    public String getUserMobileNo() {
        return userMobileNo;
    }

    public String getUserId() {
        return userId;
    }

    public String getUserEmailId() {
        return userEmailId;
    }
    //test
    public String getUserCondition()
    {
        return userCondition;

    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setUserCity(String userCity) {
        this.userCity = userCity;
    }

    public void setUserBloodgroup(String userBloodgroup) {
        this.userBloodgroup = userBloodgroup;
    }

    public void setUserAge(String userAge) {
        this.userAge = userAge;
    }

    public void setUserMobileNo(String userMobileNo) {
        this.userMobileNo = userMobileNo;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setUserEmailId(String userEmailId) {
        this.userEmailId = userEmailId;
    }
    public void setUserCondition(String userCondition){
            this.userCondition = userCondition;
    }
}
