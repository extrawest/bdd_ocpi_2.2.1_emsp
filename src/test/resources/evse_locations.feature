Feature: CPO can add or retrieve EVSE

  Scenario: CPO checks EVSE in eMSP system
    Given CPO is registered in eMSP system
    And eMSP has "locations" data "db/locations.json"
    When "evse_uid" path param is "3256"
    And "party_id" path param is "BEC"
    And "country_code" path param is "BE"
    And "location_id" path param is "LOC1"
    And CPO checks "evse" in eMSP system
    Then "evse" response is valid and has data
      | uid                | 3256                                                                                                                                                                                     |
      | evse_id            | BE*BEC*E041503001                                                                                                                                                                        |
      | status             | AVAILABLE                                                                                                                                                                                |
      | capabilities       | ["RESERVABLE"]                                                                                                                                                                           |
      | connectors         | [{"id": "L1","standard": "IEC_62196_T2","format": "CABLE","power_type": "AC_3_PHASE","max_voltage": 220,"max_amperage": 16,"tariff_ids": ["11"],"last_updated": "2022-06-06T20:39:09Z"}] |
      | physical_reference | 1                                                                                                                                                                                        |
      | floor_level        | -1                                                                                                                                                                                       |
      | last_updated       | 2015-06-28T08:12:01                                                                                                                                                                      |

  Scenario: CPO add EVSE in eMSP system
    Given CPO is registered in eMSP system
    And eMSP has "locations" data "db/locations.json"
    When "location_id" path param is "LOC1"
    And "evse_uid" path param is "3256"
    And "party_id" path param is "BEC"
    And "country_code" path param is "BE"
    And CPO updates "evse"
      | uid                | 3256                                                                                                                                                                                    |
      | evse_id            | BE*BEC*E041503001                                                                                                                                                                       |
      | status             | AVAILABLE                                                                                                                                                                               |
      | capabilities       | ["RESERVABLE"]                                                                                                                                                                          |
      | connectors         | [{"id": "1","standard": "IEC_62196_T2","format": "CABLE","power_type": "AC_3_PHASE","max_voltage": 220,"max_amperage": 16,"tariff_ids": ["11"],"last_updated": "2022-06-06T20:39:09Z"}] |
      | physical_reference | 1                                                                                                                                                                                       |
      | floor_level        | -1                                                                                                                                                                                      |
      | last_updated       | 2015-06-28T08:12:01                                                                                                                                                                     |
    And response is success