package SpringBoot.Policy_Module_Ultimate.services;

import SpringBoot.Policy_Module_Ultimate.models.Activity;
import SpringBoot.Policy_Module_Ultimate.models.ActivityDetail;
import org.springframework.data.domain.Page;

public interface ActivityDetailService {
    void saveActivityDetail(ActivityDetail activityDetail);
    Page<ActivityDetail> findPaginatedByActivity(Activity activity,Integer pageNo, Integer pageSize, String sortField, String sortDirection);
}
