package com.spaceprogram.accounting.util;

/**
 * 
 * @author Travis Reeder - travis@spaceprogram.com
 * Date: May 8, 2003
 * Time: 3:57:03 PM
 * @version 0.1
 */
public class Currency {

    public final static Currency currencies[] = {


     new Currency("EUR", "EUR Euro"),
      new Currency("USD", "USD United States Dollars"),
      new Currency("CAD", "CAD Canada Dollars"),
      new Currency("GBP", "GBP United Kingdom Pounds"),
      new Currency("DEM", "DEM Germany Deutsche Marks"),
      new Currency("FRF", "FRF France Francs"),
      new Currency("JPY", "JPY Japan Yen"),
      new Currency("NLG", "NLG Netherlands Guilders"),
      new Currency("ITL", "ITL Italy Lire"),
      new Currency("CHF", "CHF Switzerland Francs"),
      new Currency("DZD", "DZD Algeria Dinars"),
      new Currency("ARS", "ARS Argentina Pesos"),
      new Currency("AUD", "AUD Australia Dollars"),
      new Currency("ATS", "ATS Austria Schillings"),
      new Currency("BSD", "BSD Bahamas Dollars"),
      new Currency("BBD", "BBD Barbados Dollars"),
      new Currency("BEF", "BEF Belgium Francs"),
      new Currency("BMD", "BMD Bermuda Dollars"),
      new Currency("BRL", "BRL Brazil Real"),
      new Currency("BGL", "BGL Bulgaria Leva"),
      new Currency("CAD", "CAD Canada Dollars"),
      new Currency("CLP", "CLP Chile Pesos"),
      new Currency("CNY", "CNY China Yuan Renminbi"),
      new Currency("CYP", "CYP Cyprus Pounds"),
      new Currency("CZK", "CZK Czech Republic Koruny"),
      new Currency("DKK", "DKK Denmark Kroner"),
      new Currency("NLG", "NLG Dutch (Netherlands) Guilders"),
      new Currency("XCD", "XCD Eastern Caribbean Dollars"),
      new Currency("EGP", "EGP Egypt Pounds"),
      new Currency("EUR", "EUR Euro"),
      new Currency("FJD", "FJD Fiji Dollars"),
      new Currency("FIM", "FIM Finland Markkaa"),
      new Currency("FRF", "FRF France Francs"),
      new Currency("DEM", "DEM Germany Deutsche Marks"),
      new Currency("XAU", "XAU Gold Ounces"),
      new Currency("GRD", "GRD Greece Drachmae"),
      new Currency("HKD", "HKD Hong Kong Dollars"),
      new Currency("NLG", "NLG Holland (Netherlands) Guilders"),
      new Currency("HUF", "HUF Hungary Forint"),
      new Currency("ISK", "ISK Iceland Kronur"),
      new Currency("INR", "INR India Rupees"),
      new Currency("IDR", "IDR Indonesia Rupiahs"),
      new Currency("IEP", "IEP Ireland Pounds"),
      new Currency("ILS", "ILS Israel New Shekels"),
      new Currency("ITL", "ITL Italy Lire"),
      new Currency("JMD", "JMD Jamaica Dollars"),
      new Currency("JPY", "JPY Japan Yen"),
      new Currency("JOD", "JOD Jordan Dinars"),
      new Currency("KRW", "KRW Korea (South) Won"),
      new Currency("LBP", "LBP Lebanon Pounds"),
      new Currency("LUF", "LUF Luxembourg Francs"),
      new Currency("MYR", "MYR Malaysia Ringgits"),
      new Currency("MXN", "MXN Mexico Pesos"),
      new Currency("NLG", "NLG Netherlands Guilders"),
      new Currency("NZD", "NZD New Zealand Dollars"),
      new Currency("NOK", "NOK Norway Kroner"),
      new Currency("PKR", "PKR Pakistan Rupees"),
      new Currency("XPD", "XPD Palladium Ounces"),
      new Currency("PHP", "PHP Philippines Pesos"),
      new Currency("XPT", "XPT Platinum Ounces"),
      new Currency("PLN", "PLN Poland Zlotych"),
      new Currency("PTE", "PTE Portugal Escudos"),
      new Currency("ROL", "ROL Romania Lei"),
      new Currency("RUR", "RUR Russia Rubles"),
      new Currency("SAR", "SAR Saudi Arabia Riyals"),
      new Currency("XAG", "XAG Silver Ounces"),
      new Currency("SGD", "SGD Singapore Dollars"),
      new Currency("SKK", "SKK Slovakia Koruny"),
      new Currency("ZAR", "ZAR South Africa Rand"),
      new Currency("KRW", "KRW South Korea Won"),
      new Currency("ESP", "ESP Spain Pesetas"),
      new Currency("XDR", "XDR Special Drawing Rights (IMF)"),
      new Currency("SDD", "SDD Sudan Dinars"),
      new Currency("SEK", "SEK Sweden Kronor"),
      new Currency("CHF", "CHF Switzerland Francs"),
      new Currency("TWD", "TWD Taiwan New Dollars"),
      new Currency("THB", "THB Thailand Baht"),
      new Currency("TTD", "TTD Trinidad and Tobago Dollars"),
      new Currency("TRL", "TRL Turkey Liras"),
      new Currency("GBP", "GBP United Kingdom Pounds"),
      new Currency("USD", "USD United States Dollars"),
      new Currency("VEB", "VEB Venezuela Bolivares"),
      new Currency("ZMK", "ZMK Zambia Kwacha"),
      new Currency("EUR", "EUR Euro"),
      new Currency("XCD", "XCD Eastern Caribbean Dollars"),
      new Currency("XDR", "XDR Special Drawing Right (IMF)"),
      new Currency("XAG", "XAG Silver Ounces"),
      new Currency("XAU", "XAU Gold Ounces"),
      new Currency("XPD", "XPD Palladium Ounces"),
      new Currency("XPT", "XPT Platinum Ounces"),
            };

    String code;
    String name;

    public Currency(String code, String name) {
        this.code = code;
        this.name = name;

    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
