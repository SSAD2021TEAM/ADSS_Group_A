package BusinessLayer.EmployeeModule.EmployeePackage;

import BusinessLayer.EmployeeModule.ResponseT;
import DataAccessLayer.EmployeeModule.DALController;
import Resources.Preference;
import Resources.Role;

import javax.naming.NoPermissionException;
import java.time.LocalDate;
import java.util.*;

public class EmployeeController {
    private Map<String, Employee> employees;
    private Employee activeEmployee;
    private DALController dalController;

    public EmployeeController(DALController dalController) {
        employees = new HashMap<>();
        activeEmployee = null;
        this.dalController = dalController;
    }

    public void logout() {
        activeEmployee = null;
    }

    public Employee getEmployee(String ID) throws Exception {
        if (activeEmployee != null && !activeEmployee.getIsManager() && !activeEmployee.getID().equals(ID))
            throw new NoPermissionException("The employee currently using the system doesn't have permission to view this content.");
        if (!isValidID(ID)) {
            ResponseT<Employee> empResponse = dalController.getEmployee(ID);
            if (empResponse.getErrorOccurred())
                throw new IllegalArgumentException("No employee with the ID: " + ID + " was found in the system.");
            employees.put(ID, empResponse.getValue());
        }

        return employees.get(ID);
    }

    public String getName(String ID) {
        try {
            return getEmployee(ID).getName();
        } catch (Exception ignored) { return null; }
    }

    public void login(String ID) throws Exception{
        for (Employee e : employees.values()) {
            if (e.getID().equals(ID)) {
                activeEmployee = e;
                return;
            }
        }
        activeEmployee = getEmployee(ID);
    }

    public void updateEmployee(String oldID, String name, String newID, int bankId, int branchId, int accountNumber, float salary, LocalDate startDate,
                               String trustFund, int freeDays, int sickDays, List<Role> skills, Preference[][] timeFrames) throws Exception {
        Employee toUpdate = getEmployee(oldID);
        toUpdate.setName(name);
        if (!oldID.equals(newID))
            for (String empID: employees.keySet())
                if (empID.equals(newID))
                    throw new IllegalArgumentException("There is already a user with the ID " + newID + " in the system.");
        toUpdate.setID(newID);
        toUpdate.setBankId(bankId);
        toUpdate.setBranchId(branchId);
        toUpdate.setAccountNumber(accountNumber);
        toUpdate.setSalary(salary);
        toUpdate.setStartDate(startDate);
        toUpdate.setTrustFund(trustFund);
        toUpdate.setFreeDays(freeDays);
        toUpdate.setSickDays(sickDays);
        toUpdate.setSkills(skills);
        toUpdate.setTimeFrames(timeFrames);
        if (!oldID.equals(newID)) {
            employees.remove(oldID);
            employees.put(newID, toUpdate);
        }

        dalController.updateEmployee(employees.get(newID), oldID);
    }

    public void addEmployee(String name, String ID, int bankId, int branchId, int accountNumber,
                            float salary, LocalDate startDate, String trustFund, int freeDays, int sickDays, List<Role> skills, Preference[][] timeFrames) {
        for (String s : employees.keySet()) {
            if (s.equals(ID)) {
                throw new IllegalArgumentException("There is already an employee with the ID: " + ID + " in the system.");
            }
        }

        employees.put(ID, new Employee(name, ID, bankId, branchId, accountNumber, salary, startDate, trustFund, freeDays, sickDays, skills, timeFrames));

        dalController.setEmployee(employees.get(ID));
    }

    public boolean isValidID(String ID) {
        return employees.containsKey(ID);
    }

    public boolean isManager() {
        return activeEmployee != null && activeEmployee.getIsManager();
    }

    public Map<String, String> viewAvailableEmployees(int day, boolean isMorning, Role skill) {
        Map<String, String> ret = new HashMap<>();
        for (Employee e : employees.values()) {
            if (e.hasSkill(skill)) {
                Preference p = e.getPreference(day, isMorning);
                if (p.equals(Preference.WANT))
                    ret.put(e.getID(), e.getName() + " WANTS to work at the specified date.");
                else if (p.equals(Preference.CAN))
                    ret.put(e.getID(), e.getName() + " CAN to work at the specified date.");
            }
        }
        return ret;
    }

    public Map<String, String> viewUnavailableEmployees(int day, boolean isMorning, Role skill) {
        Map<String, String> ret = new HashMap<>();
        for (Employee e : employees.values()) {
            if (e.hasSkill(skill)) {
                Preference p = e.getPreference(day, isMorning);
                if (p.equals(Preference.CANT))
                    ret.put(e.getID(), e.getName() + " CANT work at the specified date.");
            }
        }
        return ret;
    }
}