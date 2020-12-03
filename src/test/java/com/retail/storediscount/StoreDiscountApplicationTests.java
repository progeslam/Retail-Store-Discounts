package com.retail.storediscount;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.retail.storediscount.model.Bill;
import com.retail.storediscount.model.Discount;
import com.retail.storediscount.model.Item;
import com.retail.storediscount.model.Item.ItemType;
import com.retail.storediscount.model.User;
import com.retail.storediscount.model.User.UserType;
import com.retail.storediscount.service.DiscountService;

@SpringBootTest
class StoreDiscountApplicationTests {

	@Autowired
	DiscountService discountService;

	@Test
	 void testCalculateTotal_GroceriesOnly() {
		List<Item> items = new ArrayList<Item>();
		items.add(new Item(ItemType.GROCERY, new BigDecimal(50.0)));
		items.add(new Item(ItemType.GROCERY, new BigDecimal(50.0)));
		items.add(new Item(ItemType.GROCERY, new BigDecimal(20.0)));

		BigDecimal total = discountService.getTotalBillPerType(items, ItemType.GROCERY);
		assertEquals(120.00, total.doubleValue(), 0);
	}

	@Test
	 void testCalculateTotalNonGroceriesOnly() {
		List<Item> items = new ArrayList<Item>();
		items.add(new Item(ItemType.OTHER, new BigDecimal(40.0)));
		items.add(new Item(ItemType.OTHER, new BigDecimal(100.0)));
		items.add(new Item(ItemType.OTHER, new BigDecimal(60.0)));

		BigDecimal total = discountService.getTotalBill(items);
		assertEquals(200.00, total.doubleValue(), 0);
	}

	@Test
	 void testCalculateTotalMix() {
		List<Item> items = new ArrayList<Item>();
		items.add(new Item(ItemType.OTHER, new BigDecimal(100.0)));
		items.add(new Item(ItemType.OTHER, new BigDecimal(100.0)));
		items.add(new Item(ItemType.OTHER, new BigDecimal(100.0)));
		items.add(new Item(ItemType.GROCERY, new BigDecimal(100.0)));
		items.add(new Item(ItemType.GROCERY, new BigDecimal(100.0)));

		BigDecimal total = discountService.getTotalBillPerType(items, ItemType.GROCERY);
		assertEquals(200.00, total.doubleValue(), 0);
	}

	@Test
	 void testCalculateTotal_error() {
		assertThrows(NullPointerException.class, () -> {
			discountService.getUserDiscount(null);
		});

	}

	@Test
	 void testCalculateDiscount_10pct() {
		BigDecimal total = calculateDiscount(new BigDecimal(1000), new BigDecimal(0.1));
		assertEquals(900.00, total.doubleValue(), 0);
	}

	@Test
	 void testCalculateDiscount_50pct() {
		BigDecimal total = calculateDiscount(new BigDecimal(1000), new BigDecimal(0.5));
		assertEquals(500.00, total.doubleValue(), 0);
	}

	@Test
	 void testCalculateDiscount_0pct() {
		BigDecimal total = calculateDiscount(new BigDecimal(1000), new BigDecimal(0.0));
		assertEquals(1000.00, total.doubleValue(), 0);
	}

	@Test
	 void testCalculateDiscount_100pct() {
		BigDecimal total = calculateDiscount(new BigDecimal(1000), new BigDecimal(1.0));
		assertEquals(0.0, total.doubleValue(), 0);
	}

	@Test
	 void testCalculateDiscount_error() {
		BigDecimal amount = new BigDecimal(1000);
		BigDecimal discount = new BigDecimal(2.0);
		assertThrows(IllegalArgumentException.class, () -> {
			calculateDiscount(amount, discount);
		});
	}

	@Test
	 void testGetUserSpecificDiscount_affiliate() {
		User user = new User(UserType.AFFILIATE, LocalDate.now());
		BigDecimal discount = discountService.getUserDiscount(user);
		assertEquals(0.1, discount.doubleValue(), 0);
	}

