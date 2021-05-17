/**
 * This will be the main class we will use to keep it sort of 3 tier where this is the middle tier
 *
 * - then can swap out implementations if we want, ie: change from hibernate to ojb, etc.
 *
 * @author Travis Reeder - travis@spaceprogram.com
 * Date: Feb 22, 2003
 * Time: 6:38:42 PM
 * @version 0.1
 */
package com.spaceprogram.accounting;


import java.sql.SQLException;

/*@nullable_by_default@*/
public interface AccountingManager {
	//@ pre companyKey >= 0;
    int getAccountIdFromInternalCode(int companyKey, int internalAccountCode);
    /**
     * done with manager, so close everything up and commit it all
     */
    void close() throws SQLException, Exception;
}
