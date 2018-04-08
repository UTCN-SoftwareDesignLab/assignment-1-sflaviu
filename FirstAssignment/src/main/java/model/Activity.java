package model;

import java.sql.Date;

public class Activity {
    private Long id;
    private Long performerId;
    private Date date;
    private String type;
    private Long modifiedClientId;
    private Long modifiedAccountId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPerformerId() {
        return performerId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getModifiedClientId() {
        return modifiedClientId;
    }

    public Long getModifiedAccountId() {
        return modifiedAccountId;
    }

    public void setPerformerId(Long performerId) {
        this.performerId = performerId;
    }

    public void setModifiedClientId(Long modifiedClientId) {
        this.modifiedClientId = modifiedClientId;
    }

    public void setModifiedAccountId(Long modifiedAccountId) {
        this.modifiedAccountId = modifiedAccountId;
    }

    public String toString()
    {
        String activityString=" ";
        activityString=activityString.concat("Activity type: "+getType());
        activityString=activityString.concat("\nDate: "+getDate().toString());
        activityString=activityString.concat("\nModified account: "+getModifiedAccountId());
        activityString=activityString.concat("\nModified client: "+getModifiedClientId());
        return activityString;
    }
}
