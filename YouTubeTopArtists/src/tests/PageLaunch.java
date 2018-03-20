package tests;

import java.io.IOException;

import mymethods.LaunchBrowser;
import mymethods.OnPageActions;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

import pages.MusicInsights;

public class PageLaunch {
	
	@Test(enabled = false)
	public void launchMusicInsightsPage() throws IOException, InterruptedException{
		
		LaunchBrowser launcher = new LaunchBrowser();
		WebDriver driver = launcher.launchMyBrowser("Chrome");
		
		MusicInsights musicInsightsPage = new MusicInsights();
		musicInsightsPage.launchPage(driver);
		
		Thread.sleep(1000);
		OnPageActions actor = new OnPageActions();
		actor.myBrowserSnapshot(driver, "musicInsightsPage.jpg");
		
		driver.quit();
	}
	
	@Test
	public void searchLocation() throws IOException, InterruptedException{
		
		LaunchBrowser launcher = new LaunchBrowser();
		WebDriver driver = launcher.launchMyBrowser("Chrome");
		
		MusicInsights musicInsightsPage = new MusicInsights();
		musicInsightsPage.launchPage(driver);
		musicInsightsPage.searchNative(driver);
		
		Thread.sleep(1000);
		OnPageActions actor = new OnPageActions();
		actor.myBrowserSnapshot(driver, "afterSearch.jpg");
		
		driver.quit();
	}

}
