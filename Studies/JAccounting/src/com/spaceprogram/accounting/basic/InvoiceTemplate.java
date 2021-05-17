package com.spaceprogram.accounting.basic;

/**
 * @author Travis Reeder - travis@spaceprogram.com
 *         Date: Dec 3
 * @author 2003
 *         Time: 4:41:33 PM
 * @version 0.1
 */

/*@nullable_by_default@*/
public class InvoiceTemplate {
    Long id;
    Integer companyKey;
    String name = "";
    String logo = null;
    String companyInfo = "";
    String topText = "";
    String bottomText = "";

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getCompanyKey() {
        return companyKey;
    }

    public void setCompanyKey(Integer companyKey) {
        this.companyKey = companyKey;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getCompanyInfo() {
        return companyInfo;
    }

    public void setCompanyInfo(String companyInfo) {
        this.companyInfo = companyInfo;
    }

    public String getTopText() {
        return topText;
    }

    public void setTopText(String topText) {
        this.topText = topText;
    }

    public String getBottomText() {
        return bottomText;
    }

    public void setBottomText(String bottomText) {
        this.bottomText = bottomText;
    }
}
