Feature: CPO can add or retrieve Connectors

  Scenario: CPO checks connector in eMSP system
    Given CPO is registered in eMSP system
    And eMSP has "locations" data "db/locations.json"
    When "location_id" path param is "LOC1"
    And "evse_uid" path param is "3256"
    And "party_id" path param is "BEC"
    And "country_code" path param is "BE"
    And "connector_id" path param is "L1"
    And CPO checks "connector" in eMSP system
    Then "connector" response is valid and has data
      | id           | L1                  |
      | standard     | IEC_62196_T2        |
      | format       | CABLE               |
      | power_type   | AC_3_PHASE          |
      | max_voltage  | 220                 |
      | max_amperage | 16                  |
      | tariff_ids   | 11                  |
      | last_updated | 2022-06-06T20:39:09 |

  Scenario: CPO add EVSE in eMSP system
    Given CPO is registered in eMSP system
    And eMSP has "locations" data "db/locations.json"
    When "location_id" path param is "LOC1"
    And "evse_uid" path param is "3256"
    And "party_id" path param is "BEC"
    And "country_code" path param is "BE"
    And "connector_id" path param is "L1"
    And CPO updates "connector"
      | id           | L1                  |
      | standard     | IEC_62196_T2        |
      | format       | CABLE               |
      | power_type   | AC_3_PHASE          |
      | max_voltage  | 220                 |
      | max_amperage | 16                  |
      | tariff_ids   | 11                  |
      | last_updated | 2022-06-06T20:39:09 |
    Then response is success