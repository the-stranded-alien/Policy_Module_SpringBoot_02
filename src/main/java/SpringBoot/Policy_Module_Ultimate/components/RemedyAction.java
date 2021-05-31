package SpringBoot.Policy_Module_Ultimate.components;

import SpringBoot.Policy_Module_Ultimate.models.Action;
import SpringBoot.Policy_Module_Ultimate.models.Activity;
import SpringBoot.Policy_Module_Ultimate.services.*;
import SpringBoot.Policy_Module_Ultimate.services.mail.MailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

@Component
public class RemedyAction {

    @Autowired
    private MailService mailService;

    @Autowired
    private UserService userService;

    @Autowired
    private RiskService riskService;

    @Autowired
    private PolicyService policyService;

    @Autowired
    private ActivityService activityService;

    @Autowired
    private ActivityDetailService activityDetailService;

    @Autowired
    private ActionService actionService;

    private static final Logger log = LoggerFactory.getLogger(Task.class);
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    @Scheduled(fixedRate = 60000L)
    public void performAction() {

        LocalDateTime timeNow = LocalDateTime.now(ZoneId.systemDefault());
        log.info("Action's Time : {}", timeNow);

        LocalDateTime currentActionStartTime = timeNow.minusMinutes(1);
        LocalDateTime currentActionEndTime = timeNow;

        // Fetch All The Actions That Are Not Performed In The Last "fixedRate" Minutes
        List<Action> currentActions = actionService.getActionsByStatusAndTime(false, currentActionStartTime, currentActionEndTime);

        for(Action currentAction : currentActions) {
            // Fetch The Activity For The Current Action
            Activity currentActivity = currentAction.getActivity();
            // Update This Action Status To Done (True) As It Would Be Taken Care Of Now
            actionService.updateActionStatusById(currentAction.getId());

            String actionType = currentAction.getActionType();

            File currentDir = new File(".");
            File userDirectoryPath = new File(currentDir + currentActivity.getUser().getFileDirectory());
            Path currentFilePath = Paths.get("" + userDirectoryPath + "/" + currentActivity.getFileName());
            Path targetPathMove = Paths.get("" + userDirectoryPath + "/moved/" + currentActivity.getFileName());
            Path targetPathRename = Paths.get("" + userDirectoryPath + "/renamed/" + currentActivity.getFileName());
            Path targetPathNotify = Paths.get("" + userDirectoryPath + "/notified/" + currentActivity.getFileName());

            String UserEmail = currentActivity.getUser().getEmail();

            if(actionType.equals("delete")) {
                try {
                    Files.deleteIfExists(currentFilePath);
                } catch (IOException err) {
                    System.out.println("Unable To Delete This File " + err.getMessage());
                }
            } else if (actionType.equals("move")) {
                try {
                    Files.move(currentFilePath, targetPathMove);
                } catch (IOException err) {
                    System.out.println("Unable To Move This File " + err.getMessage());
                }
            } else if (actionType.equals("rename")) {
                try {
                    Files.move(currentFilePath, targetPathRename);
                } catch (IOException err) {
                    System.out.println("Unable To Rename The File " + err.getMessage());
                }
            } else if (actionType.equals("notify")) {
                try {
                    mailService.sendMail(UserEmail, currentActivity.getPoliciesViolated().toString(),"Policies Violated For File : " + currentActivity.getFileName());
                } catch(Exception err) {
                    System.out.println("Unable To Send Email");
                }
                try {
                    Files.move(currentFilePath, targetPathNotify);
                } catch (IOException err) {
                    System.out.println("Unable To Move The File After Sending Notification " + err.getMessage());
                }
            } else if (actionType.equals("unknown")) {
                log.info("Some Wrong Remedy Type In Actions Sending Notifications Instead");
                try {
                    mailService.sendMail(UserEmail, currentActivity.getPoliciesViolated().toString(),"Policies Violated For File : " + currentActivity.getFileName());
                } catch(Exception err) {
                    System.out.println("Unable To Send Email");
                }
                try {
                    Files.move(currentFilePath, targetPathNotify);
                } catch (IOException err) {
                    System.out.println("Unable To Move The File After Sending Notification " + err.getMessage());
                }
            } else {
                log.info("Wrong Remedy Type In Actions !!");
            }
        } // Current Action Loop Ends Here
    } // Scheduler Loop Ends Here
}
