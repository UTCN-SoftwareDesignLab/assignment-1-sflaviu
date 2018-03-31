package repository.activity;

import model.Activity;

import java.util.List;

public interface ActivityRepository {

    Activity findById(Long id);

    List<Activity> findByPerformerId(int userId);

    boolean save(Activity activity);

    void removeAll();

}
