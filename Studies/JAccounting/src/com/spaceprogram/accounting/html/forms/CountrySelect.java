/**
 * @author Travis Reeder - travis@spaceprogram.com
 * Date: May 6, 2002
 * Time: 1:33:22 PM
 * 
 */
package com.spaceprogram.accounting.html.forms;

import java.util.List;

/*@nullable_by_default@*/
public class CountrySelect {
	/*@spec_public@*/boolean multiple; // if greater than 1, then show as multiple
    /*@spec_public@*/int size;
	/*@spec_public@*/String name = "country_select";
	/*@spec_public@*/List selected;
	/*@spec_public@*/String countries[];

	public boolean isMultiple() {
		return multiple;
	}

	public void setMultiple(boolean multiple) {
		this.multiple = multiple;
	}

	//@ pre true;
	//@ post \result == this.size;
	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List getSelected() {
		return selected;
	}

	public String[] getCountries() {
		return countries;
	}



	public String getSelect(){
		String ret = "<select name=\"" + name + "\" id=\"" + name + "\"";
		if(size > 0){
			ret += " size=\"" + size + "\"";
		}
		if(multiple){
			ret += " multiple";
		}
		ret += ">\r\n                  <option value=\"AF\"  > Afghanistan \r\n                  <option value=\"AL\"  > Albania \r\n                  <option value=\"DZ\"  > Algeria \r\n                  <option value=\"AS\"  > American Samoa \r\n                  <option value=\"AD\"  > Andorra \r\n                  <option value=\"AO\"  > Angola \r\n                  <option value=\"AI\"  > Anguilla \r\n                  <option value=\"AQ\"  > Antarctica \r\n                  <option value=\"AG\"  > Antigua and Barbuda \r\n                  <option value=\"AR\"  > Argentina \r\n                  <option value=\"AM\"  > Armenia \r\n                  <option value=\"AW\"  > Aruba \r\n                  <option value=\"AU\"  > Australia \r\n                  <option value=\"AT\"  > Austria \r\n                  <option value=\"AZ\"  > Azerbaijan \r\n                  <option value=\"BS\"  > Bahamas \r\n                  <option value=\"BH\"  > Bahrain \r\n                  <option value=\"BD\"  > Bangladesh \r\n                  <option value=\"BB\"  > Barbados \r\n                  <option value=\"BY\"  > Belarus \r\n                  <option value=\"BE\"  > Belgium \r\n                  <option value=\"BZ\"  > Belize \r\n                  <option value=\"BJ\"  > Benin \r\n                  <option value=\"BM\"  > Bermuda \r\n                  <option value=\"BT\"  > Bhutan \r\n                  <option value=\"BO\"  > Bolivia \r\n                  <option value=\"BA\"  > Bosnia and Herzegovina \r\n                  <option value=\"BW\"  > Botswana \r\n                  <option value=\"BV\"  > Bouvet Island \r\n                  <option value=\"BR\"  > Brazil \r\n                  <option value=\"IO\"  > British Indian Ocean Territory \r\n                  <option value=\"BN\"  > Brunei \r\n                  <option value=\"BG\"  > Bulgaria \r\n                  <option value=\"BF\"  > Burkina Faso \r\n                  <option value=\"BI\"  > Burundi \r\n                  <option value=\"KH\"  > Cambodia \r\n                  <option value=\"CM\"  > Cameroon \r\n                  <option value=\"CA\"  > Canada \r\n                  <option value=\"CV\"  > Cape Verde \r\n                  <option value=\"KY\"  > Cayman Islands \r\n                  <option value=\"CF\"  > Central African Republic \r\n                  <option value=\"TD\"  > Chad \r\n                  <option value=\"CL\"  > Chile \r\n                  <option value=\"CN\"  > China \r\n                  <option value=\"CX\"  > Christmas Island \r\n                  <option value=\"CC\"  > Cocos (Keeling) Islands \r\n                  <option value=\"CO\"  > Colombia \r\n                  <option value=\"KM\"  > Comoros \r\n                  <option value=\"CG\"  > Congo \r\n                  <option value=\"CK\"  > Cook Islands \r\n                  <option value=\"CR\"  > Costa Rica \r\n                  <option value=\"CI\"  > C\u00f4te d'Ivoire \r\n                  <option value=\"HR\"  > Croatia (Hrvatska) \r\n                  <option value=\"CU\"  > Cuba \r\n                  <option value=\"CY\"  > Cyprus \r\n                  <option value=\"CZ\"  > Czech Republic \r\n                  <option value=\"CD\"  > Congo (DRC) \r\n                  <option value=\"DK\"  > Denmark \r\n                  <option value=\"DJ\"  > Djibouti \r\n                  <option value=\"DM\"  > Dominica \r\n                  <option value=\"DO\"  > Dominican Republic \r\n                  <option value=\"TP\"  > East Timor \r\n                  <option value=\"EC\"  > Ecuador \r\n                  <option value=\"EG\"  > Egypt \r\n                  <option value=\"SV\"  > El Salvador \r\n                  <option value=\"GQ\"  > Equatorial Guinea \r\n                  <option value=\"ER\"  > Eritrea \r\n                  <option value=\"EE\"  > Estonia \r\n                  <option value=\"ET\"  > Ethiopia \r\n                  <option value=\"FK\"  > Falkland Islands (Islas Malvinas) \r\n                  <option value=\"FO\"  > Faroe Islands \r\n                  <option value=\"FJ\"  > Fiji Islands \r\n                  <option value=\"FI\"  > Finland \r\n                  <option value=\"FR\"  > France \r\n                  <option value=\"GF\"  > French Guiana \r\n                  <option value=\"PF\"  > French Polynesia \r\n                  <option value=\"TF\"  > French Southern and Antarctic Lands \r\n                  <option value=\"GA\"  > Gabon \r\n                  <option value=\"GM\"  > Gambia \r\n                  <option value=\"GE\"  > Georgia \r\n                  <option value=\"DE\"  > Germany \r\n                  <option value=\"GH\"  > Ghana \r\n                  <option value=\"GI\"  > Gibraltar \r\n                  <option value=\"GR\"  > Greece \r\n                  <option value=\"GL\"  > Greenland \r\n                  <option value=\"GD\"  > Grenada \r\n                  <option value=\"GP\"  > Guadeloupe \r\n                  <option value=\"GU\"  > Guam \r\n                  <option value=\"GT\"  > Guatemala \r\n                  <option value=\"GN\"  > Guinea \r\n                  <option value=\"GW\"  > GuineaBissau \r\n                  <option value=\"GY\"  > Guyana \r\n                  <option value=\"HT\"  > Haiti \r\n                  <option value=\"HM\"  > Heard Island and McDonald Islands \r\n                  <option value=\"HN\"  > Honduras \r\n                  <option value=\"HK\"  > Hong Kong SAR \r\n                  <option value=\"HU\"  > Hungary \r\n                  <option value=\"IS\"  > Iceland \r\n                  <option value=\"IN\"  > India \r\n                  <option value=\"ID\"  > Indonesia \r\n                  <option value=\"IR\"  > Iran \r\n                  <option value=\"IQ\"  > Iraq \r\n                  <option value=\"IE\"  > Ireland \r\n                  <option value=\"IL\"  > Israel \r\n                  <option value=\"IT\"  > Italy \r\n                  <option value=\"JM\"  > Jamaica \r\n                  <option value=\"JP\"  > Japan \r\n                  <option value=\"JO\"  > Jordan \r\n                  <option value=\"KZ\"  > Kazakhstan \r\n                  <option value=\"KE\"  > Kenya \r\n                  <option value=\"KI\"  > Kiribati \r\n                  <option value=\"KR\"  > Korea \r\n                  <option value=\"KW\"  > Kuwait \r\n                  <option value=\"KG\"  > Kyrgyzstan \r\n                  <option value=\"LA\"  > Laos \r\n                  <option value=\"LV\"  > Latvia \r\n                  <option value=\"LB\"  > Lebanon \r\n                  <option value=\"LS\"  > Lesotho \r\n                  <option value=\"LR\"  > Liberia \r\n                  <option value=\"LY\"  > Libya \r\n                  <option value=\"LI\"  > Liechtenstein \r\n                  <option value=\"LT\"  > Lithuania \r\n                  <option value=\"LU\"  > Luxembourg \r\n                  <option value=\"MO\"  > Macau SAR \r\n                  <option value=\"MK\"  > Macedonia Former Yugoslav Republic of \r\n                  <option value=\"MG\"  > Madagascar \r\n                  <option value=\"MW\"  > Malawi \r\n                  <option value=\"MY\"  > Malaysia \r\n                  <option value=\"MV\"  > Maldives \r\n                  <option value=\"ML\"  > Mali \r\n                  <option value=\"MT\"  > Malta \r\n                  <option value=\"MH\"  > Marshall Islands \r\n                  <option value=\"MQ\"  > Martinique \r\n                  <option value=\"MR\"  > Mauritania \r\n                  <option value=\"MU\"  > Mauritius \r\n                  <option value=\"YT\"  > Mayotte \r\n                  <option value=\"MX\"  > Mexico \r\n                  <option value=\"FM\"  > Micronesia \r\n                  <option value=\"MD\"  > Moldova \r\n                  <option value=\"MC\"  > Monaco \r\n                  <option value=\"MN\"  > Mongolia \r\n                  <option value=\"MS\"  > Montserrat \r\n                  <option value=\"MA\"  > Morocco \r\n                  <option value=\"MZ\"  > Mozambique \r\n                  <option value=\"MM\"  > Myanmar \r\n                  <option value=\"NA\"  > Namibia \r\n                  <option value=\"NR\"  > Nauru \r\n                  <option value=\"NP\"  > Nepal \r\n                  <option value=\"NL\"  > Netherlands \r\n                  <option value=\"AN\"  > Netherlands Antilles \r\n                  <option value=\"NC\"  > New Caledonia \r\n                  <option value=\"NZ\"  > New Zealand \r\n                  <option value=\"NI\"  > Nicaragua \r\n                  <option value=\"NE\"  > Niger \r\n                  <option value=\"NG\"  > Nigeria \r\n                  <option value=\"NU\"  > Niue \r\n                  <option value=\"NF\"  > Norfolk Island \r\n                  <option value=\"KP\"  > North Korea \r\n                  <option value=\"MP\"  > Northern Mariana Islands \r\n                  <option value=\"NO\"  > Norway \r\n                  <option value=\"OM\"  > Oman \r\n                  <option value=\"PK\"  > Pakistan \r\n                  <option value=\"PW\"  > Palau \r\n                  <option value=\"PA\"  > Panama \r\n                  <option value=\"PG\"  > Papua New Guinea \r\n                  <option value=\"PY\"  > Paraguay \r\n                  <option value=\"PE\"  > Peru \r\n                  <option value=\"PH\"  > Philippines \r\n                  <option value=\"PN\"  > Pitcairn Islands \r\n                  <option value=\"PL\"  > Poland \r\n                  <option value=\"PT\"  > Portugal \r\n                  <option value=\"PR\"  > Puerto Rico \r\n                  <option value=\"QA\"  > Qatar \r\n                  <option value=\"RE\"  > Reunion \r\n                  <option value=\"RO\"  > Romania \r\n                  <option value=\"RU\"  > Russia \r\n                  <option value=\"RW\"  > Rwanda \r\n                  <option value=\"KN\"  > St. Kitts and Nevis \r\n                  <option value=\"LC\"  > St. Lucia \r\n                  <option value=\"VC\"  > St. Vincent and the Grenadines \r\n                  <option value=\"WS\"  > Samoa \r\n                  <option value=\"SM\"  > San Marino \r\n                  <option value=\"ST\"  > S\u00e3o Tom\u00e9 and Pr\u00edncipe \r\n                  <option value=\"SA\"  > Saudi Arabia \r\n                  <option value=\"SN\"  > Senegal \r\n                  <option value=\"SC\"  > Seychelles \r\n                  <option value=\"SL\"  > Sierra Leone \r\n                  <option value=\"SG\"  > Singapore \r\n                  <option value=\"SK\"  > Slovakia \r\n                  <option value=\"SI\"  > Slovenia \r\n                  <option value=\"SB\"  > Solomon Islands \r\n                  <option value=\"SO\"  > Somalia \r\n                  <option value=\"ZA\"  > South Africa \r\n                  <option value=\"GS\"  > South Georgia and the South Sandwich Islands \r\n                  <option value=\"ES\"  > Spain \r\n                  <option value=\"LK\"  > Sri Lanka \r\n                  <option value=\"SH\"  > St. Helena \r\n                  <option value=\"PM\"  > St. Pierre and Miquelon \r\n                  <option value=\"SD\"  > Sudan \r\n                  <option value=\"SR\"  > Suriname \r\n                  <option value=\"SJ\"  > Svalbard and Jan Mayen \r\n                  <option value=\"SZ\"  > Swaziland \r\n                  <option value=\"SE\"  > Sweden \r\n                  <option value=\"CH\"  > Switzerland \r\n                  <option value=\"SY\"  > Syria \r\n                  <option value=\"TW\"  > Taiwan \r\n                  <option value=\"TJ\"  > Tajikistan \r\n                  <option value=\"TZ\"  > Tanzania \r\n                  <option value=\"TH\"  > Thailand \r\n                  <option value=\"TG\"  > Togo \r\n                  <option value=\"TK\"  > Tokelau \r\n                  <option value=\"TO\"  > Tonga \r\n                  <option value=\"TT\"  > Trinidad and Tobago \r\n                  <option value=\"TN\"  > Tunisia \r\n                  <option value=\"TR\"  > Turkey \r\n                  <option value=\"TM\"  > Turkmenistan \r\n                  <option value=\"TC\"  > Turks and Caicos Islands \r\n                  <option value=\"TV\"  > Tuvalu \r\n                  <option value=\"UG\"  > Uganda \r\n                  <option value=\"UA\"  > Ukraine \r\n                  <option value=\"AE\"  > United Arab Emirates \r\n                  <option value=\"UK\"  > United Kingdom \r\n                  <option value=\"US\"  > United States \r\n                  <option value=\"UM\"  > United States Minor Outlying Islands \r\n                  <option value=\"UY\"  > Uruguay \r\n                  <option value=\"UZ\"  > Uzbekistan \r\n                  <option value=\"VU\"  > Vanuatu \r\n                  <option value=\"VA\"  > Vatican City \r\n                  <option value=\"VE\"  > Venezuela \r\n                  <option value=\"VN\"  > Viet Nam \r\n                  <option value=\"VG\"  > Virgin Islands (British) \r\n                  <option value=\"VI\"  > Virgin Islands \r\n                  <option value=\"WF\"  > Wallis and Futuna \r\n                  <option value=\"YE\"  > Yemen \r\n                  <option value=\"YU\"  > Yugoslavia \r\n                  <option value=\"ZM\"  > Zambia \r\n                  <option value=\"ZW\"  > Zimbabwe \r\n  </select>\r\n";
		ret += "<script>\nvar country_sel = document.getElementById(\"" + name + "\");\n"
			+ "var selected_countries = new Array();\n";
		if(selected != null){
		for (int i = 0; i < selected.size(); i++) {
			String s = (String)selected.get(i);
			ret += "selected_countries[" + i + "] = \"" + s + "\";";
		}
		}

		ret	+= "\nfor(i = 0; i < country_sel.options.length; i++){\n"
		+ "for(j = 0; j < selected_countries.length; j++){\n"
			+ "if(selected_countries[j] == country_sel.options[i].value){\n"
			+ " country_sel.options[i].selected = true;\n break;\n}\n}\n}\n</script>\r\n";
		return ret;
	}
	public void setSelected(List selected){
		this.selected = selected;
	}


}
