package SpringBoot.Policy_Module_Ultimate.repositories;

import SpringBoot.Policy_Module_Ultimate.models.Action;
import SpringBoot.Policy_Module_Ultimate.models.Activity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface ActionRepository extends JpaRepository<Action, Long> {
    Action findByActivity(Activity activity);
    List<Action> findAllByStatusOrderByActionTimeAsc(Boolean status);
    List<Action> findAllByStatusAndActionTimeBetween(Boolean status, LocalDateTime actionStartTime, LocalDateTime actionEndTime);
    List<Action> findActionsByActionTimeBetweenAndStatus(LocalDateTime actionStartTime, LocalDateTime actionEndTime, Boolean status);
}
