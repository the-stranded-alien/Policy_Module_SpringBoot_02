package SpringBoot.Policy_Module_Ultimate.models;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Activity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TIMESTAMP")
    private LocalDateTime activityLogTime; // Time of Log Generation

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private String fileName; // File Name Fetched From File Meta

    @Column(columnDefinition = "TIMESTAMP")
    private LocalDateTime fileCreationTime; // Creation Time (From File Meta)

    @Column(columnDefinition = "TIMESTAMP")
    private LocalDateTime fileLastModifiedTime; // Last Modified Time (From File Meta)

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "action_id", referencedColumnName = "id")
    private Action action;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(
            name = "activity_all_policies",
            joinColumns = @JoinColumn(
                    name = "activity_id", referencedColumnName = "id",
                    nullable = false, updatable = false),
            inverseJoinColumns = @JoinColumn(
                    name = "policy_id", referencedColumnName = "id",
                    nullable = false, updatable = false))
    private Set<Policy> policiesCheckedAgainst = new HashSet<>();

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(
            name = "activity_policies_violated",
            joinColumns = @JoinColumn(
                    name = "activity_id", referencedColumnName = "id",
                    nullable = false, updatable = false),
            inverseJoinColumns = @JoinColumn(
                    name = "policy_id", referencedColumnName = "id",
                    nullable = false, updatable = false))
    private Set<Policy> policiesViolated = new HashSet<>();

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(
            name = "activity_policies_not_violated",
            joinColumns = @JoinColumn(
                    name = "activity_id", referencedColumnName = "id",
                    nullable = false, updatable = false),
            inverseJoinColumns = @JoinColumn(
                    name = "policy_id", referencedColumnName = "id",
                    nullable = false, updatable = false))
    private Set<Policy> policiesNotViolated = new HashSet<>();

    @OneToMany(mappedBy = "activity")
    private Set<ActivityDetail> activityDetails = new HashSet<>();

    private String overallResult;

    public Activity() {

    }

    public Activity(LocalDateTime activityLogTime, String fileName, LocalDateTime fileCreationTime, LocalDateTime fileLastModifiedTime, String overallResult) {
        this.activityLogTime = activityLogTime;
        this.fileName = fileName;
        this.fileCreationTime = fileCreationTime;
        this.fileLastModifiedTime = fileLastModifiedTime;
        this.overallResult = overallResult;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getActivityLogTime() {
        return activityLogTime;
    }

    public void setActivityLogTime(LocalDateTime activityLogTime) {
        this.activityLogTime = activityLogTime;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public LocalDateTime getFileCreationTime() {
        return fileCreationTime;
    }

    public void setFileCreationTime(LocalDateTime fileCreationTime) {
        this.fileCreationTime = fileCreationTime;
    }

    public LocalDateTime getFileLastModifiedTime() {
        return fileLastModifiedTime;
    }

    public void setFileLastModifiedTime(LocalDateTime fileLastModifiedTime) {
        this.fileLastModifiedTime = fileLastModifiedTime;
    }

    public Action getAction() {
        return action;
    }

    public void setAction(Action action) {
        this.action = action;
    }

    public Set<ActivityDetail> getActivityDetails() {
        return activityDetails;
    }

    public void setActivityDetails(Set<ActivityDetail> activityDetails) {
        this.activityDetails = activityDetails;
    }

    public Set<Policy> getPoliciesCheckedAgainst() {
        return policiesCheckedAgainst;
    }

    public void setPoliciesCheckedAgainst(Set<Policy> policiesCheckedAgainst) {
        this.policiesCheckedAgainst = policiesCheckedAgainst;
    }

    public Set<Policy> getPoliciesViolated() {
        return policiesViolated;
    }

    public void setPoliciesViolated(Set<Policy> policiesViolated) {
        this.policiesViolated = policiesViolated;
    }

    public Set<Policy> getPoliciesNotViolated() {
        return policiesNotViolated;
    }

    public void setPoliciesNotViolated(Set<Policy> policiesNotViolated) {
        this.policiesNotViolated = policiesNotViolated;
    }

    public String getOverallResult() {
        return overallResult;
    }

    public void setOverallResult(String overallResult) {
        this.overallResult = overallResult;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Activity activity = (Activity) o;

        return id != null ? id.equals(activity.id) : activity.id == null;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "Activity{" +
                "id=" + id +
                ", activityLogTime=" + activityLogTime +
                ", user=" + user +
                ", fileName='" + fileName + '\'' +
                ", fileCreationTime=" + fileCreationTime +
                ", fileLastModifiedTime=" + fileLastModifiedTime +
                ", policiesCheckedAgainst=" + policiesCheckedAgainst +
                ", policiesViolated=" + policiesViolated +
                ", policiesNotViolated=" + policiesNotViolated +
                ", overallResult='" + overallResult + '\'' +
                '}';
    }
}
