/**
  * This file is part of the KeY Java Card API Reference Implementation.
  *
  * Author: Wojciech Mostowski, woj@cs.ru.nl
  *   
  * For details and license see README and LICENSE files.
  *    
  * $Id: ECKeyImpl.java,v 1.2 2007/06/25 09:57:01 woj Exp $
  */

package javacard.security;

import javacard.framework.Util;

public abstract class ECKeyImpl extends KeyImpl implements ECKey {

    protected short _k;

    boolean _kSet;

    protected short _size1;

    protected short _size2;

    protected ECKeyImpl(byte type, short size, short wsSize)
            throws CryptoException {
        super(
                type,
                size,
                (short) ((short) ((short) ((short) ((short) (size / 8) + 1) * 48) + 1) + wsSize),
                (short) 6);
        _size1 = (short) ((short) ((short) (size / 8) + 1) * 8);
        _size2 = (short) ((short) (2 * _size1) + 1);
        _k = 0;
        _kSet = false;
    }

    public void setFieldFP(byte[] buffer, short offset, short length)
            throws CryptoException {
        if (_type != KeyBuilder.TYPE_EC_FP_PUBLIC
                && _type != KeyBuilder.TYPE_EC_FP_PRIVATE)
            CryptoException.throwIt(CryptoException.NO_SUCH_ALGORITHM);

        setKeyMaterial(buffer, offset, length, _size1,
                (short) ((short) (3 * _size1) + _size2), (short) 4);
    }

    public void setFieldF2M(short e) throws CryptoException {
        // Poor imitation of what could be really happening here:
        if (_type != KeyBuilder.TYPE_EC_F2M_PUBLIC
                && _type != KeyBuilder.TYPE_EC_F2M_PRIVATE)
            CryptoException.throwIt(CryptoException.NO_SUCH_ALGORITHM);
        Util.setShort(_key, (short) ((short) (3 * _size1) + _size2), e);
        _initialized[4] = true;
    }

    public void setFieldF2M(short e1, short e2, short e3)
            throws CryptoException {
        // Poor imitation of what could be really happening here:
        if (_type != KeyBuilder.TYPE_EC_F2M_PUBLIC
                && _type != KeyBuilder.TYPE_EC_F2M_PRIVATE)
            CryptoException.throwIt(CryptoException.NO_SUCH_ALGORITHM);
        Util.setShort(_key, (short) ((short) (3 * _size1) + _size2), e1);
        Util.setShort(_key,
                (short) ((short) ((short) (3 * _size1) + _size2) + 2), e2);
        Util.setShort(_key,
                (short) ((short) ((short) (3 * _size1) + _size2) + 4), e3);
        _initialized[4] = true;
    }

    public void setA(byte[] buffer, short offset, short length)
            throws CryptoException {
        setKeyMaterial(buffer, offset, length, _size1, (short) 0, (short) 0);
    }

    public void setB(byte[] buffer, short offset, short length)
            throws CryptoException {
        setKeyMaterial(buffer, offset, length, _size1, _size1, (short) 1);
    }

    public void setG(byte[] buffer, short offset, short length)
            throws CryptoException {
        setKeyMaterial(buffer, offset, length, _size2, (short) (2 * _size1),
                (short) 2);
    }

    public void setR(byte[] buffer, short offset, short length)
            throws CryptoException {
        setKeyMaterial(buffer, offset, length, _size1,
                (short) ((short) (2 * _size1) + _size2), (short) 3);
    }

    public short getField(byte[] buffer, short offset) throws CryptoException {
        return getKeyMaterial(buffer, offset, _size1,
                (short) ((short) (3 * _size1) + _size2), (short) 4);
    }

    public short getA(byte[] buffer, short offset) throws CryptoException {
        return getKeyMaterial(buffer, offset, _size1, (short) 0, (short) 0);
    }

    public short getB(byte[] buffer, short offset) throws CryptoException {
        return getKeyMaterial(buffer, offset, _size1, _size1, (short) 1);
    }

    public short getG(byte[] buffer, short offset) throws CryptoException {
        return getKeyMaterial(buffer, offset, _size2, (short) (2 * _size1),
                (short) 2);
    }

    public short getR(byte[] buffer, short offset) throws CryptoException {
        return getKeyMaterial(buffer, offset, _size1,
                (short) ((short) (2 * _size1) + _size2), (short) 3);
    }

    public void setK(short k) {
        _k = k;
        _kSet = true;
    }

    public short getK() throws CryptoException {
        if (!_kSet)
            CryptoException.throwIt(CryptoException.UNINITIALIZED_KEY);
        return _k;
    }

}
