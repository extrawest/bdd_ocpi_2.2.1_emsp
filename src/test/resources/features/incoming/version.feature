Feature: OCPI Server supports 2.2.1 version
  Scenario: CPO checks OCPI 2.2.1 version is supported
    When CPO checks "version" in eMSP system
    And list of versions response is valid and contains values
      | version |             url                           |
      | 2.2.1   | http://localhost:8080/emsp/api/versions/details?version=2.2.1  |