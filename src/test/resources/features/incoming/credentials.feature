Feature: eMSP Server returns credentials

  Scenario: The client receives credentials
    Given CPO is registered in eMSP system
    And eMSP has "cdrs" data "db/cdrs.json"
    When CPO checks "cdr" with "id" "1" in eMSP system
    Then response is success