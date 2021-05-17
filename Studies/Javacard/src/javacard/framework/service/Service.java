/**
  * This file is part of the KeY Java Card API Reference Implementation.
  *
  * Author: Wojciech Mostowski, woj@cs.ru.nl
  *   
  * For details and license see README and LICENSE files.
  *    
  * $Id: Service.java,v 1.2 2007/06/25 09:57:01 woj Exp $
  */

package javacard.framework.service;

import javacard.framework.APDU;

public interface Service {

    public boolean processDataIn(APDU apdu);

    public boolean processCommand(APDU apdu);

    public boolean processDataOut(APDU apdu);

}
