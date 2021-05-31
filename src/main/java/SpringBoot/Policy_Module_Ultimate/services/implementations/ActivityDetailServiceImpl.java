package SpringBoot.Policy_Module_Ultimate.services.implementations;

import SpringBoot.Policy_Module_Ultimate.models.Activity;
import SpringBoot.Policy_Module_Ultimate.models.ActivityDetail;
import SpringBoot.Policy_Module_Ultimate.models.Policy;
import SpringBoot.Policy_Module_Ultimate.repositories.ActivityDetailRepository;
import SpringBoot.Policy_Module_Ultimate.services.ActivityDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class ActivityDetailServiceImpl implements ActivityDetailService {
    
    @Autowired
    private ActivityDetailRepository activityDetailRepository;
    
    @Override
    public void saveActivityDetail(ActivityDetail activityDetail) {
        this.activityDetailRepository.save(activityDetail);
    }

    @Override
    public Page<ActivityDetail> findPaginatedByActivity(Activity activity, Integer pageNo, Integer pageSize, String sortField, String sortDirection) {
        Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending() :
                Sort.by(sortField).descending();
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize, sort);
        return this.activityDetailRepository.findAllByActivity(activity, pageable);
    }
}
