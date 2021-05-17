/**
 * todo: Most of the stuff in here should be converted to using Hibernate if it hasn't been done already
 *
 * @author Travis Reeder - travis@spaceprogram.com
 * @version 0.1
 * Date: Sep 4, 2002
 * Time: 1:06:27 PM
 *
 */
package com.spaceprogram.accounting.datastore;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import net.sf.hibernate.Hibernate;
import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;
import net.sf.hibernate.type.Type;

import com.crossdb.sql.ColumnValue;
import com.crossdb.sql.CrossdbResultSet;
import com.crossdb.sql.InsertQuery;
import com.crossdb.sql.SQLFactory;
import com.crossdb.sql.SelectQuery;
import com.crossdb.sql.UpdateQuery;
import com.crossdb.sql.WhereCondition;
import com.spaceprogram.accounting.basic.AClass;
import com.spaceprogram.accounting.basic.Account;
import com.spaceprogram.accounting.basic.AccountType;
import com.spaceprogram.accounting.basic.Charge;
import com.spaceprogram.accounting.basic.Customer;
import com.spaceprogram.accounting.basic.Invoice;
import com.spaceprogram.accounting.basic.Product;
import com.spaceprogram.accounting.basic.Tax;
import com.spaceprogram.accounting.common.Recurrence;
import com.spaceprogram.accounting.setup.DefaultAccountSetup;
import com.spaceprogram.accounting.setup.Preferences;

/*@nullable_by_default@*/
public class AccountingPersistenceManager {
	public static String tablePrefix = "ACCT"; // prefix for accounting tables

	/** for internal id to find accounts */
	public static final int ACCOUNT_INTERNAL_OPENING_BALANCE_EQUITY = 101;
	public static final int ACCOUNT_INTERNAL_TAX_PAYABLE = 501;
	public static final int ACCOUNT_INTERNAL_TAX_RECEIVABLE = 502;
	public static final int ACCOUNT_INTERNAL_ACCOUNTS_RECEIVABLE = 1001;
	public static final int ACCOUNT_INTERNAL_ACCOUNTS_PAYABLE = 1002;


	public static int APP_ID = 6676;
	public static int APP_ID_CHARGE = 6677;
	public static final int CUSTOMER = 1;
	public static final int VENDOR = 2;


	/*public static JournalEntry getJournalEntry(com.crossdb.sql.SQLFactory factory, Connection conn, int id) throws SQLException {
        JournalEntry ret = null;
        if (id == 0) {
            // then return empty entry
            ret = JournalEntry.getDefaultJournalEntry();
            //ret.setEntryDate(new Date());

        } else {
            SelectQuery sq = factory.getSelectQuery();
            sq.addTable(tablePrefix + "JournalEntry");
            sq.addWhereCondition("id", WhereCondition.EQUAL_TO, id);
            CrossdbResultSet rs = sq.execute(conn);
            if (rs.next()) {
                ret = new JournalEntry();
                ret.setId(rs.getInt("id"));
                ret.setEntryDate(rs.getDateTime("entryDate"));
                ret.setEntryNumber(rs.getInt("entryNumber"));
                ret.setMemo(rs.getString("memo"));
                ret.setCompanyKey(rs.getInt("companyKey"));

                // now get transactions
                ret.setTransactions(AccountingPersistenceManager.getTransactionsByJournalEntry(factory, conn, ret.getId()));


            }
            rs.close();

        }
        return ret;

    }
	 */
	/*private static Collection getTransactionsByJournalEntry(SQLFactory factory, Connection conn, int journalEntryKey) throws SQLException {
        List ret = new ArrayList();
        SelectQuery sq = factory.getSelectQuery();
        sq.addTable(tablePrefix + "Transaction");
        sq.addWhereCondition("journalEntryKey", WhereCondition.EQUAL_TO, journalEntryKey);
        CrossdbResultSet rs2 = sq.execute(conn);
        while (rs2.next()) {
            Transaction trans = new Transaction();
            trans.setId(rs2.getInt("id"));
            trans.setCompanyKey(rs2.getInt("companyKey"));
            trans.setAccountKey(rs2.getInt("accountKey"));
            trans.setClassKey(rs2.getInt("classKey"));
            trans.setCustomerKey(rs2.getInt("customerKey"));
            trans.setJournalEntryKey(rs2.getInt("journalEntryKey"));
            trans.setMemo(rs2.getString("memo"));
            //trans.setTaxable(rs2.getBoolean("taxable"));
            trans.setTransactionDate(rs2.getTimestamp("transactionDate"));
            ret.add(trans);
        }
        rs2.close();
        return ret;
    }
	 */
	
	//@ pre factory != null;
	//@ post true;
	//@ signals_only SQLException;
	public static CrossdbResultSet getAccountsResultSet(SQLFactory factory, Connection conn, int cid) throws SQLException {

		SelectQuery sq = factory.getSelectQuery();
		sq.addTable(tablePrefix + "Account");
		sq.addWhereCondition("companyKey", WhereCondition.EQUAL_TO, cid);
		return sq.execute(conn);

	}

	/*public static List getAccounts(SQLFactory factory, Connection conn, int cid) throws SQLException {
        List accounts = new ArrayList();
        CrossdbResultSet rs = AccountingPersistenceManager.getAccountsResultSet(factory, conn, cid);
        while (rs.next()) {
            accounts.add(new Account(rs.getInt("id"), rs.getInt("companyKey"), rs.getDateTime("created"), rs.getString("name"), rs.getString("description"), rs.getInt("parentKey"), rs.getInt("typeKey"), rs.getInt("detailTypeKey"), rs.getBigDecimal("balance")));
        }
        rs.close();
        return accounts;
    }*/


	//@ pre factory != null;
	//@ pre cid >= 0;
	//@ post true;
	//@ signals_only SQLException;
	public static List getClasses(SQLFactory factory, Connection conn, int cid) throws SQLException {
		List classes = new ArrayList();
		CrossdbResultSet rs = AccountingPersistenceManager.getClassesResultSet(factory, conn, cid);
		while (rs.next()) {
			classes.add(new AClass(rs.getInt("id"), rs.getInt("companyKey"), rs.getString("name")));
		}
		rs.close();
		return classes;
	}

	//@ pre true;
	//@ post factory != null;
	//@ signals_only SQLException;
	private static CrossdbResultSet getClassesResultSet(SQLFactory factory, Connection conn, int cid) throws SQLException {
		SelectQuery sq = factory.getSelectQuery();
		sq.addTable(tablePrefix + "AClass");
		sq.addWhereCondition("companyKey", WhereCondition.EQUAL_TO, cid);
		return sq.execute(conn);
	}

