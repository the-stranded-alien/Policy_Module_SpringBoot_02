package SpringBoot.Policy_Module_Ultimate.models;


import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Risk {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Column(columnDefinition = "boolean default false")
    private Boolean status;

    private String keyWords;
    private String regex;
    private String description;
    @Column(columnDefinition = "integer default 1")
    private Integer riskMatchCount;

    @ManyToMany(mappedBy = "risksInvolved", fetch = FetchType.EAGER)
    private Set<Policy> policiesIncludedIn = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "creator_id", nullable = false)
    private User creator;

    @ManyToMany(mappedBy = "risksCheckedAgainst", fetch = FetchType.EAGER)
    private Set<ActivityDetail> activitiesInvolvedIn = new HashSet<>();

    @ManyToMany(mappedBy = "risksViolated", fetch = FetchType.EAGER)
    private Set<ActivityDetail> activitiesViolatedIn = new HashSet<>();

    @ManyToMany(mappedBy = "risksNotViolated", fetch = FetchType.EAGER)
    private Set<ActivityDetail> activitiesNotViolatedIn = new HashSet<>();

    public Risk() {

    }

    public Risk(String title, Boolean status, String keyWords, String regex, String description, Integer riskMatchCount, String username) {
        this.title = title;
        this.status = status;
        this.keyWords = keyWords;
        this.regex = regex;
        this.description = description;
        this.riskMatchCount = riskMatchCount;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public String getKeyWords() {
        return keyWords;
    }

    public void setKeyWords(String keyWords) {
        this.keyWords = keyWords;
    }

    public String getRegex() {
        return regex;
    }

    public void setRegex(String regex) {
        this.regex = regex;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getRiskMatchCount() {
        return riskMatchCount;
    }

    public void setRiskMatchCount(Integer riskMatchCount) {
        this.riskMatchCount = riskMatchCount;
    }

    public Set<Policy> getPoliciesIncludedIn() {
        return policiesIncludedIn;
    }

    public void setPoliciesIncludedIn(Set<Policy> policiesIncludedIn) {
        this.policiesIncludedIn = policiesIncludedIn;
    }

    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }

    public Set<ActivityDetail> getActivitiesInvolvedIn() {
        return activitiesInvolvedIn;
    }

    public void setActivitiesInvolvedIn(Set<ActivityDetail> activitiesInvolvedIn) {
        this.activitiesInvolvedIn = activitiesInvolvedIn;
    }

    public Set<ActivityDetail> getActivitiesViolatedIn() {
        return activitiesViolatedIn;
    }

    public void setActivitiesViolatedIn(Set<ActivityDetail> activitiesViolatedIn) {
        this.activitiesViolatedIn = activitiesViolatedIn;
    }

    public Set<ActivityDetail> getActivitiesNotViolatedIn() {
        return activitiesNotViolatedIn;
    }

    public void setActivitiesNotViolatedIn(Set<ActivityDetail> activitiesNotViolatedIn) {
        this.activitiesNotViolatedIn = activitiesNotViolatedIn;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Risk risk = (Risk) o;

        return id != null ? id.equals(risk.id) : risk.id == null;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "Risk{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", status=" + status +
                ", keyWords='" + keyWords + '\'' +
                ", regex='" + regex + '\'' +
                ", description='" + description + '\'' +
                ", riskMatchCount=" + riskMatchCount +
                ", policiesIncludedIn=" + policiesIncludedIn +
                ", creator=" + creator +
                ", activitiesInvolvedIn=" + activitiesInvolvedIn +
                ", activitiesViolatedIn=" + activitiesViolatedIn +
                ", activitiesNotViolatedIn=" + activitiesNotViolatedIn +
                '}';
    }
}

