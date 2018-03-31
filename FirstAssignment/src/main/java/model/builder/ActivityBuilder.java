package model.builder;

import model.Account;
import model.Activity;
import model.Client;
import model.User;

import java.sql.Date;

public class ActivityBuilder {

    private Activity activity;

    public ActivityBuilder() {
        activity=new Activity();
    }

    public ActivityBuilder setId(Long id) {
        activity.setId(id);
        return this;
    }

    public ActivityBuilder setPerformer(User performer){
        activity.setPerformer(performer);
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

    public ActivityBuilder setModifiedClient(Client modified) {
        activity.setModifiedClient(modified);
        return this;
    }

    public ActivityBuilder setModifiedAccount(Account account) {
        activity.setModifiedAccount(account);
        return this;
    }

    public Activity build() { return activity; }
}