	//@ pre sess != null;
	//@ post true;
	//@ signals_only HibernateException;
	public static List getCustomers(Session sess, Integer cid) throws HibernateException {
		List custs = sess.find("from " + Customer.class.getName() + " cust where cust.companyKey = ? ",
				cid,
				Hibernate.INTEGER); //new ArrayList();
		/*CrossdbResultSet rs = AccountingPersistenceManager.getCustomersResultSet(factory, conn, cid, 1);
        while (rs.next()) {
            //custs.add(new AccountingCustomer(rs.getInt("id"), cid, rs.getInt("customerKey"), rs.getString("customer_name")));
             custs.add(new Customer(rs.getInt("customer_id"), rs.getString("customer_name")));
        }
        rs.close();*/
		return custs;
	}

	/* public static List getVendors(SQLFactory factory, Connection conn, int cid) throws SQLException {
        List classes = new ArrayList();
        CrossdbResultSet rs = AccountingPersistenceManager.getCustomersResultSet(factory, conn, cid, 2);
        while (rs.next()) {
            //classes.add(new AccountingCustomer(rs.getInt("id"), cid, rs.getInt("customerKey"), rs.getString("customer_name")));
            classes.add(new Customer(rs.getInt("customer_id"), rs.getString("customer_name")));
        }
        rs.close();
        return classes;
    }
	 */
	//@ pre factory != null;
	//@ post true;
	//@ signals_only SQLException;
	private static CrossdbResultSet getCustomersResultSet(SQLFactory factory, Connection conn, int cid, int type) throws SQLException {
		String table1 = tablePrefix + "Customer";
		String table2 = "Customers";
		SelectQuery sq = factory.getSelectQuery();
		sq.addTable(table1);
		sq.addTable(table2);
		sq.addWhereCondition("customer_company_key", WhereCondition.EQUAL_TO, cid);
		sq.addWhereCondition(table1 + ".customerKey", WhereCondition.EQUAL_TO, table2 + ".customer_id");
		sq.addWhereCondition("ACCTCustomer.type", WhereCondition.EQUAL_TO, type);
		return sq.execute(conn);
	}

	//@ pre factory != null;
	//@ post true;
	//@ signals_only SQLException;
	public static List getAccountTypes(SQLFactory factory, Connection conn, int cid) throws SQLException {
		List classes = new ArrayList();
		CrossdbResultSet rs = AccountingPersistenceManager.getAccountTypesResultSet(factory, conn, cid);
		while (rs.next()) {
			classes.add(new AccountType(rs.getInt("id"), rs.getString("name"), rs.getString("description")));
		}
		rs.close();
		return classes;
	}

	//@ pre factory != null;
	//@ post true;
	//@ signals_only SQLException;
	private static CrossdbResultSet getAccountTypesResultSet(SQLFactory factory, Connection conn, int cid) throws SQLException {
		SelectQuery sq = factory.getSelectQuery();
		sq.addTable(tablePrefix + "AccountType");
		//sq.addWhereCondition("companyKey", WhereCondition.EQUAL_TO, cid);
		return sq.execute(conn);
	}

	/**
	 * @deprecated using hibernate for new stuff
	 * 
	 * @param factory
	 * @param conn
	 * @param cid
	 * @param account
	 * @throws SQLException
	 */
	//@ pre factory != null;
	//@ post true;
	//@ signals_only SQLException;
	public static void saveAccount(SQLFactory factory, Connection conn, int cid, Account account) throws SQLException {
		List columns = new ArrayList();

//		set the column list in the query after
		columns.add(new ColumnValue("name", account.getName()));

		columns.add(new ColumnValue("description", account.getDescription()));
		columns.add(new ColumnValue("typeKey", account.getTypeKey()));
		columns.add(new ColumnValue("parentKey", account.getParentKey()));

		/*if (account.getId() > 0) {

            UpdateQuery uq = factory.getUpdateQuery();
            uq.setTable(tablePrefix + "Account");
            uq.addWhereCondition("id", WhereCondition.EQUAL_TO, account.getId());
            uq.appendColumns(columns);

            uq.execute(conn);

        } else {

            InsertQuery iq = factory.getInsertQuery();
            iq.setTable(tablePrefix + "Account");
            iq.addAutoIncrementColumn("id");
            iq.addColumn("companyKey", cid);
            iq.addColumn("balance", account.getBalance());
            iq.addColumn("internalId", account.getInternalId());
            iq.returnID(true);
            iq.appendColumns(columns);
            account.setId(iq.execute(conn));
        }
		 */

	}

	//@ pre factory != null;
	//@ post true;
	//@ signals_only SQLException;
	public static Account getAccount(SQLFactory factory, Connection conn, int id) throws SQLException {
		Account ret = null;
		if (id == 0) {
			// then return empty entry
			ret = Account.getDefault();
			//ret.setEntryDate(new Date());

		} else {
			/* SelectQuery sq = factory.getSelectQuery();
            sq.addTable(tablePrefix + "Account");
            sq.addWhereCondition("id", WhereCondition.EQUAL_TO, id);
            CrossdbResultSet rs = sq.execute(conn);
            if (rs.next()) {
                ret = new Account(rs.getInt("id"), rs.getInt("companyKey"), rs.getDateTime("created"),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getInt("parentKey"),
                        rs.getInt("typeKey"),
                        rs.getInt("detailTypeKey"),
                        rs.getBigDecimal("balance"));


            }
            rs.close();*/

		}
		return ret;
	}

	/** get an account for the company based on an internal id, mostly for premade accounts
	 * ex: Opening Balance Equity
	 */
	//@ pre true;
	//@ post true;
	//@ signals (Exception) true; 
	public static Account getAcountByInternalId(Session sess, Integer cid, int internalId) throws  Exception {
		Account ret = null;
		if (internalId != 0) {


			List x =   sess.find("from Account account where account.companyKey = ? and account.internalId = ?",
					new Object[]{ cid, new Integer(internalId)},
					new Type[]{Hibernate.INTEGER, Hibernate.INTEGER}
			);
			if(x.size() > 0){
				ret = (Account) x.get(0);

			} else {
				// create internal account
				ret = DefaultAccountSetup.createInternalAccount(sess, cid, internalId);

			}

		}
		return ret;


	}

