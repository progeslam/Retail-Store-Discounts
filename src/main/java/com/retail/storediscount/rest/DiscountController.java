package com.retail.storediscount.rest;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.retail.storediscount.model.Discount;
import com.retail.storediscount.service.DiscountService;

@RestController
@RequestMapping("api/discount")
public class DiscountController {

    @Autowired
    private DiscountService discountService;

    @PostMapping
    public BigDecimal getDiscount(@RequestBody Discount discount) {
        return discountService.discountCalculation(discount);
    }

}
