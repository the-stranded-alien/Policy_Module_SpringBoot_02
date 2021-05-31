package SpringBoot.Policy_Module_Ultimate.services;

import SpringBoot.Policy_Module_Ultimate.models.Action;
import SpringBoot.Policy_Module_Ultimate.models.Activity;

import java.time.LocalDateTime;
import java.util.List;

public interface ActionService {
    void saveAction(Action action);
    Action getActionByActivity(Activity activity);
    List<Action> getActionsByStatusAndTime(Boolean status, LocalDateTime startTime, LocalDateTime endTime);
    void updateActionStatusById(Long id);
}