	/**
	 * will persist a deposit
	 * @param factory
	 * @param conn
	 * @param cid
	 * @param deposit
	 */
	/*public static void saveDeposit(SQLFactory factory, Connection conn, int cid, int uid, Deposit deposit) throws SQLException {

        // save journal entry first
        JournalEntry je = deposit.getJournalEntry();
        saveJournalEntry(factory, conn, cid, uid, je);

        if (deposit.getId() > 0) {
            // IS THERE ANYTYHING TO DO ON AN UPDATE OTHER THAN THE JOURNAL ENTRY?




        } else {


            // now insert deposit
            InsertQuery iq = factory.getInsertQuery();
            iq.setTable(tablePrefix + "Deposit");
            iq.addAutoIncrementColumn("id");
            iq.addColumn("journalEntryKey", je.getId());
            iq.returnID(true);
            deposit.setId(iq.execute(conn));


        }
    }*/

	/*public static void saveJournalEntry(SQLFactory factory, Connection conn, int cid, int uid, JournalEntry je) throws SQLException {
        List columns = new ArrayList();

//        set the column list in the query after
        columns.add(new ColumnValue("entryDate", je.getEntryDate()));
        columns.add(new ColumnValue("updated", new Date()));

        columns.add(new ColumnValue("entryNumber", je.getEntryNumber()));
        columns.add(new ColumnValue("memo", je.getMemo()));


        if (je.getId() > 0) {
            UpdateQuery uq = factory.getUpdateQuery();
            uq.setTable(tablePrefix + "JournalEntry");
            uq.addWhereCondition("id", WhereCondition.EQUAL_TO, je.getId());

            uq.appendColumns(columns);
            uq.execute(conn);
        } else {
            columns.add(new ColumnValue("companyKey", cid));

            InsertQuery iq = factory.getInsertQuery();
            iq.setTable(tablePrefix + "JournalEntry");
            iq.addAutoIncrementColumn("id");
            iq.appendColumns(columns);
            iq.returnID(true);
            je.setId(iq.execute(conn));
        }

        // now save transactions
        Collection transactions = je.getTransactions();
        Iterator iter = transactions.iterator();
        //for (int i = 0; i < transactions.size(); i++) {
        while(iter.hasNext()){
            Transaction transaction = (Transaction) iter.next(); //transactions.get(i);
            transaction.setJournalEntryKey(je.getId());
            saveTransaction(factory, conn, cid, transaction);

        }

    }*/

	/**
	 * Save transaction to db

	 */
	/* private static void saveTransaction(SQLFactory factory, Connection conn, int cid, Transaction transaction) throws SQLException {
        List columns = new ArrayList();
        columns.add(new ColumnValue("accountKey", transaction.getAccountKey()));
        columns.add(new ColumnValue("amount", transaction.getAmount()));
        columns.add(new ColumnValue("memo", transaction.getMemo()));
        columns.add(new ColumnValue("customerKey", transaction.getCustomerKey()));
        columns.add(new ColumnValue("classKey", transaction.getClassKey()));
        columns.add(new ColumnValue("transactionDate", transaction.getTransactionDate()));
        columns.add(new ColumnValue("currency", transaction.getCurrency()));

        if (transaction.getId() > 0) {
            UpdateQuery uq = factory.getUpdateQuery();
            uq.setTable(tablePrefix + "Transaction");
            uq.addWhereCondition("id", WhereCondition.EQUAL_TO, transaction.getId());
            uq.appendColumns(columns);
            System.out.println(uq);
            uq.execute(conn);

        } else {
            columns.add(new ColumnValue("companyKey", cid));
            columns.add(new ColumnValue("journalEntryKey", transaction.getJournalEntryKey()));
            InsertQuery iq = factory.getInsertQuery();
            iq.setTable(tablePrefix + "Transaction");
            iq.addAutoIncrementColumn("id");
            iq.returnID(true);
            iq.appendColumns(columns);
            transaction.setId(iq.execute(conn));

        }

    }*/

	/* public static Invoice getInvoice(SQLFactory factory, Connection conn, int cid, int uid, int id) throws SQLException {
        Invoice ret = null;
        if (id == 0) {
            // then return empty entry
            ret = Invoice.getDefault();
            ret.setCustomerMessage("Thank you for your business!");
            //ret.setEntryDate(new Date());

        } else {
            SelectQuery sq = factory.getSelectQuery();
            sq.addTable(tablePrefix + "Invoice");
            sq.addWhereCondition("id", WhereCondition.EQUAL_TO, id);
            CrossdbResultSet rs = sq.execute(conn);
            if (rs.next()) {
                ret = new Invoice();
                ret.setId(rs.getInt("id"));
                ret.setCreated(rs.getDateTime("created"));
                ret.setUpdated(rs.getDateTime("updated"));
                ret.setCompanyKey(rs.getInt("companyKey"));
                ret.setInvoiceDate(rs.getDateTime("invoiceDate"));
                ret.setDueDate(rs.getDateTime("dueDate"));
                ret.setInvoiceNumber(rs.getInt("invoiceNumber"));
                ret.setCustomerMessage(rs.getString("customerMessage"));
                ret.setSendToEmailKey(rs.getInt("sendToEmailKey"));
                ret.setToBeSent(rs.getBoolean("toBeSent"));
                ret.setToBePrinted(rs.getBoolean("toBePrinted"));
                ret.setBillTo(rs.getString("billTo"));
                ret.setCustomerKey(rs.getInt("customerKey"));
                ret.setTemplateKey(rs.getInt("templateKey"));
                // ret.setMemo(rs.getString("memo"));

                // get Charges
                sq = factory.getSelectQuery();
                sq.addTable(tablePrefix + "Charge");
                sq.addWhereCondition("invoiceKey", WhereCondition.EQUAL_TO, ret.getId());
                CrossdbResultSet rs2 = sq.execute(conn);
                while(rs2.next()) {
                    Charge charge = getChargeByResultSet(factory, conn, cid, uid, rs2);
                    ret.addCharge(charge);


                }
                //ret.setCharges(getChargesByResultSet(factory, conn, rs2));
                rs2.close();


                JournalEntry je = getJournalEntry(factory, conn, rs.getInt("journalEntryKey"));
                ret.setJournalEntry(je);

                // get taxes for invoice
                List taxes = getTaxesForInvoices(factory, conn, id);
                ret.setTaxes(taxes);


                //


            }
            rs.close();

        }
        return ret;
    }*/

