package SpringBoot.Policy_Module_Ultimate.models;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Action {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(mappedBy = "action")
    private Activity activity;

    @Column(columnDefinition = "TIMESTAMP")
    private LocalDateTime actionTime;

    private String actionType;

    private Boolean status;

    public Action() { }

    public Action(LocalDateTime actionTime, String actionType, Boolean status) {
        this.actionTime = actionTime;
        this.actionType = actionType;
        this.status = status;
    }

    public LocalDateTime getActionTime() {
        return actionTime;
    }

    public void setActionTime(LocalDateTime actionTime) {
        this.actionTime = actionTime;
    }

    public String getActionType() {
        return actionType;
    }

    public void setActionType(String actionType) {
        this.actionType = actionType;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Action action = (Action) o;

        return id != null ? id.equals(action.id) : action.id == null;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "Action{" +
                "id=" + id +
                ", activity=" + activity +
                ", actionTime=" + actionTime +
                ", actionType='" + actionType + '\'' +
                ", status=" + status +
                '}';
    }
}
