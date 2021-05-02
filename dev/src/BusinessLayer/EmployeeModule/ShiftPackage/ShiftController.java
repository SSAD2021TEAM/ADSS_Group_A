package BusinessLayer.EmployeeModule.ShiftPackage;

import java.time.LocalDate;
import java.util.*;

import DataAccessLayer.EmployeeModule.DALController;
import Resources.Role;

public class ShiftController {
    private List<Shift> shifts;
    private ShiftPersonnel sp;
    private Shift activeShift;
    private DALController dalController;

    public ShiftController(DALController dalController) {
        shifts = new ArrayList<Shift>();
        sp = new ShiftPersonnel(dalController);
        activeShift = null;
        this.dalController = dalController;
    }

    public Shift getShift(LocalDate date, boolean isMorning) {
        for (Shift shift : shifts) {
            if (shift.getDate().equals(date) && shift.isMorning() == isMorning) {
                activeShift = shift;
                return shift;
            }
        }
        throw new NoSuchElementException("there is no shift at the time you want.");
    }

    public List<Shift> getShifts() {
        return shifts;
    }

    public boolean AssignToShift(String id, Role skill) {
        if (activeShift == null)
            throw new NullPointerException("need a shift to assign this employee to.");
        int amountPlanned = sp.getQtty(getDay(activeShift.getDate()), activeShift.isMorning()).get(skill);
        int actualAmount = activeShift.assignEmployee(skill, id);
        if (actualAmount > amountPlanned)
            throw new IndexOutOfBoundsException("Note that you only needed " + amountPlanned + " " + skill + "s\nAnd now the amount is - " + actualAmount);
        return true;
    }

    public boolean removeFromShift(String id) {
        if (activeShift == null)
            throw new NullPointerException("need a shift to remove this employee from.");
        if (!activeShift.removeFromShift(id))
            throw new IllegalArgumentException(id + " is not assigned to this shift.");
        return true;
    }

    public void definePersonnelForShift(int day, boolean isMorning, Role skill, int qtty) {
        sp.setQtty(day, isMorning, skill, qtty);
    }

    public boolean addShift(LocalDate date, boolean isMorning) {
        int day = getDay(date);
        int index = isMorning ? day - 1 : day + 5;
        if (day >= 7)
            throw new IndexOutOfBoundsException("This is a saturday or a day larger than 7.");
        if (index > 10)
            throw new IllegalArgumentException("this shift is on rest day");
        for (Shift shift: shifts)
            if(shift.getDate().isEqual(date) && shift.isMorning()==isMorning)
                throw new IllegalArgumentException("this shift is already exists");
        activeShift = new Shift(date, isMorning);
        return shifts.add(activeShift);
    }

    public boolean removeShift(LocalDate date, boolean isMorning) {
        boolean success = shifts.remove(getShift(date, isMorning));
        if (success)
            activeShift = null;
        return success;
    }

    public int getDay(LocalDate date) {
        int day = (date.getDayOfWeek().getValue() + 1) % 7;
        if (day == 0) return 7;
        return day;
    }

    public Map<Shift, Role> getEmpShifts(String id) {
        Map<Shift, Role> empShifts = new HashMap();
        for (Shift shift : shifts) {
            Role role = shift.isAssignedToShift(id);
            if (role != null)
                empShifts.put(shift, role);
        }
        return empShifts;
    }

    public Map<Role, Integer> getPersonnelForShift(int day, boolean isMorning) {
        return sp.getQtty(day, isMorning);
    }

    public boolean API_isRoleAssignedToShift(LocalDate date, boolean isMorning, Role role) {
        try {
            List<String> IDs = getShift(date, isMorning).getPositions().get(role);
            return IDs != null && IDs.size() > 0;
        } catch (Exception ignored) {
            return false;
        }
    }

    public boolean API_isDriverAssignedToShift(LocalDate date, boolean isMorning, String ID) {
        try {
            List<String> IDs = getShift(date, isMorning).getPositions().get(Role.Driver);
            return IDs != null && IDs.size() > 0;
        } catch (Exception ignored) {
            return false;
        }
    }
}