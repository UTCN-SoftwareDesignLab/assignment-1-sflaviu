package service.activity;

import model.Activity;
import model.User;
import repository.EntityNotFoundException;

import java.util.List;

public interface ActivityService {

    List<Activity> findAll();

    Activity findById(Long id) throws EntityNotFoundException;

    boolean save(Activity activity);

    List<Activity> findByPerformer(User performer);
}