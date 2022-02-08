package BusDriverManagement.entity.table;

import BusDriverManagement.entity.Drivers;

import java.util.List;

public class AssignmentTable {
    private Drivers drivers;

    private List<Assignment> assignmentList;

    public AssignmentTable(Drivers drivers, List<Assignment> assignmentList) {
        this.drivers = drivers;
        this.assignmentList = assignmentList;
    }

    public Drivers getDrivers() {
        return drivers;
    }

    public void setDrivers(Drivers drivers) {
        this.drivers = drivers;
    }

    public List<Assignment> getAssignmentList() {
        return assignmentList;
    }

    public void setAssignmentList(List<Assignment> assignmentList) {
        this.assignmentList = assignmentList;
    }

    @Override
    public String toString() {
        return "AssignmentTable{" +
                "driver=" + drivers +
                ", assignmentList=" + assignmentList +
                '}';
    }
}
