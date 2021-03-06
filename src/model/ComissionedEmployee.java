package model;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ComissionedEmployee extends SalaraiedEmployee {

	private List<Sale> sales = new ArrayList<Sale>();
	private double comission;

	public ComissionedEmployee(int id, int syndicateId, String name,
			String adress, PaymentMethod paymentMethod, Calendar admissionDate,
			Calendar lastPayDate, Calendar nextPayDate, boolean isOnSyndicate,
			double monthlyFee, double deduction, double salary,
			List<Sale> sales, double comission) {
		super(id, syndicateId, name, adress, paymentMethod, admissionDate,
				lastPayDate, nextPayDate, isOnSyndicate, monthlyFee, deduction,
				salary);
		this.sales = sales;
		this.comission = comission;
	}

	public ComissionedEmployee(String name, String adress,
			PaymentMethod paymentMethod, Calendar admissionDate, double salary,
			double comission) {
		super(name, adress, paymentMethod, admissionDate, salary);
		this.comission = comission;

		int daysToFriday = Calendar.FRIDAY
				- admissionDate.get(Calendar.DAY_OF_WEEK);

		Calendar nextPayDate = Calendar.getInstance();

		if (daysToFriday > 0) {
			nextPayDate.add(Calendar.DAY_OF_WEEK, daysToFriday);
		} else
			nextPayDate.add(Calendar.DAY_OF_WEEK, 7);

		daysToFriday = Calendar.FRIDAY - nextPayDate.get(Calendar.DAY_OF_WEEK);
		if (daysToFriday > 0) {
			nextPayDate.add(Calendar.DAY_OF_WEEK, daysToFriday);
		} else
			nextPayDate.add(Calendar.DAY_OF_WEEK, 7);
		super.setNextPayDate(nextPayDate);
	}

	public ComissionedEmployee(String name, String adress, double salary,
			double comission) {
		super(name, adress, salary);
		this.comission = comission;
	}

	public List<Sale> getSales() {
		return sales;
	}

	public void setSales(List<Sale> sales) {
		this.sales = sales;
	}

	public double getComission() {
		return comission;
	}

	public void setComission(double comission) {
		this.comission = comission;
	}

	public void addSale(Sale sale) {
		this.sales.add(sale);
	}

	@Override
	public double getLiquidSalary() {
		double liquidSalary = this.getSalary() / 2;

		for (Sale sale : this.getSales()) {
			if (sale.getSaleDate().getTimeInMillis() > this.getLastPayDate()
					.getTimeInMillis()
					&& sale.getSaleDate().getTimeInMillis() < this
							.getNextPayDate().getTimeInMillis()) {
				liquidSalary += sale.getValue() * this.comission;
			}
		}
		if (this.isOnSyndicate()) {
			liquidSalary += -this.getMonthlyFee() - this.getDeduction();
		}

		return liquidSalary;
	}

	@Override
	public void pay() {
		this.setLastPayDate(this.getNextPayDate());

		Calendar nextPayDay = this.getNextPayDate();
		nextPayDay.add(Calendar.DATE, 14);
		this.setNextPayDate(nextPayDay);

		this.setDeduction(0);
	}

	@Override
	public ComissionedEmployee clone() {
		ComissionedEmployee cloned;

		List<Sale> clonedSales = new ArrayList<Sale>();
		for (Sale sale : this.getSales()) {
			clonedSales.add(sale.clone());
		}

		cloned = new ComissionedEmployee(this.getId(), this.getSyndicateId(),
				this.getName(), this.getAdress(), this.getPaymentMethod(),
				(Calendar) this.getAdmissionDate().clone(), (Calendar) this
						.getLastPayDate().clone(),
				(Calendar) this.getNextPayDate(), this.isOnSyndicate(),
				this.getMonthlyFee(), this.getDeduction(), this.getSalary(),
				clonedSales, this.getComission());

		return cloned;
	}

}
