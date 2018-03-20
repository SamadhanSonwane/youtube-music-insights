# youtube-music-insights
A Selenium WebDriver project that fetches information (artist, total views) of top artists for the last 12 months in a region/state from YouTube Music Insights (https://artists.youtube.com/insights) and stores it as a dataset in an MS Excel file.

Prerequisites:
	• Java
  • Selenium WebDriver (v3 and above)
	• TestNG
	• Apache POI

Running the tests:
	• Input data can be edited in the 'YouTubeforArtists.xlsx' file under 'data' directory
	• Running 'ExtractArtistInfo.java' in package tests will extract data from YouTube Music Insights
	• The output dataset can be obtained from the 'TopArtistsDataset.xlsx' file under 'output-data' directory

Author:
	Samadhan Dilip Sonwane
