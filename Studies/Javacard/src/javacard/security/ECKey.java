/**
  * This file is part of the KeY Java Card API Reference Implementation.
  *
  * Author: Wojciech Mostowski, woj@cs.ru.nl
  *   
  * For details and license see README and LICENSE files.
  *    
  * $Id: ECKey.java,v 1.2 2007/06/25 09:57:01 woj Exp $
  */

package javacard.security;

public interface ECKey {

    public void setFieldFP(byte[] buffer, short offset, short length)
            throws CryptoException;

    public void setFieldF2M(short e) throws CryptoException;

    public void setFieldF2M(short e1, short e2, short e3)
            throws CryptoException;

    public void setA(byte[] buffer, short offset, short length)
            throws CryptoException;

    public void setB(byte[] buffer, short offset, short length)
            throws CryptoException;

    public void setG(byte[] buffer, short offset, short length)
            throws CryptoException;

    public void setR(byte[] buffer, short offset, short length)
            throws CryptoException;

    public void setK(short k);

    public short getField(byte[] buffer, short offset) throws CryptoException;

    public short getA(byte[] buffer, short offset) throws CryptoException;

    public short getB(byte[] buffer, short offset) throws CryptoException;

    public short getG(byte[] buffer, short offset) throws CryptoException;

    public short getR(byte[] buffer, short offset) throws CryptoException;

    public short getK() throws CryptoException;

}
