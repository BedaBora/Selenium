Feature: Check if all the entries have been added

Scenario: Check the total number of entries in table
	Given User in the homepage
	And The login page is displayed
	When User enters valid username and password
	| numinus1@gmail.com | HighRadius |
	And Click on login button
	Then User is logged into the website
	When Menu is displayed
	Then Company tab is present
	And Count the number of entries in each page
	And Edit last 5 records
	And Select top 10 rows
	Then Check "MAX" filter option
	