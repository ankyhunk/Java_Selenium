@tag
Feature: Error Validation
 
 @ErrorValidation
 Scenario Outline: Title of your Scenario outline
 	Given I landed on Ecommerce Page
 	When Logged in with username <name> and password <password>
 	Then "Incorrect email or password." message is displayed
 	
 	Examples:
		|name				|password |
		|${TEST_EMAIL} |wrong-password |
