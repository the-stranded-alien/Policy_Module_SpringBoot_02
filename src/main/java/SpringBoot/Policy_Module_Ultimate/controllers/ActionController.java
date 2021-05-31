package SpringBoot.Policy_Module_Ultimate.controllers;

import SpringBoot.Policy_Module_Ultimate.models.Action;
import SpringBoot.Policy_Module_Ultimate.models.Activity;
import SpringBoot.Policy_Module_Ultimate.services.ActionService;
import SpringBoot.Policy_Module_Ultimate.services.ActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/action")
public class ActionController {

    @Autowired
    private ActionService actionService;

    @Autowired
    private ActivityService activityService;

    @GetMapping("/action/{id}")
    public String showActionForActivity(@PathVariable(name = "id") Long id, Model model) {
        Activity activity = activityService.findActivityById(id);
        Action action = actionService.getActionByActivity(activity);

        model.addAttribute("action", action);

        return "action/homeAction";
    }

}