	/* public static Charge getCharge(SQLFactory factory, Connection conn, int cid, int uid, int id) throws SQLException {
        Charge charge = null;
        if (id == 0) {
            charge = Charge.getDefault();
        } else {
            SelectQuery sq = factory.getSelectQuery();
            sq.addTable(tablePrefix + "Charge");
            sq.addWhereCondition("id", WhereCondition.EQUAL_TO, id);

            CrossdbResultSet rs2 = sq.execute(conn);
            if (rs2.next()) {
                charge = getChargeByResultSet(factory, conn, cid, uid, rs2);


            }
            rs2.close();

              }

        return charge;
    }
	 */
	/* private static Charge getChargeByResultSet(SQLFactory factory, Connection conn, int cid, int uid, CrossdbResultSet rs2) throws SQLException {
        Charge charge;
        charge = new Charge();
        charge.setId(rs2.getInt("id"));
        charge.setCreated(rs2.getDateTime("created"));
        charge.setUpdated(rs2.getDateTime("updated"));
        charge.setCompanyKey(rs2.getInt("companyKey"));
        charge.setCustomerKey(rs2.getInt("customerKey"));
        charge.setInvoiceKey(rs2.getInt("invoiceKey"));
        charge.setChargeDate(rs2.getDateTime("chargeDate"));
        charge.setChargeNumber(rs2.getInt("chargeNumber"));
        charge.setMemo(rs2.getString("memo"));
        charge.setCurrency(rs2.getString("currency"));


        // now get chargelines
                 getChargeLines(factory, conn, charge);

                 // now get recurrence if it exists
                charge.setRecurrence(AccountingPersistenceManager.getRecurrence(factory, conn, cid, uid, AccountingPersistenceManager.APP_ID_CHARGE, charge.getId()));



        return charge;
    }*/

	/* private static void getChargeLines(SQLFactory factory, Connection conn, Charge charge) throws SQLException {
        SelectQuery sq;
        sq = factory.getSelectQuery();
        sq.addTable(tablePrefix + "ChargeLine");
        sq.addWhereCondition("chargeKey", WhereCondition.EQUAL_TO, charge.getId());
        CrossdbResultSet rs3 = sq.execute(conn);
        while (rs3.next()) {
            ChargeLine cline = new ChargeLine();
            cline.setId(rs3.getInt("id"));
            cline.setChargeKey(rs3.getInt("chargeKey"));
            //cline.setClassKey(rs3.getInt("classKey"));
            //cline.setDescription(rs3.getString("description"));
            cline.setQuantity(rs3.getBigDecimal("quantity"));
            cline.setRate(rs3.getBigDecimal("rate"));
            cline.setTaxable(rs3.getBoolean("taxable"));

            // get produyct for chargeline
            cline.setProduct(AccountingPersistenceManager.getProduct(factory, conn, rs3.getInt("productKey")));

            // get transaction
            cline.setTransaction(AccountingPersistenceManager.getTransaction(factory, conn, rs3.getInt("transactionKey")));



            charge.addChargeLine(cline);


        }
        rs3.close();
    }*/

	//@ pre true;
	//@ post true;
	//@ signals_only HibernateException;
	public static List getChargesByCustomer(Session sess, Integer cid, Integer customerKey) throws  HibernateException {

		List ret;
		ret = sess.find("from Charge charge where charge.customerKey = ? " +
				"and (charge.invoiceKey = 0 or charge.invoiceKey is null)",
				customerKey,
				Hibernate.INTEGER);


		return ret;

	}

	/*private static Transaction getTransaction(SQLFactory factory, Connection conn, int id) throws SQLException {
        Transaction ret = null;
        SelectQuery sq = factory.getSelectQuery();
        sq.addTable(tablePrefix + "Transaction");
        sq.addWhereCondition("id", WhereCondition.EQUAL_TO, id);
        CrossdbResultSet rs2 = sq.execute(conn);
        if (rs2.next()) {
            Transaction trans = new Transaction();
            trans.setId(rs2.getInt("id"));
            trans.setCompanyKey(rs2.getInt("companyKey"));
            trans.setAccountKey(rs2.getInt("accountKey"));
            trans.setClassKey(rs2.getInt("classKey"));
            trans.setCustomerKey(rs2.getInt("customerKey"));
            trans.setJournalEntryKey(rs2.getInt("journalEntryKey"));
            trans.setMemo(rs2.getString("memo"));
            trans.setCurrency(rs2.getString("currency"));
           // trans.setTaxable(rs2.getBoolean("taxable"));
            trans.setTransactionDate(rs2.getTimestamp("transactionDate"));
            ret = trans;
        }
        rs2.close();
        return ret;
    }*/

	/*public static Product getProduct(SQLFactory factory, Connection conn, int id) throws SQLException {
        Product ret = null;

        SelectQuery sq = factory.getSelectQuery();
        sq.addTable(tablePrefix + "Product");
        sq.addWhereCondition("id", WhereCondition.EQUAL_TO, id);

        CrossdbResultSet rs2 = sq.execute(conn);
        if (rs2.next()) {
            ret = new Product();
            ret.setId(rs2.getInt("id"));
            ret.setName(rs2.getString("name"));
            ret.setDescription(rs2.getString("description"));
            ret.setAccountKey(rs2.getInt("accountKey"));
            ret.setRate(rs2.getBigDecimal("rate"));
            ret.setTaxable(rs2.getBoolean("taxable"));
        }
        rs2.close();

        return ret;
    }*/

	/**
	 *
	 * @param factory
	 * @param conn
	 * @param cid the company key

	 */
	/* public static List getProducts(SQLFactory factory, Connection conn, int cid) throws SQLException {
        List retlist = new ArrayList();

        SelectQuery sq = factory.getSelectQuery();
        sq.addTable(tablePrefix + "Product");
        sq.addWhereCondition("companyKey", WhereCondition.EQUAL_TO, cid);

        CrossdbResultSet rs2 = sq.execute(conn);
        while (rs2.next()) {
            Product ret = new Product();
            ret.setId(rs2.getInt("id"));
            ret.setName(rs2.getString("name"));
            ret.setDescription(rs2.getString("description"));
            ret.setAccountKey(rs2.getInt("accountKey"));
            ret.setRate(rs2.getBigDecimal("rate"));
            ret.setTaxable(rs2.getBoolean("taxable"));
            retlist.add(ret);
        }
        rs2.close();

        return retlist;
    }*/

