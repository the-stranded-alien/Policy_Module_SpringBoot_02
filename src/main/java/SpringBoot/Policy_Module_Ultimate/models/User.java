package SpringBoot.Policy_Module_Ultimate.models;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    private String firstName;
    private String lastName;
    private String username;
    private String email;
    private String password;
    private String fileDirectory;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(
            name = "user_role",
            joinColumns = @JoinColumn(
                    name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(
                    name = "role_id", referencedColumnName = "id"))
    private Set<Role> roles;

    @OneToMany(mappedBy = "creator")
    private Set<Risk> risksCreated = new HashSet<>();

    @OneToMany(mappedBy = "creator")
    private Set<Policy> policiesCreated = new HashSet<>();

    @OneToMany(mappedBy = "user")
    private Set<Activity> userActivities = new HashSet<>();


    public User() {

    }

    public User(String firstName, String lastName, String username, String email, String password, String fileDirectory) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.email = email;
        this.password = password;
        this.fileDirectory = fileDirectory;
    }

    public Set<Risk> getRisksCreated() {
        return risksCreated;
    }

    public void setRisksCreated(Set<Risk> risksCreated) {
        this.risksCreated = risksCreated;
    }

    public Set<Policy> getPoliciesCreated() {
        return policiesCreated;
    }

    public void setPoliciesCreated(Set<Policy> policiesCreated) {
        this.policiesCreated = policiesCreated;
    }

    public Set<Activity> getUserActivities() {
        return userActivities;
    }

    public void setUserActivities(Set<Activity> userActivities) {
        this.userActivities = userActivities;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFileDirectory() {
        return fileDirectory;
    }

    public void setFileDirectory(String fileDirectory) {
        this.fileDirectory = fileDirectory;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        return id != null ? id.equals(user.id) : user.id == null;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", fileDirectory='" + fileDirectory + '\'' +
                ", roles=" + roles +
                '}';
    }
}
