/************************************************************************
 * The Mondex Case Study - The KeY Approach
 * javacard api reference implementation
 * author: Dr. Wojciech Mostowski (W.Mostowski@cs.ru.nl)
 * (adapted by Dr. Isabel Tonin - tonin@ira.uka.de)
 * Universität Karlsruhe - Institut für Theoretische Informatik
 * http://key-project.org/
 */

package javacard.framework;

public interface ISO7816 {

  short SW_NO_ERROR                       = (short)0x9000;
  short SW_BYTES_REMAINING_00             = (short)0x6100;
  short SW_WRONG_LENGTH                   = (short)0x6700;
  short SW_SECURITY_STATUS_NOT_SATISFIED  = (short)0x6982;
  short SW_FILE_INVALID                   = (short)0x6983;
  short SW_DATA_INVALID	                  = (short)0x6984;
  short SW_CONDITIONS_NOT_SATISFIED	  = (short)0x6985;
  short SW_COMMAND_NOT_ALLOWED	          = (short)0x6986;
  short SW_APPLET_SELECT_FAILED	          = (short)0x6999;
  short SW_WRONG_DATA	                  = (short)0x6A80;
  short SW_FUNC_NOT_SUPPORTED             = (short)0x6A81;
  short SW_FILE_NOT_FOUND                 = (short)0x6A82;
  short SW_RECORD_NOT_FOUND               = (short)0x6A83;
  short SW_INCORRECT_P1P2                 = (short)0x6A86;
  short SW_WRONG_P1P2 	                  = (short)0x6B00;
  short SW_CORRECT_LENGTH_00              = (short)0x6C00;
  short SW_INS_NOT_SUPPORTED              = (short)0x6D00;
  short SW_CLA_NOT_SUPPORTED              = (short)0x6E00;
  short SW_UNKNOWN                        = (short)0x6F00;
  short SW_FILE_FULL                      = (short)0x6A84;
  short SW_LOGICAL_CHANNEL_NOT_SUPPORTED  = (short)0x6881;
  short SW_SECURE_MESSAGING_NOT_SUPPORTED = (short)0x6882;
  short SW_WARNING_STATE_UNCHANGED        = (short)0x6200;
  
  byte OFFSET_CLA      = (byte)0;
  byte OFFSET_INS      = (byte)1;
  byte OFFSET_P1       = (byte)2;
  byte OFFSET_P2       = (byte)3;
  byte OFFSET_LC       = (byte)4;
  byte OFFSET_CDATA    = (byte)5;
  
  byte CLA_ISO7816                = (byte)0x00;
  byte INS_SELECT                 = (byte)0xA4;
  byte INS_EXTERNAL_AUTHENTICATE  = (byte) 0x82;

}
