package SpringBoot.Policy_Module_Ultimate.components;

import SpringBoot.Policy_Module_Ultimate.models.*;
import SpringBoot.Policy_Module_Ultimate.services.*;
import SpringBoot.Policy_Module_Ultimate.services.mail.MailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.Set;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.time.LocalDateTime;
import java.util.*;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

@Component
public class Task {

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
    public void performTask() {

        log.info("Task's Time : {}", dateFormat.format(new Date()));
        List<User> allUsers = userService.findAll();

        for(User currentUser : allUsers) {

            String currentUserFullName = currentUser.getFirstName() + " " + currentUser.getLastName();
            String currentUserEmail = currentUser.getEmail();
            String currentUserUsername = currentUser.getUsername();
            String currentUserDirectory = currentUser.getFileDirectory();

            log.info("User's Full Name : {}, Username : {} & Directory : {}",
                    currentUserFullName, currentUserUsername, currentUserDirectory);

            // Fetch All User Policies From The DB
            List<Policy> currentUserPolicies = policyService.getAllPoliciesByCreator(currentUser);

            // Fetch All The User Risks For Each Policy From The DB
            List<Set<Risk>> currentUserPolicyRiskPairs = new ArrayList<Set<Risk>>();
            for(Policy currentPolicy : currentUserPolicies) {
                Set<Risk> currentPolicyEnabledRisks = riskService.getAllRisksByCreatorAndStatusAndPolicy(currentUser, true, currentPolicy);
                currentUserPolicyRiskPairs.add(currentPolicyEnabledRisks);
            }

            File currentDir = new File(".");
            File userDirectoryPath = new File(currentDir + currentUserDirectory);
            File userFileList[] = userDirectoryPath.listFiles();

            // Now Iterate Over Each File From User's Directory And
            // Do All The Required Stuff In One Go !
            for(File currentFile : userFileList) {
                // Fetch File Details
                Path path = Paths.get("" + currentFile);
                BasicFileAttributes fileAttributes;
                try {
                    fileAttributes = Files.readAttributes(path, BasicFileAttributes.class);
                    Boolean isDirectory = fileAttributes.isDirectory();
                    if(!isDirectory) {
                        FileTime fileCreationTime = fileAttributes.creationTime();
                        FileTime fileLastModifiedTime = fileAttributes.lastModifiedTime();
                        FileTime fileLastAccessTime = fileAttributes.lastAccessTime();

                        Long currentTimeInMillis = Calendar.getInstance().getTimeInMillis();
                        Long fileCreationTimeInMillis = fileCreationTime.toMillis();
                        Long fileLastModifiedTimeInMillis = fileLastModifiedTime.toMillis();
                        Long fileLastAccessTimeInMillis = fileLastAccessTime.toMillis();
                        Long timeInHoursFilesNeededBeChecked = 1l; // Hardcoded To 1 Hour Right Now, Can Be Given As An Option To User To Set
                        Long timeInMillisFilesNeededBeChecked = timeInHoursFilesNeededBeChecked * 3600000L;

                        // Condition For Eligible Files :
                        if ((currentTimeInMillis - fileCreationTimeInMillis <= timeInMillisFilesNeededBeChecked) ||
                                (currentTimeInMillis - fileLastModifiedTimeInMillis <= timeInMillisFilesNeededBeChecked) ||
                                (currentTimeInMillis - fileLastAccessTimeInMillis <= timeInMillisFilesNeededBeChecked)
                        ) {
                            log.info("Eligible : File Name -> {} & File Path -> {}", currentFile.getName(), currentFile.getAbsolutePath());
                            Long timeDiff = Math.min((currentTimeInMillis - fileCreationTimeInMillis), Math.min((currentTimeInMillis - fileLastModifiedTimeInMillis), (currentTimeInMillis - fileLastAccessTimeInMillis)));
                            double timeDiffInMinutes = ((double) timeDiff / 3600000);
                            log.info("Time Difference (In Minutes) -> {}", timeDiffInMinutes);

                            // For Each Eligible File Initially Consider That It Doesn't Violate Any User Policy
                            boolean overallResultForFile = true;

                            // For Each Eligible File Create A New Activity & Set Few Basic Details
                            Activity currentActivity = new Activity();
                            currentActivity.setActivityLogTime(LocalDateTime.now());
                            currentActivity.setUser(currentUser);
                            currentActivity.setFileName(currentFile.getName());
                            currentActivity.setFileCreationTime(LocalDateTime.ofInstant(fileCreationTime.toInstant(), ZoneId.systemDefault()));
                            currentActivity.setFileLastModifiedTime(LocalDateTime.ofInstant(fileLastModifiedTime.toInstant(), ZoneId.systemDefault()));

                            // For Each Activity Initialize An Action
                            Action currentAction = new Action();
                            currentAction.setActivity(currentActivity);

                            // Initializing A HashMap For Each Eligible File For Keeping Track of Remedy
                            // String : Remedy Type
                            // Integer : Minimum Remedy Time For That Remedy Type
                            HashMap<String, Integer> allRemedies = new HashMap<>();

                            // Now If File Is Eligible Fetch The Data From It
                            try {
                                Scanner fileScanner = new Scanner(currentFile);
                                String fileTextLine;
                                StringBuffer fileText = new StringBuffer();
                                while (fileScanner.hasNextLine()) {
                                    fileTextLine = fileScanner.nextLine();
                                    fileText.append(fileTextLine + " ");
                                }

                                String fileTextString = fileText.toString();
                                String[] fileTextTokens = fileTextString.split("[,;. \t\n\r]++");

                                HashMap<String, Integer> fileTextHashMap = new HashMap<>();

                                // Fetch All String Tokens And Put Them In A HashMap
                                for (String str : fileTextTokens) {
                                    if (!fileTextHashMap.containsKey(str)) {
                                        fileTextHashMap.put(str, 1);
                                    } else {
                                        fileTextHashMap.put(str, fileTextHashMap.get(str) + 1);
                                    }
                                }

                                // Now Check This File (Using fileTextHashMap) For Each Policy
                                Integer riskItr = 0;
                                for (Policy currentPolicy : currentUserPolicies) {

                                    // Add This Policy To Checked Against List For Current File Activity
                                    currentActivity.getPoliciesCheckedAgainst().add(currentPolicy);

                                    // Check if Current Policy Is Violated
                                    boolean isCurrentPolicyViolated = false;

                                    // Create A Activity Detail For Each Activity-Policy (File-Policy) Pair
                                    ActivityDetail currentActivityDetail = new ActivityDetail();
                                    currentActivityDetail.setActivity(currentActivity);
                                    currentActivityDetail.setPolicy(currentPolicy);

                                    // Fetch All Risks For Current Policy From List<Set<Risk>> Prepared Earlier !
                                    Set<Risk> currentPolicyRisks = currentUserPolicyRiskPairs.get(riskItr++);

                                    for (Risk currentRisk : currentPolicyRisks) {
                                        // Add This Risk To Checked Against List For Current File-Policy Activity Detail
                                        currentActivityDetail.getRisksCheckedAgainst().add(currentRisk);

                                        // Check if Current Risk Violates The Policy
                                        boolean isCurrentRiskViolated = false;

                                        // Check For Keyword Violation
                                        boolean isKeywordRisk = false;
                                        if (!currentRisk.getKeyWords().isEmpty()) {
                                            ArrayList<String> keyWordList = new ArrayList<>();
                                            if (currentRisk.getKeyWords() != null) {
                                                String[] tempKeyWords = currentRisk.getKeyWords().split("[,;. \t\n\r]++");
                                                for (String kw : tempKeyWords) {
                                                    if (kw.equals(" ")) continue;
                                                    else {
                                                        keyWordList.add(kw.trim());
                                                    }
                                                }
                                            }
                                            for (int kwItr = 0; kwItr < keyWordList.size(); kwItr++) {
                                                if (fileTextHashMap.containsKey(keyWordList.get(kwItr))) {
                                                    if (fileTextHashMap.get(keyWordList.get(kwItr)) >= currentRisk.getRiskMatchCount()) {
                                                        isKeywordRisk = true;
                                                    }
                                                }
                                            }
                                        }

                                        // Check For Regex Violation
                                        boolean isRegexRisk = false;
                                        if (!currentRisk.getRegex().isEmpty()) {
                                            Pattern pattern = Pattern.compile(currentRisk.getRegex());
                                            for (String str : fileTextTokens) {
                                                Matcher matcher = pattern.matcher(str);
                                                boolean matchFound = matcher.find();
                                                if (matchFound) {
                                                    isRegexRisk = true;
                                                }
                                            }
                                        }

                                        // If There Is Either Keyword Risk Or Regex Risk Or Both Then Risk Is Violated
                                        if (isKeywordRisk || isRegexRisk) {
                                            isCurrentRiskViolated = true;
                                            currentActivityDetail.getRisksViolated().add(currentRisk);
                                        } else {
                                            currentActivityDetail.getRisksNotViolated().add(currentRisk);
                                        }

                                        // Even If One Risk Is Violated Then The Policy Is Violated
                                        if (isCurrentRiskViolated) {
                                            isCurrentPolicyViolated = true;
                                        }
                                    } // Risk Loop Ends Here

                                    if (isCurrentPolicyViolated) {
                                        overallResultForFile = false;
                                        currentActivity.getPoliciesViolated().add(currentPolicy);
                                        currentActivityDetail.setPolicyResult("Violated");
                                    } else {
                                        currentActivity.getPoliciesNotViolated().add(currentPolicy);
                                        currentActivityDetail.setPolicyResult("Not Violated");
                                    }

                                    // Save Activity Detail For Each Activity-Policy (File-Policy Pair)
                                    // And Also Add It To The Activity
                                    activityDetailService.saveActivityDetail(currentActivityDetail);
                                    currentActivity.getActivityDetails().add(currentActivityDetail);

                                    // Manage Remedy (Action) For Current Policy
                                    String currentPolicyRemedyType = currentPolicy.getRemedyType().toLowerCase();
                                    Integer currentPolicyRemedyTime = currentPolicy.getRemedyTime();
                                    if (!allRemedies.containsKey(currentPolicyRemedyType)) {
                                        allRemedies.put(currentPolicyRemedyType, currentPolicyRemedyTime);
                                    } else {
                                        allRemedies.put(currentPolicyRemedyType, Math.min(allRemedies.get(currentPolicyRemedyType), currentPolicyRemedyTime));
                                    }

                                } // Policy Loop Ends Here

                                // Save Overall Result
                                if (!overallResultForFile) {
                                    currentActivity.setOverallResult("Violated");
                                } else {
                                    currentActivity.setOverallResult("Not Violated");
                                }

                                // Manage Remedy (Action) For The Entire Activity
                                // Remedy Type Priority
                                // 1.Delete, 2.Move, 3.Rename, 4.Notify(Email)
                                String actionType;
                                Integer actionTime;

                                if (allRemedies.containsKey("delete")) {
                                    actionType = "delete";
                                    actionTime = allRemedies.get("delete");
                                } else if (allRemedies.containsKey("move")) {
                                    actionType = "move";
                                    actionTime = allRemedies.get("move");
                                } else if (allRemedies.containsKey("rename")) {
                                    actionType = "rename";
                                    actionTime = allRemedies.get("rename");
                                } else if (allRemedies.containsKey("notify")) {
                                    actionType = "notify";
                                    actionTime = allRemedies.get("notify");
                                } else {
                                    actionType = "unknown";
                                    actionTime = 0;
                                }
                                // Set Current Action's Information
                                currentAction.setActionType(actionType);
                                currentAction.setActionTime(currentActivity.getActivityLogTime().plusMinutes(actionTime));
                                currentAction.setStatus(false);

                                // Save Action
                                actionService.saveAction(currentAction);

                                // Save Activity For Each File
                                currentActivity.setAction(currentAction);
                                activityService.saveActivity(currentActivity);

                            } catch (FileNotFoundException err) {
                                System.out.println("Some Error Fetching The File : " + err.getMessage());
                            }
                        } // Eligible File Condition Ends Here
                    } // If Is Directory
                } catch(IOException err) { System.out.println("Some Error Fetching File : " + err.getMessage()); }
            } // File Loop Ends Here
        } // User Loop Ends Here
    } // Scheduler Loop Ends Here
} // Component (Class) Loop Ends Here
