package com.qa.pages;


import com.qa.MenuPage;
import com.qa.utils.TestUtils;

import io.appium.java_client.MobileElement;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.iOSXCUITFindBy;

public class ProductDetailsPage extends MenuPage {
	
	TestUtils utils = new TestUtils();
	
	@AndroidFindBy(xpath="//android.view.ViewGroup[@content-desc=\"test-Description\"]/android.widget.TextView[1]") private MobileElement SLBTitle;
	@AndroidFindBy(xpath="//android.view.ViewGroup[@content-desc=\"test-Description\"]/android.widget.TextView[2]") private MobileElement SLBTxt;
	@AndroidFindBy(accessibility="test-BACK TO PRODUCTS") private MobileElement backToProductsBtn;
	//@AndroidFindBy(accessibility="test-Price") private MobileElement SLBPrice;
	
	@iOSXCUITFindBy (id = "test-ADD TO CART") private MobileElement addToCartBtn;

public String getSLBTitle() {
	String title = getText(SLBTitle, "title is - ");
	return title;
	
}

public String getSLBTxt() {	
	String txt = getText(SLBTxt, "txt is - ");
	return txt;
}


/*
 * public String getSLBPrice() { System.out.println("getting SLB Title" +
 * SLBPrice); return getAttribute(SLBPrice,"text");
 * 
 * }
 */

public String scrollToSLBPriceAndGetSLBPrice() {
	return getText(scrollToElement(), "");
	
}

public void scrollPage() {
	iOSScrollToElement();
}

public Boolean isAddToCartBtnDisplayed() {
	return addToCartBtn.isDisplayed();
}

public ProductsPage pressBackToProductsBtn() {
	click(backToProductsBtn, "navigate back to products page");
	return new ProductsPage();
}


}