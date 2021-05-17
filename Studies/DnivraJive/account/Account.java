/* === This file is part of Jive JML dynamic program verifier ===
 *
 *   Copyright 2012, Arvind S Raj <sraj[dot]arvind[at]gmail[dot]com>
 *
 *   Jive JML dynamic program verifier is free software: you can redistribute it
 *   and/or modify it under the terms of the GNU General Public License as
 *   published by the Free Software Foundation, either version 3 of the License,
 *   or (at your option) any later version.
 *
 *   Jive JML Dynamic Program Verifier is distributed in the hope that it
 *   will be useful, but WITHOUT ANY WARRANTY; without even the implied
 *   warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 *   See the GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with Jive JML Dynamic Program Verifier. If not, see
 *   <http://www.gnu.org/licenses/>.
 */

package account;

public class Account {
  //@ public invariant bal >= 0;
  private /*@ spec_public @*/ int bal;

  /*@ requires amt >= 0;
    @ assignable bal;
    @ ensures bal == amt; @*/
  public Account(int amt) {
    bal = amt;
  }

  /*@ assignable bal;
    @ ensures bal == acc.bal; @*/
  public Account(Account acc) {
    bal = acc.getBalance();
  }

  /*@ requires amt > 0 && amt <= acc.getBalance();
    @ assignable bal, acc.bal;
    @ ensures bal == \old(bal) + amt
    @   && acc.bal == \old(acc.bal) - amt; @*/
  public void transferAmount(int amt, Account acc) {
    acc.withdrawAmount(amt);
    depositAmount(amt);
  }

  /*@ requires amt > 0 && amt <= bal;
    @ assignable bal;
    @ ensures bal == \old(bal) - amt; @*/
  public void withdrawAmount(int amt) {
    bal -= amt;
  }

  /*@ requires amt > 0;
    @ assignable bal;
    @ ensures bal == \old(bal) + amt; @*/
  public void depositAmount(int amt) {
    bal += amt;
  }

  //@ ensures \result == bal;
  public /*@ pure @*/ int getBalance() {
    return bal;
  }
};
