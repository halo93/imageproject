# Gherkin file for upload image feature
Feature: Upload Image

    Scenario: Upload new image with success
        Given browser "Google Chrome"
        When I open "{environment.URL}"
        And I set "file description" value to "File description placeholder"
        And I click on "Choose File button"
        And I click on "image.jpg"
        And I press on “ENTER”
        And I click on "Submit image button"
        Then I should see text “Success” in “Response message placeholder”

    Scenario: Upload new image without success due to missing description
        Given browser "Google Chrome"
        When I open "{environment.URL}"
        And I click on "Choose File button"
        And I click on "image.jpg"
        And I press on “ENTER”
        And I click on "Submit image button"
        Then I should see text “Failed” in “Response message placeholder”
        And I should see text “Required” in “File description error placeholder”

    Scenario: Upload new image without success due to missing description
        Given browser "Google Chrome"
        When I open "{environment.URL}"
        And I click on "Choose File button"
        And I click on "image.jpg"
        And I press on “ENTER”
        And I click on "Submit image button"
        Then I should see text “Failed” in “Response message placeholder”
        And I should see text “Required” in “File description error placeholder”
