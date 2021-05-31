package SpringBoot.Policy_Module_Ultimate.repositories;

import SpringBoot.Policy_Module_Ultimate.models.Policy;
import SpringBoot.Policy_Module_Ultimate.models.Risk;
import SpringBoot.Policy_Module_Ultimate.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Set;

public interface RiskRepository extends JpaRepository<Risk, Long> {
    List<Risk> findAllByCreator(User user);
    Page<Risk> findAllByCreator(User user, Pageable pageable);
    Page<Risk> findAllByCreatorAndStatus(User user, Boolean status, Pageable pageable);
    List<Risk> findAllByCreatorAndStatus(User user, Boolean status);
    Set<Risk> findAllByCreatorAndStatusAndPoliciesIncludedInContaining(User user, Boolean status, Policy policy);
}
