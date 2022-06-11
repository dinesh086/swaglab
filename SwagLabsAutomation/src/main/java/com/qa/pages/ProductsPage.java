package com.qa.pages;


import com.qa.MenuPage;
import com.qa.utils.TestUtils;

import io.appium.java_client.MobileElement;
import io.appium.java_client.pagefactory.AndroidFindBy;

public class ProductsPage extends MenuPage {
	TestUtils utils = new TestUtils();
	@AndroidFindBy(xpath="//android.view.ViewGroup/android.view.ViewGroup[2]/android.widget.TextView") private MobileElement productTitleTxt;
	@AndroidFindBy(xpath="(//android.widget.TextView[@content-desc=\"test-Item title\"])[1]") private MobileElement SLBTitle;
	@AndroidFindBy(xpath="(//android.widget.TextView[@content-desc=\"test-Price\"])[1]") private MobileElement SLBPrice;
	
	
	
	
public String getTitle() {
	String title = getText(productTitleTxt, "product page title is - ");
	return title;
	
}


public String getSLBTitle() {
	String title = getText(SLBTitle, "title is - ");
	return title;
	
}

public String getSLBPrice() {
	String price = getText(SLBPrice, "price is - ");
	return price;
	
}

public ProductDetailsPage pressSLBTitle() {
	click(SLBTitle, "press SLB tile link");
	return new ProductDetailsPage();
}




}