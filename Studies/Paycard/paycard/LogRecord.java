package paycard;

public class LogRecord {
    
    /*@ public invariant
      @     !empty ==> (balance >= 0 && transactionId >= 0);
      @*/
	
    private /*@ spec_public @*/ static int transactionCounter = 0;
    
    private /*@ spec_public @*/ int balance = -1;
    private /*@ spec_public @*/ int transactionId = -1;
    private /*@ spec_public @*/ boolean empty = true;
    
    

    public /*@pure@*/ LogRecord() {
        //transactionCounter = 0;
    }
    
      
    /*@ public normal_behavior
      @   requires balance >= 0 && transactionCounter >= 0;
      @   ensures this.balance == balance 
      @           && transactionId == \old(transactionCounter);
      @   ensures transactionCounter >= 0;
      @   assignable empty, this.balance, transactionId, transactionCounter;
      @*/
    public void setRecord(int balance) throws CardException {
	if(balance < 0){
	    throw new CardException();
	}
	this.empty = false;
	this.balance = balance;
	this.transactionId = transactionCounter++;
    }
    
    /*@ public normal_behavior
      @   ensures \result == balance;
      @*/
    public /*@pure@*/ int getBalance() {
	return balance;
    }
    
    /*@ public normal_behavior
      @   ensures \result == transactionId;
      @*/
    public /*@pure@*/ int getTransactionId() {
	return transactionId;
    }
}
