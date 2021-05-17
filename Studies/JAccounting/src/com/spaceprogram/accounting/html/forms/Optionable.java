/**
 * things that can go in option lists should implement this
 *
 * @author Travis Reeder - travis@spaceprogram.com
 * @version 0.1
 * Date: Oct 6, 2002
 * Time: 11:21:28 PM
 * 
 */
package com.spaceprogram.accounting.html.forms;
/*@nullable_by_default@*/
public interface Optionable {

    int getId();
    //@ post \result instanceof java.lang.String;
    String getName();


}
