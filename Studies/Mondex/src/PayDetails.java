/************************************************************************
 * The Mondex Case Study - The KeY Approach
 * Payment Details class
 * author: Dr. Isabel Tonin (tonin@ira.uka.de)
 * Universität Karlsruhe - Institut für Theoretische Informatik
 * http://key-project.org/
 */

public class PayDetails
{
  /* Z spec TransferDetails fields */
  /*@ spec_public @*/ short fromName = 0;
  /*@ spec_public @*/ short toName = 0;
  /*@ spec_public @*/ short value = 0;
  /* Z spec PayDetails extra fields */
  /*@ spec_public @*/ short fromSeq = 0;
  /*@ spec_public @*/ short toSeq = 0;
	  
  protected PayDetails () {}
	  
  public boolean equals(PayDetails x)
  {
    return (x.fromName == fromName &&
	    x.toName   == toName &&
	    x.value    == value &&
	    x.fromSeq  == fromSeq &&
	    x.toSeq    == toSeq);
  }
}
