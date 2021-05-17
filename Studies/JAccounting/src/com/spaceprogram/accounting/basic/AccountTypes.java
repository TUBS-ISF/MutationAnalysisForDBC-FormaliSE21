/**
 * @author Travis Reeder - travis@spaceprogram.com
 * @version 0.1
 * Date: Sep 5, 2002
 * Time: 2:09:58 PM
 * 
 */
package com.spaceprogram.accounting.basic;

/*@nullable_by_default@*/
public class AccountTypes {

     /**
     * Asset
     * Liability
     * Expense
     * Sales
     * Equity
     * Non-current Asset
     * Non-current Liability
     */

     /**
      * cash, bank accounts, etc
      */
    public static final int BANK = 1;
    /**
     * money owed by clients for products/sevices
     *
     *  most businesses only need one
     */
    public static final int ACCOUNTS_RECEIVABLE = 2;
    /**
     * Liquid within a year that aren't bank or A/R
     * - allowance for bad debt
     * - development costs
     * - employee advances
     * - inventory
     * - investments
     * - loans
     * - prepaid expenses
     * - retainage
     * - undeposited funds
     */
    public static final int OTHER_CURRENT_ASSETS = 3;
    /**
     * not liquid within  a year
     *
     * - accumulated depreciation, amortization, or depletion
     * - buildings
     * - depletable assets
     * - furniture and fixtures
     * - intangible assets
     * - land
     * - leasehold improvements
     * - machinery and equipment
     * - vehicles
     *
     */
    public static final int NON_CURRENT_ASSETS = 8;
    /**
     * just another for above
     * @see #NON_CURRENT_ASSETS
     */
    public static final int FIXED_ASSETS = NON_CURRENT_ASSETS;
    /**
     * Assets that are neither current nor fixed assets
     *
     * - Accumulated amortization of other assets
     * - goodwill
     * - lease buyout
     * - licenses
     * - org. costs
     * - security deposits
     *
     */
    public static final int OTHER_ASSETS = 9;




    public static final int ACCOUNTS_PAYABLE = 21;
    public static final int CREDIT_CARD = 22;
    /**
     * to be paid within a year, other than A/P and credit cards
     */
    public static final int OTHER_CURRENT_LIABILITY= 23;
    /**
     * paid more than a year
     */
    public static final int LONG_TERM_LIABILITY = 28;

    /**
     * track owner's equity, owner's draws, capital invesment, stock, and reatinged earnings
     */
    public static final int EQUITY = 51;

    /**
     * track main sources of money coming into your company from normal operations
     *
     * - Discounts/refunds given
     * - nonprofit income
     * - other primary income
     * - sales of product income
     * - service/fee income
     */
    public static final int INCOME = 61;
    /**
     * received from other than normal business operations
     *
     * - dividend income
     * - interest earned
     * - other investment income
     * - other misc income
     * - tax-exempt interest
     */
    public static final int OTHER_INCOME = 62;


    /**
     * Costs of Goods Sold (COGS)
     * track cost of resources used to sell products/services
     *
     * Mostly used if tracking inventory, otherwise, use expense account
     *
     * - cost of labor
     * - equipment rental
     * - shipping, freight & delivery
     * - supplies and materials
     */
    public static final int COGS = 71;


    /**
     * how/where company spends money for normal operations
     * too numerous to list
     */
    public static final int EXPENSE = 81;

    /**
     * money spent on things other than normal business operating expenses
     *
     * - amortization
     * - depreciation
     * - penalties and settlements
     */
     public static final int OTHER_EXPENSE = 82;



}
