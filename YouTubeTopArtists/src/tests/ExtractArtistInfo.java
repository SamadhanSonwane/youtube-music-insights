package tests;

import java.io.IOException;
import java.text.ParseException;

import mymethods.LaunchBrowser;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

import pages.MusicInsights;

public class ExtractArtistInfo{
	
	@Test(enabled = false)
	public void readUnfilteredArtistInfo() throws IOException, InterruptedException{
		
		LaunchBrowser launcher = new LaunchBrowser();
		WebDriver driver = launcher.launchMyBrowser("Chrome");
		
		MusicInsights musicInsightsPage = new MusicInsights();
		musicInsightsPage.launchPage(driver);
		musicInsightsPage.searchNative(driver);
		musicInsightsPage.getDefaultArtistInfo(driver);
		
		driver.quit();
	}
	
	@Test
	public void readAnnualArtistInfo() throws IOException, InterruptedException, ParseException{

		LaunchBrowser launcher = new LaunchBrowser();
		WebDriver driver = launcher.launchMyBrowser("Chrome");
		
		MusicInsights musicInsightsPage = new MusicInsights();
		musicInsightsPage.launchPage(driver);
		musicInsightsPage.searchNative(driver);
		musicInsightsPage.getAnnualArtistInfo(driver);
		
		driver.quit();
	}

}
