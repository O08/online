package com.et.be.util;

/**
 * 税收工具
 */
public class TaxUtil {
    public static Double calculateTax(){
        return 200.00;
    }

    /**
     * 综合税 13%
     * @return
     */
    public static Double averageTax(Double price){
        return price* 0.13;
    }
}
