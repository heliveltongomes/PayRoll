package model;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class HourlyEmplyee extends Employee {

	private double hourPrice;
	private List<TimeCard> timeCards = new ArrayList<TimeCard>();

	public HourlyEmplyee(int id, int syndicateId, String name, String adress,
			PaymentMethod paymentMethod, Calendar admissionDate,
			Calendar lastPayDate, Calendar nextPayDate, boolean isOnSyndicate,
			double monthlyFee, double deduction, double hourPrice,
			List<TimeCard> timeCards) {
		super(id, syndicateId, name, adress, paymentMethod, admissionDate,
				lastPayDate, nextPayDate, isOnSyndicate, monthlyFee, deduction);
		this.hourPrice = hourPrice;
		this.timeCards = timeCards;
	}

	public HourlyEmplyee(String name, String adress,
			PaymentMethod paymentMethod, Calendar admissionDate,
			double hourPrice) {
		super(name, adress, paymentMethod, admissionDate);
		this.hourPrice = hourPrice;

		int daysToFriday = Calendar.FRIDAY
				- admissionDate.get(Calendar.DAY_OF_WEEK);

		Calendar nextPayDate = Calendar.getInstance();

		if (daysToFriday > 0) {
			nextPayDate.add(Calendar.DAY_OF_WEEK, daysToFriday);
		} else
			nextPayDate.add(Calendar.DAY_OF_WEEK, 7);
		super.setNextPayDate(nextPayDate);
	}

	public HourlyEmplyee(String name, String adress, double hourPrice) {
		super(name, adress);
		this.hourPrice = hourPrice;

	}

	public double getHourPrice() {
		return hourPrice;
	}

	public void setHourPrice(double hourPrice) {
		this.hourPrice = hourPrice;
	}

	public List<TimeCard> getTimeCards() {
		return timeCards;
	}

	public void setTimeCards(List<TimeCard> timeCards) {
		this.timeCards = timeCards;
	}

	public void addTimeCard(TimeCard timeCard) {
		timeCards.add(timeCard);
	}

	@Override
	public double getLiquidSalary() {

		super.getLastPayDate();

		// Date date = super.getLastPayDate().getTime();
		// date.setDate(super.getLastPayDate().FRIDAY);
		// super.setNextPayDate(nextPay);
		double liquidSalary = 0;

		for (TimeCard timeCard : this.timeCards) {
			if (timeCard.getCheckin().getTimeInMillis() > this.getLastPayDate()
					.getTimeInMillis()
					&& timeCard.getCheckin().getTimeInMillis() < this
							.getNextPayDate().getTimeInMillis()) {
				if (timeCard.getWorkHours() <= 8) {
					liquidSalary += (timeCard.getWorkHours() * this
							.getHourPrice());
				} else {
					liquidSalary += (8 * this.getHourPrice())
							+ ((timeCard.getWorkHours() - 8) * (this
									.getHourPrice() * 1.5));
				}
			}
		}

		if (this.isOnSyndicate()) {
			liquidSalary += -super.getDeduction() - super.getMonthlyFee();
		}

		return liquidSalary;
	}

	@Override
	public void pay() {

		this.setLastPayDate(this.getNextPayDate());
		Calendar nextPayDate = this.getNextPayDate();
		nextPayDate.add(Calendar.DAY_OF_WEEK, 7);
		this.setNextPayDate(nextPayDate);
		this.setDeduction(0);

	}

	public HourlyEmplyee clone() {
		HourlyEmplyee cloned;

		List<TimeCard> clonedTimeCards = new ArrayList<TimeCard>();

		for (TimeCard timeCard : this.getTimeCards()) {
			clonedTimeCards.add(timeCard.clone());
		}

		cloned = new HourlyEmplyee(this.getId(), this.getSyndicateId(),
				this.getName(), this.getAdress(), this.getPaymentMethod(),
				(Calendar) this.getAdmissionDate().clone(), (Calendar) this
						.getLastPayDate().clone(), (Calendar) this
						.getNextPayDate().clone(), this.isOnSyndicate(),
				this.getMonthlyFee(), this.getDeduction(), this.getHourPrice(),
				clonedTimeCards);
		return cloned;
	}
}
