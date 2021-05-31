package SpringBoot.Policy_Module_Ultimate.controllers;

import SpringBoot.Policy_Module_Ultimate.models.Activity;
import SpringBoot.Policy_Module_Ultimate.models.ActivityDetail;
import SpringBoot.Policy_Module_Ultimate.models.Policy;
import SpringBoot.Policy_Module_Ultimate.services.ActivityDetailService;
import SpringBoot.Policy_Module_Ultimate.services.ActivityService;
import SpringBoot.Policy_Module_Ultimate.services.PolicyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/activityDetail")
public class ActivityDetailController {

    @Autowired
    private ActivityDetailService activityDetailService;

    @Autowired
    private ActivityService activityService;

    @GetMapping("/activityDetail/{id}")
    public String showActivityDetails(@PathVariable(value = "id") Long id, Model model) {
        Activity activity = activityService.getActivityById(id);
        return findPaginated(activity, 1, "id", "asc", model);
    }

    @GetMapping("/page/{pageNo}")
    public String findPaginated(Activity activity, @PathVariable(value = "pageNo") Integer pageNo,
                                @RequestParam("sortField") String sortField,
                                @RequestParam("sortDir") String sortDir,
                                Model model) {

        Integer pageSize = 5;
        Page<ActivityDetail> page = activityDetailService.findPaginatedByActivity(activity, pageNo, pageSize, sortField, sortDir);
        List<ActivityDetail> listActivityDetail = page.getContent();

        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());

        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");

        model.addAttribute("listActivityDetail", listActivityDetail);

        return "activityDetail/homeActivityDetail";
    }

}
