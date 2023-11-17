Feature: OCPI Server supports 2.2.1 version
  Scenario: CPO checks OCPI 2.2.1 version is supported
    When CPO checks "version" in eMSP system
    And list of versions response is valid and contains values
      | version |             url                           |
      | 2.2.1   | http://localhost:8080/emsp/api/versions/details?version=2.2.1  |


  Scenario: CPO checks OCPI 2.2.1 version's details
    When CPO checks "version" in eMSP system
    And list of versions response is valid and contains values
      | version |             url                           |
      | 2.2.1   | /emsp/api/versions/details?version=2.2.1  |
    Then CPO checks "version details" with "version" "2.2.1" in eMSP system
    And "version details" response is valid and has data
      | version   | 2.2.1 |
      | endpoints | [{"identifier": "tariffs", "role": "RECEIVER", "url": "/emsp/api/2.2.1/tariffs"}] |

  Scenario: CPO initilize registration in eMSP system
    Given eMSP has "parties" data "db/parties.json"