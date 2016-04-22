package com.accenture.fers.it;

import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import com.accenture.fers.utils.Configure;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class SeleniumIT  extends TestCase{

	private HtmlUnitDriver driver = new HtmlUnitDriver();
	private Configure config = new Configure();
	private static final String APPLICATION_URL = System.getProperty("applicationURL");
	private static final String ZAP_IP = System.getProperty("zapIp");
	private static final String ZAP_PORT = System.getProperty("zapPort");

	public SeleniumIT(String testName) {
		super(testName);
		if (config.isZapEnabled() && ZAP_IP != null
				&& !ZAP_IP.equals("") && ZAP_PORT != null
				&& !ZAP_PORT.equals("")) {
			driver.setProxy(ZAP_IP, Integer.valueOf(ZAP_PORT));
		}

	}

	public static Test suite(){
		return new TestSuite(SeleniumIT.class);

	}

	public void testTitle(){
		driver.get(APPLICATION_URL);
		String strExpected = "New Codington Chamber of Commerce Portal";
	    assertEquals(strExpected, driver.getTitle());

	}

}
