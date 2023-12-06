Feature: eMSP Server returns credentials

  Scenario: Already registered client can not be registered one more time
    Given CPO is registered in eMSP system
    When CPO post "credentials" in eMSP system with data
    |any|
    Then eMSP responded with HTTP status 405