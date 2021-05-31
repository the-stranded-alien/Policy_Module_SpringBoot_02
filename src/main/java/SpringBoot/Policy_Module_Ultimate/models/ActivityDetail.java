package SpringBoot.Policy_Module_Ultimate.models;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
public class ActivityDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "activity_id", nullable = false)
    private Activity activity;

    @ManyToOne
    @JoinColumn(name = "policy_id", nullable = false)
    private Policy policy;

    private String policyResult;

    @ManyToMany
    @JoinTable(
            name = "activity_risk_checked",
            joinColumns = @JoinColumn(
                    name = "activity_detail_id", referencedColumnName = "id",
                    nullable = false, updatable = false),
            inverseJoinColumns = @JoinColumn(
                    name = "risk_id", referencedColumnName = "id",
                    nullable = false, updatable = false))
    private Set<Risk> risksCheckedAgainst = new HashSet<>();

    @ManyToMany
    @JoinTable(
            name = "activity_risk_violated",
            joinColumns = @JoinColumn(
                    name = "activity_detail_id", referencedColumnName = "id",
                    nullable = false, updatable = false),
            inverseJoinColumns = @JoinColumn(
                    name = "risk_id", referencedColumnName = "id",
                    nullable = false, updatable = false))
    private Set<Risk> risksViolated = new HashSet<>();

    @ManyToMany
    @JoinTable(
            name = "activity_risk_not_violated",
            joinColumns = @JoinColumn(
                    name = "activity_detail_id", referencedColumnName = "id",
                    nullable = false, updatable = false),
            inverseJoinColumns = @JoinColumn(
                    name = "risk_id", referencedColumnName = "id",
                    nullable = false, updatable = false))
    private Set<Risk> risksNotViolated = new HashSet<>();

    public ActivityDetail() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public Policy getPolicy() {
        return policy;
    }

    public void setPolicy(Policy policy) {
        this.policy = policy;
    }

    public Set<Risk> getRisksCheckedAgainst() {
        return risksCheckedAgainst;
    }

    public void setRisksCheckedAgainst(Set<Risk> risksCheckedAgainst) {
        this.risksCheckedAgainst = risksCheckedAgainst;
    }

    public Set<Risk> getRisksViolated() {
        return risksViolated;
    }

    public void setRisksViolated(Set<Risk> risksViolated) {
        this.risksViolated = risksViolated;
    }

    public Set<Risk> getRisksNotViolated() {
        return risksNotViolated;
    }

    public void setRisksNotViolated(Set<Risk> risksNotViolated) {
        this.risksNotViolated = risksNotViolated;
    }

    public String getPolicyResult() {
        return policyResult;
    }

    public void setPolicyResult(String policyResult) {
        this.policyResult = policyResult;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ActivityDetail that = (ActivityDetail) o;

        return id != null ? id.equals(that.id) : that.id == null;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "ActivityDetail{" +
                "id=" + id +
                ", activity=" + activity +
                ", policy=" + policy +
                ", policyResult='" + policyResult + '\'' +
                ", risksCheckedAgainst=" + risksCheckedAgainst +
                ", risksViolated=" + risksViolated +
                ", risksNotViolated=" + risksNotViolated +
                '}';
    }
}
