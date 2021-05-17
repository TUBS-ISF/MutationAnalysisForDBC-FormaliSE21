/**
  * This file is part of the KeY Java Card API Reference Implementation.
  *
  * Author: Wojciech Mostowski, woj@cs.ru.nl
  *   
  * For details and license see README and LICENSE files.
  *    
  * $Id: SecurityService.java,v 1.2 2007/06/25 09:57:01 woj Exp $
  */

package javacard.framework.service;

public interface SecurityService extends Service {

    public static final short PRINCIPAL_APP_PROVIDER = (short) 3;

    public static final short PRINCIPAL_CARD_ISSUER = (short) 2;

    public static final short PRINCIPAL_CARDHOLDER = (short) 1;

    public static final byte PROPERTY_INPUT_CONFIDENTIALITY = (byte) 1;

    public static final byte PROPERTY_INPUT_INTEGRITY = (byte) 2;

    public static final byte PROPERTY_OUTPUT_CONFIDENTIALITY = (byte) 4;

    public static final byte PROPERTY_OUTPUT_INTEGRITY = (byte) 8;

    public boolean isAuthenticated(short principal) throws ServiceException;

    public boolean isChannelSecure(byte properties) throws ServiceException;

    public boolean isCommandSecure(byte properties) throws ServiceException;

}
