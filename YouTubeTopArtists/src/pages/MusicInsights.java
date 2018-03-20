package pages;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class MusicInsights {
	
	static XSSFWorkbook workbook;
	static int infoRowCounter = 1;
	
	// method to get input data from the file YouTubeforArtists.xlsx
	public XSSFSheet getSheet(String sheetName) throws IOException{
		File source = new File(System.getProperty("user.dir") + "\\data\\YouTubeforArtists.xlsx");
		FileInputStream fis = new FileInputStream(source);
		
		workbook = new XSSFWorkbook(fis);
		XSSFSheet sheet = workbook.getSheet(sheetName);
		
		return sheet;
	}
	
	// method to launch the Music Insights page
	public void launchPage(WebDriver driver) throws IOException{
		
		String url = getSheet("MusicInsights").getRow(1).getCell(1).getStringCellValue();
		driver.get(url);
		
		workbook.close();
	}
	
	// method to search for the desired location/state/region
	public void searchNative(WebDriver driver) throws IOException, InterruptedException{
		
		WebElement search = driver.findElement(By.xpath(getSheet("MusicInsights").getRow(1).getCell(4).getStringCellValue()));
		driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
		search.clear();
		search.sendKeys(getSheet("MusicInsights").getRow(2).getCell(1).getStringCellValue());
		Thread.sleep(500);
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		WebElement suggestion = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(getSheet("MusicInsights").getRow(2).getCell(4).getStringCellValue())));
		suggestion.click();
		
		workbook.close();
	}
	
	// method to extract top artist information(artist name and total views) for just 1 time
	public void getDefaultArtistInfo(WebDriver driver) throws IOException{
		
		WebDriverWait wait = new WebDriverWait(driver, 5);
		List<WebElement> artistNames = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath(getSheet("MusicInsights").getRow(3).getCell(4).getStringCellValue())));
		List<WebElement> totalViewsList = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath(getSheet("MusicInsights").getRow(4).getCell(4).getStringCellValue())));
		
		XSSFWorkbook outputWorkbook = new XSSFWorkbook();
		XSSFSheet outputSheet = outputWorkbook.createSheet("ArtistInformation");
		
		// writing column headers
		Row hRow = outputSheet.getRow(0);
		if (hRow == null){
			hRow = outputSheet.createRow(0);
		}
		Cell hCell = hRow.getCell(0);
		if (hCell == null){
			hCell = hRow.createCell(0);
		}
		hCell.setCellValue("Artist");	// column header: Artist
		hCell = hRow.getCell(1);
		if (hCell == null){
			hCell = hRow.createCell(1);
		}
		hCell.setCellValue("Total Views");	// column header: Total Views
		
		// writing data to Excel file
		for(int i=0; i<artistNames.size(); i++){
			
			Row row = outputSheet.getRow(i + 1);
				if (row == null){
					row = outputSheet.createRow(i + 1);
				}
				Cell cell = row.getCell(0);
				if (cell == null){
					cell = row.createCell(0);
				}
			cell.setCellValue(artistNames.get(i).getText());
				cell = row.getCell(1);
				if (cell == null){
					cell = row.createCell(1);
				}
			cell.setCellValue(totalViewsList.get(i).getText().replace(" total views", ""));
		}
		
		FileOutputStream fos = new FileOutputStream(new File(System.getProperty("user.dir") + "\\output-data\\ArtistInformation.xlsx"));
		outputWorkbook.write(fos);
		fos.close();
		outputWorkbook.close();
	}
	
	// method to extract top artist information(name, total views, start date, end date, location) for last 1 year
	public void getAnnualArtistInfo(WebDriver driver) throws InterruptedException, ParseException, IOException{
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		// verifying the visibility of the START DATE field
		WebElement start = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(getSheet("MusicInsights").getRow(5).getCell(4).getStringCellValue())));
		// verifying the visibility of the END DATE field
		WebElement end = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(getSheet("MusicInsights").getRow(6).getCell(4).getStringCellValue())));
		
		
		// setting up output excel file
		XSSFWorkbook outputWorkbook = new XSSFWorkbook();
		XSSFSheet outputSheet = outputWorkbook.createSheet("Top Artists");
		writeColumnHeaders(outputSheet);	// writing column headers
		
		// loop to iterate for last 12 months
		for(int i=0; i<12; i++){
			// extract selected dates
			String dateStrings[] = getSelectedDates(driver);
				String startDate = dateStrings[0];
				String endDate = dateStrings[1];
				// System.out.println(startDate + " " + endDate);
			
			// get top artist info
			List<WebElement> artistNames = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath(getSheet("MusicInsights").getRow(3).getCell(4).getStringCellValue())));
			List<WebElement> totalViewsList = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath(getSheet("MusicInsights").getRow(4).getCell(4).getStringCellValue())));
