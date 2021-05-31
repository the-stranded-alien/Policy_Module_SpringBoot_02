package SpringBoot.Policy_Module_Ultimate.services.implementations;

import SpringBoot.Policy_Module_Ultimate.models.Action;
import SpringBoot.Policy_Module_Ultimate.models.Activity;
import SpringBoot.Policy_Module_Ultimate.repositories.ActionRepository;
import SpringBoot.Policy_Module_Ultimate.services.ActionService;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ActionServiceImpl implements ActionService {

    @Autowired
    private ActionRepository actionRepository;

    @Override
    public void saveAction(Action action) {
        this.actionRepository.save(action);
    }

    @Override
    public Action getActionByActivity(Activity activity) {
        return this.actionRepository.findByActivity(activity);
    }

    @Override
    public List<Action> getActionsByStatusAndTime(Boolean status, LocalDateTime startTime, LocalDateTime endTime) {
        return this.actionRepository.findAllByStatusAndActionTimeBetween(status, startTime, endTime);
    }

    @Override
    public void updateActionStatusById(Long id) {
        Optional<Action> optional = this.actionRepository.findById(id);
        Action currentAction = null;
        if(optional.isPresent()) {
            currentAction = optional.get();
        } else {
            throw new RuntimeException("Action With This Id Not Found !");
        }
        currentAction.setStatus(true);
        this.actionRepository.save(currentAction);
    }
}
