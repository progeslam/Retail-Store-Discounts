package com.retail.storediscount.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;

import org.springframework.stereotype.Service;

import com.retail.storediscount.model.Discount;
import com.retail.storediscount.model.Item;
import com.retail.storediscount.model.Item.ItemType;
import com.retail.storediscount.model.User;
import com.retail.storediscount.model.User.UserType;

@Service
public class DiscountService {

	private static final int YEARS_FOR_DISCOUNT = 2;
	private static final double EMPLOYEE_DISCOUNT_PERCENTAGE = 0.30;
	private static final double AFFILIATE_DISCOUNT_PERCENTAGE = 0.10;
	private static final double CUSTOMER_DISCOUNT_PERCENTAGE = 0.05;

	public BigDecimal discountCalculation(Discount discount) {

		BigDecimal totalAmount = getTotalBill(discount.getBill().getItems());
		BigDecimal groceryAmount = getTotalBillPerType(discount.getBill().getItems(), ItemType.GROCERY);
		BigDecimal nonGroceryAmount = totalAmount.subtract(groceryAmount);
		double userDiscount = getUserDiscount(discount.getUser()).doubleValue();
		
		if (nonGroceryAmount.compareTo(BigDecimal.ZERO) > 0) {
			nonGroceryAmount = nonGroceryAmount.multiply(BigDecimal.valueOf(1 - userDiscount));
		}
		//For every $100 on the bill, there would be a $ 5 discount 
		BigDecimal billsDiscount = BigDecimal.ZERO;
		if(totalAmount.intValue() > 100) {
			billsDiscount =  totalAmount.multiply(BigDecimal.valueOf(CUSTOMER_DISCOUNT_PERCENTAGE));
		}
		return groceryAmount.add(nonGroceryAmount).subtract(billsDiscount);
	}

	public BigDecimal getTotalBill(List<Item> items) {
		return items.stream().map(Item::getPrice).reduce(BigDecimal.ZERO, BigDecimal::add);
	}

	public BigDecimal getTotalBillPerType(List<Item> items, ItemType type) {
		BigDecimal sum = new BigDecimal(0);

		if (type != null) {
			sum = items.stream().filter(i -> type.equals(i.getType())).map(Item::getPrice).reduce(BigDecimal.ZERO,
					BigDecimal::add);
		}

		return sum;
	}

	public BigDecimal getUserDiscount(User user) {
		if (user == null) {
			throw new NullPointerException("User cannot be null");
		}

		BigDecimal discount = new BigDecimal(0);

		UserType type = user.getType();

		switch (type) {
		case EMPLOYEE:
			discount = BigDecimal.valueOf(EMPLOYEE_DISCOUNT_PERCENTAGE).setScale(2, RoundingMode.HALF_EVEN);
			break;

		case AFFILIATE:
			discount = BigDecimal.valueOf(AFFILIATE_DISCOUNT_PERCENTAGE).setScale(2, RoundingMode.HALF_EVEN);
			break;

		case REGULAR:
			if (isCustomerSince(user.getJoinDate(), YEARS_FOR_DISCOUNT)) {
				discount = BigDecimal.valueOf(CUSTOMER_DISCOUNT_PERCENTAGE).setScale(2, RoundingMode.HALF_EVEN);
			}
			break;
		}

		return discount;
	}

	public boolean isCustomerSince(LocalDate joinDate, long years) {
		Period period = Period.between(joinDate, LocalDate.now());
		return period.getYears() >= years;
	}

}
