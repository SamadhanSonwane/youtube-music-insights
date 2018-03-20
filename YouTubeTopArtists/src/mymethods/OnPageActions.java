package mymethods;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

public class OnPageActions {
	
	public void myBrowserSnapshot(WebDriver driver, String fileName) throws IOException{
		File capture = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
		FileUtils.copyFile(capture, new File(System.getProperty("user.dir") + "\\screenshots\\" + fileName));
	}

}
