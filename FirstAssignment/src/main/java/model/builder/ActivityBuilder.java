package model.builder;

import model.Account;
import model.Activity;
import model.Client;
import model.User;

import java.sql.Date;

public class ActivityBuilder implements Builder<Activity>{

    private Activity activity;

    public ActivityBuilder() {
        activity=new Activity();
    }

    public ActivityBuilder setId(Long id) {
        activity.setId(id);
        return this;
    }

    public ActivityBuilder setPerformer(Long performerId){
        activity.setPerformerId(performerId);
        return this;
    }

    public ActivityBuilder setDate(Date date) {
        activity.setDate(date);
        return this;
    }

    public ActivityBuilder setType(String type) {
        activity.setType(type);
        return this;
    }

    public ActivityBuilder setModifiedClientId(Long modifiedId) {
        activity.setModifiedClientId(modifiedId);
        return this;
    }

    public ActivityBuilder setModifiedAccountId(Long modifiedId) {
        activity.setModifiedAccountId(modifiedId);
        return this;
    }

    public Activity build() { return activity; }
}