	/*public static void saveProduct(SQLFactory factory, Connection conn, int cid, int uid, Product product) throws SQLException {
        List columns = new ArrayList();
        columns.add(new ColumnValue("accountKey", product.getAccountKey()));
        columns.add(new ColumnValue("name", product.getName()));
        columns.add(new ColumnValue("description", product.getDescription()));
        columns.add(new ColumnValue("rate", product.getRate()));
        columns.add(new ColumnValue("taxable", product.isTaxable()));
        columns.add(new ColumnValue("code", product.getCode()));

        if (product.getId() > 0) {
            UpdateQuery uq = factory.getUpdateQuery();
            uq.setTable(tablePrefix + "Product");
            uq.addWhereCondition("id", WhereCondition.EQUAL_TO, product.getId());
            uq.appendColumns(columns);
            uq.execute(conn);

        } else {
            columns.add(new ColumnValue("companyKey", cid));

            InsertQuery iq = factory.getInsertQuery();
            iq.setTable(tablePrefix + "Product");
            iq.addAutoIncrementColumn("id");
            iq.returnID(true);
            iq.appendColumns(columns);
            product.setId(iq.execute(conn));

        }

    }*/

	//@ pre true;
	//@ post true;
	public static void addCustomer(SQLFactory factory, Connection conn, int cid, int uid, int customer_id) throws SQLException {
		addCustomer(factory, conn, cid, uid, customer_id, 1);
	}

	//@ pre conn!= null;
	//@ post cid >= 0;
	public static void addVendor(SQLFactory factory, Connection conn, int cid, int uid, int customer_id) throws SQLException {
		addCustomer(factory, conn, cid, uid, customer_id, 2);
	}

	/**
	 * types:
	 *  1 = customer
	 *  2 = vendor
	 * @param factory
	 * @param conn
	 * @param cid
	 * @param uid
	 * @param customer_id
	 * @param type
	 */
	//@ pre factory != null;
	//@ post true;
	//@ signals_only SQLException;
	public static void addCustomer(SQLFactory factory, Connection conn, int cid, int uid, int customer_id, int type) throws SQLException {
		//TODO: should do a lookup first to see if already exists
		InsertQuery iq = factory.getInsertQuery();
		iq.setTable(tablePrefix + "customer");
		iq.addAutoIncrementColumn("id");
		iq.addColumn("companyKey", cid);
		iq.addColumn("customerKey", customer_id);
		iq.addColumn("type", type);
		iq.execute(conn);
	}

	/*public static void saveCharge(SQLFactory factory, Connection conn, int cid, int uid, Charge charge) throws SQLException {
        Date now = new Date();
        List columns = new ArrayList();
        columns.add(new ColumnValue("customerKey", charge.getCustomerKey()));
        columns.add(new ColumnValue("chargeNumber", charge.getChargeNumber()));
        columns.add(new ColumnValue("memo", charge.getMemo()));
        columns.add(new ColumnValue("chargeDate", charge.getChargeDate()));
        columns.add(new ColumnValue("updated", now));
        columns.add(new ColumnValue("invoiceKey", charge.getInvoiceKey()));
        columns.add(new ColumnValue("currency", charge.getCurrency()));


        if (charge.getId() > 0) {
            UpdateQuery uq = factory.getUpdateQuery();
            uq.setTable(tablePrefix + "Charge");
            uq.addWhereCondition("id", WhereCondition.EQUAL_TO, charge.getId());
            uq.appendColumns(columns);
            uq.execute(conn);

        } else {
            columns.add(new ColumnValue("companyKey", cid));

            InsertQuery iq = factory.getInsertQuery();
            iq.setTable(tablePrefix + "Charge");
            iq.addAutoIncrementColumn("id");
            iq.addColumn("created", now);

            iq.returnID(true);
            iq.appendColumns(columns);
            charge.setId(iq.execute(conn));


        }

        // todo: now save chargelines in charge here, already done though before hand in charges.jsp?  is this necessary?
        // could check if they have been changed or not maybe?
       Set chargeLines = charge.getChargeLines();

        saveChargeLines(factory, conn, cid, uid, charge.getId(), chargeLines);




    }
	 */

	/*private static void saveChargeLines(SQLFactory factory, Connection conn, int cid, int uid, int chargeKey, Set chargeLines) throws SQLException {
        Iterator iter = chargeLines.iterator();
//        for (int i = 0; i < chargeLines.size(); i++) {
         while(iter.hasNext()){
             ChargeLine chargeLine = (ChargeLine) iter.next();//(ChargeLine) chargeLines.get(i);
            chargeLine.setChargeKey(chargeKey);
            saveChargeLine(factory, conn, cid, uid, chargeLine);
        }
    }*/

	/*public static void saveChargeLine(SQLFactory factory, Connection conn, int cid, int uid, ChargeLine cl) throws SQLException {
        //Date now = new Date();

        // save underlying transaction first
        saveTransaction(factory, conn, cid, cl.getTransaction());

               List columns = new ArrayList();
               columns.add(new ColumnValue("chargeKey", cl.getChargeKey()));
               columns.add(new ColumnValue("transactionKey", cl.getTransaction().getId()));
               columns.add(new ColumnValue("productKey", cl.getProductKey()));
               columns.add(new ColumnValue("quantity", cl.getQuantity()));
               //columns.add(new ColumnValue("updated", now));
               columns.add(new ColumnValue("rate", cl.getRate()));
        columns.add(new ColumnValue("taxable", cl.isTaxable()));


               if (cl.getId() > 0) {
                   UpdateQuery uq = factory.getUpdateQuery();
                   uq.setTable(tablePrefix + "ChargeLine");
                   uq.addWhereCondition("id", WhereCondition.EQUAL_TO, cl.getId());
                   uq.appendColumns(columns);
                   uq.execute(conn);

               } else {
                   //columns.add(new ColumnValue("companyKey", cid));

                   InsertQuery iq = factory.getInsertQuery();
                   iq.setTable(tablePrefix + "ChargeLine");
                   iq.addAutoIncrementColumn("id");
                   //iq.addColumn("created", now);

                   iq.returnID(true);
                   iq.appendColumns(columns);
                   cl.setId(iq.execute(conn));


               }

    }
	 */
	/**
	 * delete chargeline and associated transaction
	 *
	 * @param factory
	 * @param conn
	 * @param cid
	 * @param uid
	 * @param chargeLine
	 */
	/*public static int deleteChargeLine(SQLFactory factory, Connection conn, int cid, int uid, ChargeLine chargeLine) throws SQLException {

        if(deleteTransaction(factory, conn, cid, uid, chargeLine.getTransaction()) == 0){
            return -1; // error, couldn't delete transaction
        }

         DeleteQuery dq = factory.getDeleteQuery();
        dq.setTable(tablePrefix + "ChargeLine");
         dq.addWhereCondition("id", WhereCondition.EQUAL_TO, chargeLine.getId());
        return dq.execute(conn);



    }
	 */
	/*private static int deleteTransaction(SQLFactory factory, Connection conn, int cid, int uid, Transaction transaction) throws SQLException {
        DeleteQuery dq = factory.getDeleteQuery();
        dq.setTable(tablePrefix + "Transaction");
        dq.addWhereCondition("id", WhereCondition.EQUAL_TO, transaction.getId());
        return dq.execute(conn);

    }*/

