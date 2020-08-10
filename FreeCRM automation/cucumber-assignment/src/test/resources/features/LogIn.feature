Feature: Verify if the user gets logged in

	 
Scenario: User is welcomed into the homepage
	Given User in the homepage
	And The login page is displayed
	When User enters valid username and password
	| numinus1@gmail.com | HighRadius |
	And Click on login button
	Then User is logged into the website
	