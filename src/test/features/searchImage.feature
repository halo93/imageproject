# Gherkin file for search image feature
Feature: Search Image

    Scenario: Initial page load
        Given browser "Google Chrome"
        When I open "{environment.URL}"
        Then existing records should be displayed
        And the application shows up to 20 records.

    Scenario: Search image by description with success and response result
        Given browser "Google Chrome"
        When I open "{environment.URL}"
        And I set "file description" value to "File description filter placeholder"
        And I click on "Submit search"
        Then "search-results" should be displayed
        And "search-results" has 1 result.
        And I should see text “file description” in "search-results 1st row file description placeholder".
        And I should see the image description, image size, and image file type.

    Scenario: Search image by description with success and response zero result
        Given browser "Google Chrome"
        When I open "{environment.URL}"
        And I set "non-matched file description" value to "File description filter placeholder"
        And I click on "Submit search"
        Then "search-results" should be displayed
        And "search-results" has 0 result.
        And I shouldn't see text “file description” in "search-results 1st row file description placeholder".
        And I should see no image description, image size, and image file type.

    Scenario: Search image by image type with success and response result
        Given browser "Google Chrome"
        When I open "{environment.URL}"
        And I choose "JPEG" option from "File Type combobox"
        And I click on "Submit search"
        Then "search-results" should be displayed
        And "search-results" has 1 result.
        And I should see the type “image/jpeg” in "search-results 1st row file type".
        And I should see the image description, image size, and image file type.

    Scenario: Search image by image type with success and response zero result
        Given browser "Google Chrome"
        When I open "{environment.URL}"
        And I choose "PNG" option from "File Type combobox"
        And I click on "Submit search"
        Then "search-results" should be displayed
        And "search-results" has 0 result.
        And I shouldn't see the type “image/png” in "search-results 1st row file type".
        And I should see no image description, image size, and image file type.

    Scenario: Search image by size with success and response result
        Given browser "Google Chrome"
        When I open "{environment.URL}"
        And I set "1000" value to "Size filter placeholder"
        And I click on "Submit search"
        Then "search-results" should be displayed
        And "search-results" has 1 result.
        And I should see text “1000” in "search-results 1st row size".
        And I should see the image description, image size, and image file type.

    Scenario: Search image by size with success and response zero result
        Given browser "Google Chrome"
        When I open "{environment.URL}"
        And I set "10001" value to "Size filter placeholder"
        And I click on "Submit search"
        Then "search-results" should be displayed
        And "search-results" has 0 result.
        And I shouldn't see text “10001” in "search-results 1st row size".
        And I should see no image description, image size, and image file type.

    Scenario: Search image by size and description with success and response result
        Given browser "Google Chrome"
        When I open "{environment.URL}"
        And I set "1000" value to "Size filter placeholder"
        And I set "file description" value to "File description filter placeholder"
        And I click on "Submit search"
        Then "search-results" should be displayed
        And "search-results" has 1 result.
        And I should see text “1000” in "search-results 1st row size".
        And I should see text “file description” in "search-results 1st row file description placeholder".
        And I should see the image description, image size, and image file type.

    Scenario: Search image by size and description with success and response zero result
        Given browser "Google Chrome"
        When I open "{environment.URL}"
        And I set "10001" value to "Size filter placeholder"
        And I set "non-matched file description" value to "File description filter placeholder"
        And I click on "Submit search"
        Then "search-results" should be displayed
        And "search-results" has 0 result.
        And I shouldn't see text “10001” in "search-results 1st row size".
        And I shouldn't see text “file description” in "search-results 1st row file description placeholder".
        And I should see no image description, image size, and image file type.

    Scenario: Search image by size and image type with success and response result
        Given browser "Google Chrome"
        When I open "{environment.URL}"
        And I set "1000" value to "Size filter placeholder"
        And I choose "JPEG" option from "File Type combobox"
        And I click on "Submit search"
        Then "search-results" should be displayed
        And "search-results" has 1 result.
        And I should see text “1000” in "search-results 1st row size".
        And I should see the type “image/jpeg” in "search-results 1st row file type".
        And I should see the image description, image size, and image file type.

    Scenario: Search image by size and image type with success and response zero result
        Given browser "Google Chrome"
        When I open "{environment.URL}"
        And I set "10001" value to "Size filter placeholder"
        And I choose "PNG" option from "File Type combobox"
        And I click on "Submit search"
        Then "search-results" should be displayed
        And "search-results" has 0 result.
        And I shouldn't see text “10001” in "search-results 1st row size".
        And I shouldn't see the type “image/png” in "search-results 1st row file type".
        And I should see no image description, image size, and image file type.

    Scenario: Search image by image type and description with success and response result
        Given browser "Google Chrome"
        When I open "{environment.URL}"
        And I choose "JPEG" option from "File Type combobox"
        And I set "file description" value to "File description filter placeholder"
        And I click on "Submit search"
        Then "search-results" should be displayed
        And "search-results" has 1 result.
        And I should see the type “image/jpeg” in "search-results 1st row file type".
        And I should see text “file description” in "search-results 1st row file description placeholder".
        And I should see the image description, image size, and image file type.

    Scenario: Search image by image type and description with success and response zero result
        Given browser "Google Chrome"
        When I open "{environment.URL}"
        And I choose "PNG" option from "File Type combobox"
        And I set "non-matched file description" value to "File description filter placeholder"
        And I click on "Submit search"
        Then "search-results" should be displayed
        And "search-results" has 0 result.
        And I shouldn't see the type “image/png” in "search-results 1st row file type".
        And I shouldn't see text “file description” in "search-results 1st row file description placeholder".
        And I should see no image description, image size, and image file type.

    Scenario: Search image by image type and description and size with success and response result
        Given browser "Google Chrome"
        When I open "{environment.URL}"
        And I choose "JPEG" option from "File Type combobox"
        And I set "file description" value to "File description filter placeholder"
        And I set "1000" value to "Size filter placeholder"
        And I click on "Submit search"
        Then "search-results" should be displayed
        And "search-results" has 1 result.
        And I should see the type “image/jpeg” in "search-results 1st row file type".
        And I should see text “file description” in "search-results 1st row file description placeholder".
        And I should see text “1000” in "search-results 1st row size".
        And I should see the image description, image size, and image file type.

    Scenario: Search image by image type and description and size with success and response zero result
        Given browser "Google Chrome"
        When I open "{environment.URL}"
        And I choose "PNG" option from "File Type combobox"
        And I set "non-matched file description" value to "File description filter placeholder"
        And I set "10001" value to "Size filter placeholder"
        And I click on "Submit search"
        Then "search-results" should be displayed
        And "search-results" has 0 result.
        And I shouldn't see the type “image/png” in "search-results 1st row file type".
        And I shouldn't see text “file description” in "search-results 1st row file description placeholder".
        And I shouldn't see text “10001” in "search-results 1st row size".
        And I should see no image description, image size, and image file type.