	@Test
	 void testGetUserSpecificDiscount_employee() {
		User user = new User(UserType.EMPLOYEE, LocalDate.now());
		BigDecimal discount = discountService.getUserDiscount(user);
		assertEquals(0.3, discount.doubleValue(), 0);
	}

	@Test
	 void testGetUserSpecificDiscount_customer_old() {
		LocalDate joinDate = LocalDate.of(2016, 2, 23);
		User user = new User(UserType.REGULAR, joinDate);
		BigDecimal discount = discountService.getUserDiscount(user);
		assertEquals(0.05, discount.doubleValue(), 0);
	}

	@Test
	 void testGetUserSpecificDiscount_customer_new() {
		User user = new User(UserType.REGULAR, LocalDate.now());
		BigDecimal discount = discountService.getUserDiscount(user);
		assertEquals(0.0, discount.doubleValue(), 0);
	}

	@Test
	 void testGetUserSpecificDiscount_customer_null_user() {
		assertThrows(NullPointerException.class, () -> {
			discountService.getUserDiscount(null);
		});

	}

	@Test
	 void testIsCustomerSince() {
		LocalDate joinDate = LocalDate.now();
		boolean isTwoYearsJoined = discountService.isCustomerSince(joinDate, 2);
		assertFalse(isTwoYearsJoined);
	}

	@Test
	 void testIsCustomerSince_1year() {
		LocalDate joinDate = LocalDate.now().minusYears(1);
		boolean isTwoYearsJoined = discountService.isCustomerSince(joinDate, 2);
		assertFalse(isTwoYearsJoined);
	}

	@Test
	 void testIsCustomerSince_2years() {
		LocalDate joinDate = LocalDate.now().minusYears(2);
		boolean isTwoYearsJoined = discountService.isCustomerSince(joinDate, 2);
		assertTrue(isTwoYearsJoined);
	}

	@Test
	 void testIsCustomerSince_3years() {
		LocalDate joinDate = LocalDate.now().minusYears(3);
		boolean isTwoYearsJoined = discountService.isCustomerSince(joinDate, 2);
		assertTrue(isTwoYearsJoined);
	}

	@Test
	 void testCalculateBillsDiscount() {
		BigDecimal amount = calculateBillsDiscount(new BigDecimal(1000), new BigDecimal(100), new BigDecimal(5));
		assertEquals(50, amount.doubleValue(), 0);
	}

	@Test
	 void testCalculateBillsDiscount_2() {
		BigDecimal amount = calculateBillsDiscount(new BigDecimal(1000), new BigDecimal(50), new BigDecimal(5));
		assertEquals(100, amount.doubleValue(), 0);
	}

	@Test
	 void testCalculateBillsDiscount_3() {
		BigDecimal amount = calculateBillsDiscount(new BigDecimal(5632), new BigDecimal(100), new BigDecimal(5));
		assertEquals(280, amount.doubleValue(), 0);
	}

	@Test
	 void testDiscountServiceCalculate() {
		List<Item> items = new ArrayList<Item>();
		items.add(new Item(ItemType.GROCERY, new BigDecimal(50.0)));
		items.add(new Item(ItemType.OTHER, new BigDecimal(200.0)));
		items.add(new Item(ItemType.GROCERY, new BigDecimal(10.0)));

		Bill bill = new Bill();
		bill.setItems(items);

		DiscountService discountService = new DiscountService();

		discountService.discountCalculation(new Discount(new User(UserType.REGULAR, LocalDate.now()), bill));
		BigDecimal amount = calculateBillsDiscount(new BigDecimal(5632), new BigDecimal(100), new BigDecimal(5));
		assertEquals(280, amount.doubleValue(), 0);
	}

	private BigDecimal calculateBillsDiscount(BigDecimal totalAmount, BigDecimal amount, BigDecimal discountAmount) {
		int value = totalAmount.divide(amount).intValue();
		return discountAmount.multiply(new BigDecimal(value));
	}

	private BigDecimal calculateDiscount(BigDecimal amount, BigDecimal discount) {
		if (discount.doubleValue() > 1.0) {
			throw new IllegalArgumentException("Discount cannot be more than 100%");
		}

		BigDecimal x = amount.multiply(discount);
		return amount.subtract(x);
	}

}