	/**
	 *
	 * 1. Save journal entry with charges going to appropriate account based on product
	 * 2. update all transactions in chargelines to point to proper journal entry
	 * 2. save invoice details
	 *
	 * @param factory
	 * @param conn
	 * @param cid
	 * @param uid
	 * @param invoice
	 * @throws SQLException
	 */
	/*public static void saveInvoice(SQLFactory factory, Connection conn, int cid, int uid, Invoice invoice) throws SQLException {
          Date now = new Date();

         // save underlying JournalEntry first

          // create A/R entry
        JournalEntry journalEntry = invoice.getJournalEntry();
        Account ARaccount = AccountingPersistenceManager.getAcountByInternalId(factory, conn, cid, AccountingPersistenceManager.ACCOUNT_INTERNAL_ACCOUNTS_RECEIVABLE);

        Transaction trans = new Transaction();
        trans.setTransactionDate(now);
        trans.setCompanyKey(cid);

        trans.setAccountKey(ARaccount.getId());
        trans.setMemo("From Invoice "+ invoice.getInvoiceNumber());
        trans.setAmount(invoice.getTotal());

        journalEntry.addTransaction(trans);
        saveJournalEntry(factory, conn, cid, uid, journalEntry);

        List columns = new ArrayList();
        columns.add(new ColumnValue("customerKey", invoice.getCustomerKey()));
        columns.add(new ColumnValue("invoiceNumber", invoice.getInvoiceNumber()));
        columns.add(new ColumnValue("customerMessage", invoice.getCustomerMessage()));
        columns.add(new ColumnValue("invoiceDate", invoice.getInvoiceDate()));
        columns.add(new ColumnValue("dueDate", invoice.getDueDate()));
        columns.add(new ColumnValue("updated", now));
        columns.add(new ColumnValue("journalEntryKey", invoice.getJournalEntry().getId()));
        columns.add(new ColumnValue("billTo", invoice.getBillTo()));
        columns.add(new ColumnValue("toBePrinted", invoice.isToBePrinted()));
        columns.add(new ColumnValue("toBeSent", invoice.isToBeSent()));
        columns.add(new ColumnValue("sendToEmailKey", invoice.getSendToEmailKey()));
        columns.add(new ColumnValue("templateKey", invoice.getTemplateKey()));
        columns.add(new ColumnValue("amount", invoice.getAmount()));





        if (invoice.getId() > 0) {
            UpdateQuery uq = factory.getUpdateQuery();
            uq.setTable(tablePrefix + "Invoice");
            uq.addWhereCondition("id", WhereCondition.EQUAL_TO, invoice.getId());
            uq.appendColumns(columns);
            uq.execute(conn);

        } else {
            columns.add(new ColumnValue("companyKey", cid));

            InsertQuery iq = factory.getInsertQuery();
            iq.setTable(tablePrefix + "Invoice");
            iq.addAutoIncrementColumn("id");
            iq.addColumn("created", now);

            iq.returnID(true);
            iq.appendColumns(columns);
            invoice.setId(iq.execute(conn));


        }
    }*/

	/**
	 * this one gets all default taxes for a company
	 */
	//@ pre sess != null;
	//@ post true;
	//@ signals_only HibernateException;
	public static List getTaxes(Session sess, Integer cid) throws HibernateException {
		List ret;

		ret = sess.find("from Tax tax where tax.companyKey = ? and tax.invoiceKey = 0",
				cid,
				Hibernate.INTEGER);
		return ret;

	}

	/**
	 * this one gets the taxes attached to an invoice
	 */
	//@ pre sess != null;
	//@ post true;
	//@ signals_only HibernateException;
	public static List getTaxesForInvoices(Session sess, Integer invoiceKey) throws HibernateException {
		List ret = sess.find("from Tax tax where  tax.invoiceKey = ?",
				invoiceKey, 
				Hibernate.INTEGER);
		return ret;

	}



	/**
	 * Removes charges from invoice by setting invoiceKey to 0
	 * Used when updating invoices
	 */
	//@ pre sess != null;
    //@ post true;
    //@ signals_only HibernateException;
	public static int removeChargesFromInvoice(Session sess, Invoice invoice) throws HibernateException {
		List x = sess.find("from Charge charge where charge.invoiceKey = ?",
				invoice.getId(),
				Hibernate.INTEGER);
		for (int i = 0; i < x.size(); i++) {
			Charge charge = (Charge) x.get(i);
			charge.setInvoiceKey(null);
		}
		return x.size();
	}

	/**
	 * Adds a charge to an invoice by setting invoiceKey to invoice id
	 */
	//@ pre sess != null;
    //@ post true;
    //@ signals_only HibernateException;
	public static void addChargeToInvoice(Session sess, Invoice invoice, Integer chargeToAdd) throws HibernateException {
		Charge x = (Charge) sess.get(Charge.class, chargeToAdd);
		if(x != null){
			x.setInvoiceKey(invoice.getId());
		}




	}

	//@ pre sess != null;
    //@ post true;
    //@ signals_only HibernateException;
	public static Tax getTax(Session sess, Integer id) throws HibernateException {
		Tax ret = null;
		if(id.intValue() == 0){
			return Tax.getDefault();
		}

		/*

        SelectQuery sq = factory.getSelectQuery();
        sq.addTable(tablePrefix + "Tax");
        sq.addWhereCondition("id", WhereCondition.EQUAL_TO, id);

        CrossdbResultSet rs2 = sq.execute(conn);
        if (rs2.next()) {
            ret = getTaxFromResultSet(rs2);

        }
        rs2.close();*/


		return (Tax) sess.get(Tax. class, id);
	}

