Feature: Check if the Company tab is present in the menu

Scenario: Company tab is present in the menu
	Given User in the homepage
	And The login page is displayed
	When User enters valid username and password
	| numinus1@gmail.com | HighRadius |
	And Click on login button
	Then User is logged into the website
	When Menu is displayed
	Then Company tab is present
	