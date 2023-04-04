Feature: Registration on Mailchimp

  Scenario Outline: Successful registration with valid credentials
    Given I am on the Mailchimp signup page with browser "<browser>"
    When I enter my email "<email>"
    And I enter a username "<username>"
    And I enter a password "<password>"
    And I click on the signup button
    Then An account should be created "<created>" "<message>"

    Examples:
      | browser | email       | username   | password   | created | message                                                                           |
      | chrome  | hej@hej.com | random     | passWord1. | yes     | Check your email                                                                  |
      | chrome  |             | random     | passWord1. | no      | An email address must contain a single @.                                         |
      | chrome  | hej@hej.com | LarsFlas   | passWord1. | no      | Great minds think alike - someone already has this username. If it's you, log in. |
      | chrome  | hej@hej.com | randomLong | passWord1. | no      | Enter a value less than 100 characters long                                       |
      | safari  | hej@hej.com | random     | passWord1. | yes     | Check your email                                                                  |
      | safari  |             | random     | passWord1. | no      | An email address must contain a single @.                                         |
      | safari  | hej@hej.com | LarsFlas   | passWord1. | no      | Great minds think alike - someone already has this username. If it's you, log in. |
      | safari  | hej@hej.com | randomLong | passWord1. | no      | Enter a value less than 100 characters long                                       |