	/* public static void saveTax(Session sess, Tax tax) throws SQLException {
         List columns = new ArrayList();

        columns.add(new ColumnValue("name", tax.getName()));
        columns.add(new ColumnValue("percent", tax.getPercent()));
        columns.add(new ColumnValue("payableAccountKey", tax.getPayableAccountKey()));
        columns.add(new ColumnValue("receivableAccountKey", tax.getReceivableAccountKey()));

        if (tax.getId() > 0) {
            UpdateQuery uq = factory.getUpdateQuery();
            uq.setTable(tablePrefix + "Tax");
            uq.addWhereCondition("id", WhereCondition.EQUAL_TO, tax.getId());
            uq.appendColumns(columns);
            uq.execute(conn);

        } else {
            columns.add(new ColumnValue("companyKey", cid));

            InsertQuery iq = factory.getInsertQuery();
            iq.setTable(tablePrefix + "Tax");
            iq.addAutoIncrementColumn("id");
            iq.returnID(true);
            iq.appendColumns(columns);
            tax.setId(iq.execute(conn));

        }

    }*/

	//@ pre factory != null;
    //@ post true;
    //@ signals_only SQLException;
	public static Preferences getPreferences(SQLFactory factory, Connection conn, Integer cid) throws SQLException {

		Preferences prefs = null;
		SelectQuery sq = factory.getSelectQuery();
		sq.addTable(tablePrefix + "Preferences");
		sq.addWhereCondition("companyKey", WhereCondition.EQUAL_TO, cid.intValue());
		ResultSet rs = sq.execute(conn);
		if(rs.next()){
			prefs = new Preferences();
			prefs.setId(rs.getInt("id"));
			prefs.setCompanyKey(rs.getInt("companyKey"));
			prefs.setFromEmail(rs.getString("fromEmail"));

		}
		rs.close();
		if(prefs == null){
			return Preferences.getDefault();
		}
		return prefs;
	}

	//@ pre factory != null;
    //@ post true;
    //@ signals_only SQLException;
	public static void savePreferences(SQLFactory factory, Connection conn, int cid, int uid, Preferences prefs) throws SQLException {
		List columns = new ArrayList();


		columns.add(new ColumnValue("fromEmail", prefs.getFromEmail()));
		if (prefs.getId() > 0) {
			UpdateQuery uq = factory.getUpdateQuery();
			uq.setTable(tablePrefix + "Preferences");
			uq.addWhereCondition("id", WhereCondition.EQUAL_TO, prefs.getId());
			uq.appendColumns(columns);
			uq.execute(conn);

		} else {
			columns.add(new ColumnValue("companyKey", cid));

			InsertQuery iq = factory.getInsertQuery();
			iq.setTable(tablePrefix + "Preferences");
			iq.addAutoIncrementColumn("id");
			iq.returnID(true);
			iq.appendColumns(columns);
			prefs.setId(iq.execute(conn));

		}

	}

	//@ pre factory != null;
    //@ post true;
    //@ signals_only SQLException;
	public static void saveAClass(SQLFactory factory, Connection conn, int cid, int uid, AClass aclass) throws SQLException {
		List columns = new ArrayList();

		columns.add(new ColumnValue("name", aclass.getName()));


		if (aclass.getId() > 0) {
			UpdateQuery uq = factory.getUpdateQuery();
			uq.setTable(tablePrefix + "AClass");
			uq.addWhereCondition("id", WhereCondition.EQUAL_TO, aclass.getId());
			uq.appendColumns(columns);
			uq.execute(conn);

		} else {
			columns.add(new ColumnValue("companyKey", cid));

			InsertQuery iq = factory.getInsertQuery();
			iq.setTable(tablePrefix + "AClass");
			iq.addAutoIncrementColumn("id");
			iq.returnID(true);
			iq.appendColumns(columns);
			aclass.setId(iq.execute(conn));

		}
	}

	//@ pre factory != null;
    //@ post true;
    //@ signals_only SQLException;
	public static AClass getAClass(SQLFactory factory, Connection conn, int cid, int uid, int id) throws SQLException {
		AClass ret = null;
		if(id == 0){
			return AClass.getDefault();
		}



		SelectQuery sq = factory.getSelectQuery();
		sq.addTable(tablePrefix + "Product");
		sq.addWhereCondition("id", WhereCondition.EQUAL_TO, id);

		CrossdbResultSet rs2 = sq.execute(conn);
		if (rs2.next()) {
			ret = new AClass();
			ret.setId(rs2.getInt("id"));
			ret.setName(rs2.getString("name"));


		}
		rs2.close();

		return ret;
	}

	//@ pre sess != null;
    //@ post true;
    //@ signals_only HibernateException;
	public static Product getProduct(Session sess, Integer productKey) throws HibernateException {
		return (Product) sess.load(Product.class, productKey);
	}

	//@ pre sess != null;
    //@ post true;
    //@ signals_only HibernateException;
	public static List getProducts(Session sess, Integer cid) throws HibernateException {
		return sess.find("from " + Product.class.getName() + " prod where prod.companyKey = ?",
				cid,
				Hibernate.INTEGER);

	}

	//@ pre sess != null;
    //@ post true;
    //@ signals_only HibernateException;
	public static List getAccounts(Session sess, Integer cid) throws HibernateException {
		return sess.find("from " + Account.class.getName() + " acc where acc.companyKey = ?",
				cid,
				Hibernate.INTEGER);

	}

	//@ pre sess != null;
    //@ post true;
    //@ signals_only HibernateException;
	public static Charge getCharge(Session sess, Integer id) throws HibernateException {
		Charge charge = (Charge) sess.load(Charge.class, id);
		/*if(charge.get)
        charge.setRecurrence();*/
		return charge;
	}



	/* public static void saveRecurrence(SQLFactory factory, Connection conn, int cid, int uid, Recurrence recurrence) throws SQLException {
        List columns = new ArrayList();

        columns.add(new ColumnValue("appKey", recurrence.getRealm()));
        columns.add(new ColumnValue("itemKey", recurrence.getItemKey()));
        columns.add(new ColumnValue("type", "" + recurrence.getType()));
        columns.add(new ColumnValue("x", recurrence.getX()));
        columns.add(new ColumnValue("y", recurrence.getY()));
        columns.add(new ColumnValue("z", recurrence.getZ()));
        columns.add(new ColumnValue("startDate", recurrence.getStartDate()));
        columns.add(new ColumnValue("countdown", recurrence.getCountdown()));
        columns.add(new ColumnValue("endDate", recurrence.getEndDate()));


        if (recurrence.getId() > 0) {
            UpdateQuery uq = factory.getUpdateQuery();
            uq.setTable("Recurrence");
            uq.addWhereCondition("id", WhereCondition.EQUAL_TO, recurrence.getId());
            uq.appendColumns(columns);
            uq.execute(conn);

        } else {
            //columns.add(new ColumnValue("companyKey", cid));

            InsertQuery iq = factory.getInsertQuery();
            iq.setTable("Recurrence");
            iq.addAutoIncrementColumn("id");
            iq.returnID(true);
            iq.appendColumns(columns);
            recurrence.setId(iq.execute(conn));

        }
    }*/

