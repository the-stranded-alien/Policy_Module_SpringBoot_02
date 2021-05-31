package SpringBoot.Policy_Module_Ultimate.models;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Policy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String policyName;
    private String remedyType;
    private Integer remedyTime;

    @Column(columnDefinition = "boolean default false")
    private Boolean notifyUser;
    @Column(columnDefinition = "boolean default false")
    private Boolean notifyAdmin;

    private String adminEmail;
    private String adminEmailSubject;


    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(
            name = "policy_risks",
            joinColumns = @JoinColumn(
                    name = "policy_id", referencedColumnName = "id",
                    nullable = false, updatable = false),
            inverseJoinColumns = @JoinColumn(
                    name = "risk_id", referencedColumnName = "id",
                    nullable = false, updatable = false))
    private Set<Risk> risksInvolved = new HashSet<>();

    @ManyToOne
    @JoinColumn(name="creator_id", nullable = false)
    private User creator;

    @ManyToMany(mappedBy = "policiesCheckedAgainst", fetch = FetchType.EAGER)
    private Set<Activity> activitiesInvolvedIn = new HashSet<>();

    @ManyToMany(mappedBy = "policiesViolated", fetch = FetchType.EAGER)
    private Set<Activity> activitiesViolatedIn = new HashSet<>();

    @ManyToMany(mappedBy = "policiesNotViolated", fetch = FetchType.EAGER)
    private Set<Activity> activitiesNotViolatedIn = new HashSet<>();

    @OneToMany(mappedBy = "policy")
    private Set<ActivityDetail> activityDetails = new HashSet<>();

    public Policy() {

    }

    public Policy(String policyName, String remedyType, Integer remedyTime, Boolean notifyUser, Boolean notifyAdmin, String adminEmail, String adminEmailSubject) {
        this.policyName = policyName;
        this.remedyType = remedyType;
        this.remedyTime = remedyTime;
        this.notifyUser = notifyUser;
        this.notifyAdmin = notifyAdmin;
        this.adminEmail = adminEmail;
        this.adminEmailSubject = adminEmailSubject;
    }

    public Set<Activity> getActivitiesInvolvedIn() {
        return activitiesInvolvedIn;
    }

    public void setActivitiesInvolvedIn(Set<Activity> activitiesInvolvedIn) {
        this.activitiesInvolvedIn = activitiesInvolvedIn;
    }

    public Set<Activity> getActivitiesViolatedIn() {
        return activitiesViolatedIn;
    }

    public void setActivitiesViolatedIn(Set<Activity> activitiesViolatedIn) {
        this.activitiesViolatedIn = activitiesViolatedIn;
    }

    public Set<Activity> getActivitiesNotViolatedIn() {
        return activitiesNotViolatedIn;
    }

    public void setActivitiesNotViolatedIn(Set<Activity> activitiesNotViolatedIn) {
        this.activitiesNotViolatedIn = activitiesNotViolatedIn;
    }

    public Set<ActivityDetail> getActivityDetails() {
        return activityDetails;
    }

    public void setActivityDetails(Set<ActivityDetail> activityDetails) {
        this.activityDetails = activityDetails;
    }

    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPolicyName() {
        return policyName;
    }

    public void setPolicyName(String policyName) {
        this.policyName = policyName;
    }


    public String getRemedyType() {
        return remedyType;
    }

    public void setRemedyType(String remedyType) {
        this.remedyType = remedyType;
    }

    public Integer getRemedyTime() {
        return remedyTime;
    }

    public void setRemedyTime(Integer remedyTime) {
        this.remedyTime = remedyTime;
    }

    public Boolean getNotifyUser() {
        return notifyUser;
    }

    public void setNotifyUser(Boolean notifyUser) {
        this.notifyUser = notifyUser;
    }

    public Boolean getNotifyAdmin() {
        return notifyAdmin;
    }

    public void setNotifyAdmin(Boolean notifyAdmin) {
        this.notifyAdmin = notifyAdmin;
    }

    public String getAdminEmail() {
        return adminEmail;
    }

    public void setAdminEmail(String adminEmail) {
        this.adminEmail = adminEmail;
    }

    public String getAdminEmailSubject() {
        return adminEmailSubject;
    }

    public void setAdminEmailSubject(String adminEmailSubject) {
        this.adminEmailSubject = adminEmailSubject;
    }

    public Set<Risk> getRisksInvolved() {
        return risksInvolved;
    }

    public void setRisksInvolved(Set<Risk> risksInvolved) {
        this.risksInvolved = risksInvolved;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Policy policy = (Policy) o;

        return id != null ? id.equals(policy.id) : policy.id == null;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "Policy{" +
                ", policyName='" + policyName + '\'' +
                ", remedyType='" + remedyType + '\'' +
                ", remedyTime='" + remedyTime + '\'' +
                ", notifyUser=" + notifyUser +
                ", notifyAdmin=" + notifyAdmin +
                ", adminEmail='" + adminEmail + '\'' +
                ", adminEmailSubject='" + adminEmailSubject + '\'' +
                '}';
    }
}
