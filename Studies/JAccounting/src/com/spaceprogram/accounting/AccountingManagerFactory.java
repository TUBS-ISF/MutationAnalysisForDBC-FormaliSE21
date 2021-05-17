/**
 * used to get an AccountingManager instance for a page
 *
 * - akin to opening a hibernate session
 *
 * @author Travis Reeder - travis@spaceprogram.com
 * Date: Feb 22, 2003
 * Time: 6:59:55 PM
 * @version 0.1
 */
package com.spaceprogram.accounting;

import java.sql.SQLException;

/*@nullable_by_default@*/
public interface AccountingManagerFactory {
	//@ pre companyKey >= 0;
    AccountingManager getAccountingManager(int companyKey) throws SQLException, Exception;


}
