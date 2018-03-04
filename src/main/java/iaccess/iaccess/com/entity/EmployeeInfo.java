package iaccess.iaccess.com.entity;

/**
 * Created by TONMOYPC on 2/20/2018.
 */

public class EmployeeInfo {
    String id;
    String name;
    String designation;
    String role;

    public EmployeeInfo(String id,String name,String designation,String role){
        this.id = id;
        this.name = name;
        this.designation = designation;
        this.role = role;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