/*				for(WebElement artistName : artistNames){
					System.out.print(artistName.getText() + ", ");
				}
				System.out.println();
				for(WebElement totalViews : totalViewsList){
					System.out.print(totalViews.getText().replace(" total views", "") + " | ");
				}
				System.out.println();
*/				
				
				// writing output data to Excel file
				for(int j=0; j<artistNames.size(); j++){
					
					Row row = outputSheet.getRow(j + infoRowCounter);
						if (row == null){
							row = outputSheet.createRow(j + infoRowCounter);
						}

					Cell cell = row.getCell(0);
						if (cell == null){
							cell = row.createCell(0);
						}
					cell.setCellValue(artistNames.get(j).getText());	// writing artist name
					cell = row.getCell(1);
						if (cell == null){
							cell = row.createCell(1);
						}
					cell.setCellValue(totalViewsList.get(j).getText().replace(" total views", ""));;	// writing total views
					cell = row.getCell(2);
						if (cell == null){
							cell = row.createCell(2);
						}
					cell.setCellValue(startDate);;	// writing start date
					cell = row.getCell(3);
						if (cell == null){
							cell = row.createCell(3);
						}
					cell.setCellValue(endDate);;	// writing end date
					cell = row.getCell(4);
						if (cell == null){
							cell = row.createCell(4);
						}
					cell.setCellValue(getSheet("MusicInsights").getRow(2).getCell(1).getStringCellValue());	// writing location
				}
				infoRowCounter = infoRowCounter + artistNames.size();
			
			
			// moving back by one month in the start date calendar
			start.click();
			List<WebElement> monthNav = driver.findElements(By.xpath(getSheet("MusicInsights").getRow(7).getCell(4).getStringCellValue())); // the navigation buttons
			WebElement startPrevMonthNav = wait.until(ExpectedConditions.visibilityOf(monthNav.get(0)));
				startPrevMonthNav.click();
				Thread.sleep(1000);	// waiting for a second
				
				Calendar cal = Calendar.getInstance();
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				cal.setTime(sdf.parse(startDate));	// setting Calendar object to extracted start date
				cal.add(Calendar.MONTH, -1);	// move back by 1 month
				String startPrevMonthDate = sdf.format(cal.getTime());
				// System.out.println(startPrevMonthDate);
				
				// xpath is not fetched from the excel file
				List<WebElement> sprev = driver.findElements(By.xpath("//div[@class='day-item selection style-scope paper-calendar' and contains(@date, '" + startPrevMonthDate + "')]"));
				sprev.get(0).click();	// click the target date in the start date calendar
				Thread.sleep(500);	// waiting for half a second
				
			// moving back by one month in the end date calendar	
			end.click();
			WebElement endPrevMonthNav = wait.until(ExpectedConditions.visibilityOf(monthNav.get(2)));
				endPrevMonthNav.click();
				Thread.sleep(1000);	// waiting for a second
				
				cal.setTime(sdf.parse(endDate));	// setting Calendar object to extracted end date
				cal.add(Calendar.MONTH, -1);	// move back by 1 month
				String endPrevMonthDate = sdf.format(cal.getTime());
				// System.out.println(endPrevMonthDate);
	
				// xpath is not fetched from the excel file
				List<WebElement> eprev = driver.findElements(By.xpath("//div[@class='day-item selection style-scope paper-calendar' and contains(@date, '" + endPrevMonthDate + "')]"));
				eprev.get(1).click();	// click the target date in the end date calendar
				Thread.sleep(500);	// waiting for half a second
				
			// clicking the FILTER DATA button
			driver.findElement(By.xpath(getSheet("MusicInsights").getRow(8).getCell(4).getStringCellValue())).click();
			Thread.sleep(5000);
		}
		
		FileOutputStream fos = new FileOutputStream(new File(System.getProperty("user.dir") + "\\output-data\\TopArtistsDataset.xlsx"));
		outputWorkbook.write(fos);
		fos.close();
		outputWorkbook.close();
	}
	
	// method to get start and end dates already selected on the page
	public String[] getSelectedDates(WebDriver driver){
		
		String[] selectedDateStrings = {"", ""};
		
		// find the selected dates in the 2 calendars (start and end date)
		List<WebElement> selectedDates = driver.findElements(By.xpath("//div[@class='day-item selection selected style-scope paper-calendar']"));
		
		// get the start date string
		String startDate = selectedDates.get(0).getAttribute("date");
		// get the end date string
		String endDate = selectedDates.get(1).getAttribute("date");
		
		selectedDateStrings[0] = startDate;
		selectedDateStrings[1] = endDate;
		
		return selectedDateStrings;
	}
	
	// method to write column headers in the output excel file
	public void writeColumnHeaders(XSSFSheet outputSheet){
		
		Row hRow = outputSheet.getRow(0);
			if (hRow == null){
				hRow = outputSheet.createRow(0);
			}
		Cell hCell = hRow.getCell(0);
			if (hCell == null){
				hCell = hRow.createCell(0);
			}
			hCell.setCellValue("Artist");	// column header: Artist
		hCell = hRow.getCell(1);
			if (hCell == null){
				hCell = hRow.createCell(1);
			}
			hCell.setCellValue("Total Views");	// column header: Total Views
		hCell = hRow.getCell(2);
			if (hCell == null){
				hCell = hRow.createCell(2);
			}
			hCell.setCellValue("Start Date");	// column header: Start Date
		hCell = hRow.getCell(3);
			if (hCell == null){
				hCell = hRow.createCell(3);
			}
			hCell.setCellValue("End Date");	// column header: End Date
		hCell = hRow.getCell(4);
			if (hCell == null){
				hCell = hRow.createCell(4);
			}
			hCell.setCellValue("Location");	// column header for state/region
	}

}
