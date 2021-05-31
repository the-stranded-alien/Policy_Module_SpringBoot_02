package SpringBoot.Policy_Module_Ultimate.controllers;

import SpringBoot.Policy_Module_Ultimate.models.Risk;
import SpringBoot.Policy_Module_Ultimate.services.RiskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.List;

@Controller
@RequestMapping("/risk")
public class RiskController {

    @Autowired
    private RiskService riskService;

    // Display List of All Risks Created By The Current Logged In User
    @GetMapping
    public String viewRiskHomePage(Model model) {
        return findPaginated(1, "title", "asc", model);
    }

    // For Adding A New Risk
    @GetMapping("/showNewRiskForm")
    public String showNewRiskForm(Model model) {
        Risk risk = new Risk();
        model.addAttribute("risk", risk);
        return "risk/newRisk";
    }

    // For Saving the Added/Updated Risk
    @PostMapping("/saveRisk")
    public String saveRisk(@ModelAttribute("risk") Risk risk) {
        // Save Risk to the Database
        riskService.saveRisk(risk);
        return "redirect:/risk";
    }

    // For Updating a Particular Risk
    @GetMapping("/showUpdateRiskForm/{id}")
    public String showUpdateRiskForm(@PathVariable(value="id") Long id, Model model) {
        // Get the Risk from the Service Using the "id"
        Risk risk = riskService.getRiskById(id);
        // Set Risk as a Model Attribute to Pre-populate the Form !
        model.addAttribute("risk", risk);
        return "risk/updateRisk";
    }

    // For Deleting a Risk by "id"
    @GetMapping("/deleteRisk/{id}")
    @Transactional
    public String deleteRisk(@PathVariable(value="id") Long id) {
        this.riskService.deleteRiskById(id);
        return "redirect:/risk";
    }

    @GetMapping("/page/{pageNo}")
    public String findPaginated(@PathVariable(value="pageNo") Integer pageNo,
                                @RequestParam("sortField") String sortField,
                                @RequestParam("sortDir") String sortDir,
                                Model model) {

        Integer pageSize = 5;
        Page<Risk> page = riskService.findPaginated(pageNo, pageSize, sortField, sortDir);
        List<Risk> listRisks = page.getContent();

        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());

        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");

        model.addAttribute("listRisks", listRisks);

        return "risk/homeRisk";
    }

}
