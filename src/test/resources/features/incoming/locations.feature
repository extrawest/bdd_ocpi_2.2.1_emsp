Feature: CPO can add or retrieve Locations

  Scenario: CPO checks Location in eMSP system
    Given eMSP has "locations" data "db/locations.json"
    When "location_id" path param is "LOC1"
    And "party_id" path param is "BEC"
    And "country_code" path param is "BE"
    When CPO checks "location" in eMSP system
    Then "location" response is valid and has data
      | id           | LOC1                                           |
      | country_code | BE                                             |
      | party_id     | BEC                                            |
      | country      | BEL                                            |
      | publish      | true                                           |
      | last_updated | 2022-06-06T20:39:09                            |
      | address      | F.Rooseveltlaan 3A                             |
      | city         | Gent                                           |
      | coordinates  | {"latitude": 51.047599, "longitude": 3.729944} |
      | time_zone    | Europe/Brussels                                |

  Scenario: CPO add Location in eMSP system
    When "location_id" path param is "LOC1"
    And "party_id" path param is "BEC"
    And "country_code" path param is "BE"
    And CPO updates "location"
      | id           | LOC1                                           |
      | country_code | BE                                             |
      | party_id     | BEC                                            |
      | country      | BEL                                            |
      | publish      | true                                           |
      | last_updated | 2022-06-06T20:39:09                            |
      | address      | F.Rooseveltlaan 3A                             |
      | city         | Gent                                           |
      | coordinates  | {"latitude": 51.047599, "longitude": 3.729944} |
      | time_zone    | Europe/Brussels                                |
    And response is success