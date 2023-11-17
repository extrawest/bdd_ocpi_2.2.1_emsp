Feature: CPO Server returns tariffs
  Scenario: The eMSP receives tariffs
    When eMSP GET "tariff"
    And if response is not empty
    Then result is valid
    And result is
      | countryCode   |  DE    |
      | partyId       |  BEC   |
      | id            |  12    |
      | currency      |  USD   |
      | type          |  default   |
      | tariffAltText |  any   |
      | tariffAltUrl  |  any   |
      | minPrice      |  any   |
      | maxPrice      |  any   |
      | elements      | [{"price_components": [{"type": "TIME","price": 2.00,"vat": 10.0,"step_size": 300}]}]  |
      | startDateTime |  any   |
      | endDateTime   |  any   |
      | energyMix     |  any   |
#      | lastUpdated   | 2022-06-05T20:39:09Z | / now()

Feature: CPO Server returns tariffs after date
  Scenario: The eMSP receives tariffs after date
    When eMSP GET "tariffs" from "2022-06-05T20:39:09Z"
    Then result is valid
    And result has 5 tariff

Feature: CPO Server returns tariffs before date
  Scenario: The eMSP receives tariffs before date
    When eMSP GET "tariff" before "2022-06-05T20:39:09Z"
    And if response is not empty
    Then result is valid
    And result has 1 tariff

Feature: CPO Server returns n tariffs starting from any position
  Scenario: The eMSP receives n tariffs starting from any position
    When eMSP GET 5 "tariff" starting 10th
    And if response is not empty
    Then result is valid
    And result has 5 tariffs