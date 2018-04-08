package service.activity;

import model.Account;
import model.Activity;
import model.Client;
import model.User;
import model.builder.AccountBuilder;
import model.builder.ActivityBuilder;
import model.validation.ActivityValidator;
import model.validation.Notification;
import model.validation.Validator;
import repository.EntityNotFoundException;
import repository.activity.ActivityRepository;
import service.activity.ActivityService;

import java.sql.Date;
import java.util.List;

public class ActivityServiceImpl implements ActivityService {

    private final ActivityRepository activityRepository;

    public ActivityServiceImpl(ActivityRepository activityRepository) {
        this.activityRepository = activityRepository;
    }

    @Override
    public List<Activity> findAll() {
        return activityRepository.findAll();
    }


    @Override
    public Notification<Boolean> save(String type, User user, Date date,Long clientId,Long accountId)
    {
        Activity activity=new ActivityBuilder().setType(type).setPerformer(clientId).setDate(date).build();

        Validator activityValidator=new ActivityValidator(activity);

        boolean activityValid = activityValidator.validate();
        Notification<Boolean> activitySavingNotification = new Notification<>();

        if (!activityValid) {
            activityValidator.getErrors().forEach(activitySavingNotification::addError);
            activitySavingNotification.setResult(Boolean.FALSE);
        } else {
            activitySavingNotification.setResult(activityRepository.save(activity));
        }
        return activitySavingNotification;
    }

    @Override
    public List<Activity> findByPerformer(User performer) {
        return activityRepository.findByPerformerId(performer.getId());
    }
}
