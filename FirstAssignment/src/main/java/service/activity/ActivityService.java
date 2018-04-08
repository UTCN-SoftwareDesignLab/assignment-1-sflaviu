package service.activity;

import model.Activity;
import model.User;
import model.validation.Notification;
import repository.EntityNotFoundException;

import java.sql.Date;
import java.util.List;

public interface ActivityService {

    List<Activity> findAll();

    Notification<Boolean> save(String type, Long userId, Date date,Long clientId,Long accountId);

    List<Activity> findByPerformer(Long ownerId);

    void removeAll();
}