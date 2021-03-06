package util;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import model.CommissionedEmployee;
import model.Employee;
import model.HourlyEmployee;
import model.SalariedEmployee;

public class UndoRedo {

	private static Stack<List<Employee>> doneStack = new Stack<List<Employee>>();
	private static Stack<List<Employee>> undoStack = new Stack<List<Employee>>();

	public static void doAction() {

		
		doneStack.push(getCloneEmployeesList());
		undoStack.clear();
	}

	public static void undoAction() {
		if (!doneStack.isEmpty()) {
			undoStack.push(getCloneEmployeesList());
			Employee.setEmployees(doneStack.pop());
		} else {
			System.out.println("Sem a��es para desfazer");
		}
	}

	public static void redoAction() {
		if (!undoStack.isEmpty()) {
			doneStack.push(getCloneEmployeesList());
			Employee.setEmployees(undoStack.pop());

		} else {
			System.out.println("Sem a��es para refazer");
		}

	}

	public static List<Employee> getCloneEmployeesList() {
		List<Employee> clonedList = new ArrayList<Employee>();

		for (Employee emp : Employee.getEmployees()) {
			Employee emp2 = null;
			if (emp instanceof HourlyEmployee) {
				emp2 = ((HourlyEmployee) emp).clone();
			}
			if (emp instanceof CommissionedEmployee) {
				emp2 = ((CommissionedEmployee) emp).clone();
			}
			if (emp instanceof SalariedEmployee) {
				emp2 = ((SalariedEmployee) emp).clone();
			}

			clonedList.add(emp2);

		}
		return clonedList;
	}

}