	/* public static Recurrence getRecurrence(SQLFactory factory, Connection conn, int cid, int uid, int appKey, int itemKey) throws SQLException {
        Recurrence ret = null;




        SelectQuery sq = factory.getSelectQuery();
        sq.addTable("Recurrence");
        sq.addWhereCondition("appKey", WhereCondition.EQUAL_TO, appKey);
         sq.addWhereCondition("itemKey", WhereCondition.EQUAL_TO, itemKey);

        CrossdbResultSet rs2 = sq.execute(conn);
        if (rs2.next()) {
            ret = new Recurrence();
            ret.setId(rs2.getInt("id"));
            ret.setAppKey(rs2.getInt("appKey"));
            ret.setItemKey(rs2.getInt("itemKey"));

            ret.setType(rs2.getString("type").charAt(0));

            ret.setX(rs2.getInt("x"));
            ret.setY(rs2.getInt("y"));
            ret.setZ(rs2.getInt("z"));
            ret.setStartDate(rs2.getDateTime("startDate"));

                ret.setEndDate(rs2.getDateTime("endDate"));
                ret.setCountdown(rs2.getInt("countdown"));


        }
        rs2.close();

        return ret;
    }*/
	
	//@ pre sess != null;
    //@ post true;
    //@ signals_only HibernateException;
	public static Recurrence getRecurrence(Session sess, String realm, String itemKey) throws HibernateException {
		Recurrence ret = null;

		List recs = sess.find("from " + Recurrence.class.getName() + " rec where rec.realm = ? and rec.itemKey = ?",
				new Object[]{realm, itemKey},
				new Type[]{Hibernate.STRING, Hibernate.STRING}
		);
		if(recs.size() > 0){
			ret = (Recurrence) recs.get(0);
		}


		return ret;
	}

	/**
      so all charges that:
           1. have a recurrence
           2. now is after start date
           todo: 3. now is before end date OR end date is null
           todo: 4. countdown == -1 OR > 0 - can do both in SQL query
           5. now is after latest chargeDate + recurrence time

     Then you have to change the recurrence itemKey to the new Charge

	 * @param factory
	 * @param conn
	 * @param cid
	 * @param uid

	 * @return
	 * @throws SQLException
	 */
	/* public static List getOutstandingRecurringCharges(SQLFactory factory, Connection conn, int cid, int uid) throws SQLException {

        List ret = new ArrayList();

        Date now = new Date();
        int appKey = AccountingPersistenceManager.APP_ID_CHARGE;

           SelectQuery sq = factory.getSelectQuery();
           sq.addTable(AccountingPersistenceManager.tablePrefix + "Charge");
           sq.addTable("Recurrence");
           sq.addWhereCondition(new WhereCondition("Recurrence", "appKey", WhereCondition.EQUAL_TO, new Integer(appKey)));
           sq.addWhereCondition(new WhereCondition("Recurrence", "itemKey", WhereCondition.EQUAL_TO, AccountingPersistenceManager.tablePrefix + "Charge", "id"));
           sq.addWhereCondition(new WhereCondition(AccountingPersistenceManager.tablePrefix + "Charge", "companyKey", WhereCondition.EQUAL_TO, new Integer(cid)));
           // so now has recurrence is covered
           sq.addWhereCondition(new WhereCondition("Recurrence", "startDate", WhereCondition.LESS_THAN, now));
           // now 2 is covered
           CrossdbResultSet rs = sq.execute(conn);
           while(rs.next()){
               int chargeid2 = rs.getInt(AccountingPersistenceManager.tablePrefix + "Charge.id");
               Date chargeDate2 = rs.getDateTime("chargeDate");



                Recurrence recurrence = new Recurrence();
                   recurrence.setId(rs.getInt("Recurrence.id"));
                   recurrence.setAppKey(appKey);
                   recurrence.setItemKey(chargeid2);
                   recurrence.setType(rs.getString("Recurrence.type").charAt(0));
                   recurrence.setX( rs.getInt("x"));
                   recurrence.setY( rs.getInt("y"));
                   recurrence.setZ( rs.getInt("z"));
                   recurrence.setStartDate(rs.getDateTime("Recurrence.startDate"));
                   recurrence.setEndDate(rs.getDateTime("Recurrence.endDate"));
                   recurrence.setCountdown(rs.getInt("Recurrence.countdown"));

               Date nextDate = recurrence.getNextDate(chargeDate2);



               if(nextDate.before(now)){



                   Charge charge = getCharge(factory, conn, cid, uid, chargeid2);
                   charge.setRecurrence(recurrence);

                   ret.add(charge);
               }

           }
           rs.close();

        return ret;
    }*/

	/**
	 * deletes:
	 * charge,
	 * chargelines,
	 * transactions,
	 * recurrence
	 * @param factory
	 * @param conn
	 * @param cid
	 * @param uid
	 * @param charge
	 * @throws SQLException
	 */
	/*public static void deleteCharge(SQLFactory factory, Connection conn, int cid, int uid, Charge charge) throws SQLException {
       deleteRecurrence(factory, conn, cid, uid, charge.getRecurrence());
        deleteChargeLines(factory, conn, cid, uid, charge.getChargeLines());

        DeleteQuery dq = factory.getDeleteQuery();
        dq.setTable(tablePrefix + "Charge");
        dq.addWhereCondition("id", WhereCondition.EQUAL_TO, charge.getId());
        dq.execute(conn);


    }*/

	/*
    private static void deleteChargeLines(SQLFactory factory, Connection conn, int cid, int uid, Set chargeLines) throws SQLException {
        Iterator iter = chargeLines.iterator();
//        for (int i = 0; i < chargeLines.size(); i++) {
         while(iter.hasNext()){
             ChargeLine chargeLine = (ChargeLine) iter.next();//(ChargeLine) chargeLines.get(i);
            deleteChargeLine(factory, conn, cid, uid, chargeLine);
        }
    }*/

	/* private static void deleteRecurrence(SQLFactory factory, Connection conn, int cid, int uid, Recurrence recurrence) throws SQLException {
       if(recurrence != null){
        DeleteQuery dq = factory.getDeleteQuery();
        dq.setTable("Recurrence");
        dq.addWhereCondition("id", WhereCondition.EQUAL_TO, recurrence.getId());
        dq.execute(conn);
       }
    }*/



}