package service.activity;

import model.Activity;
import model.User;
import repository.EntityNotFoundException;
import repository.activity.ActivityRepository;
import service.activity.ActivityService;

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
    public Activity findById(Long id) throws EntityNotFoundException {
        return activityRepository.findById(id);
    }

    @Override
    public boolean save(Activity activity) {
        return activityRepository.save(activity);
    }

    @Override
    public List<Activity> findByPerformer(User performer) {
        return activityRepository.findByPerformerId(performer.getId());
    }
}
