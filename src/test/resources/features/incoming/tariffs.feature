Feature: CPO adds, removes or checks the status of a Tariff in the eMSP’s system

  Scenario: CPO removes his Tariff object which is not used any more and will not be used in the future from eMSP system (1)
    Given CPO is registered in eMSP system
    And eMSP has "tariffs" data "db/tariffs.json"
    When CPO with "party_id" "BEC" and "country_code" "DE" removes his "tariff" with "tariff_id" "Green5"
    And response is success
    Then "tariff" is absent
    And eMSP responded with HTTP status 404
    And eMSP responded with OCPI status 2000

  Scenario: CPO removes his Tariff object which is not used any more and will not be used in the future from eMSP system (2)
    Given CPO is registered in eMSP system
    When CPO with "party_id" "BEC" and "country_code" "DE" put "tariff" with "tariff_id" "Green5" in eMSP system with data
      | any |
    And response is success
    When CPO removes his "tariff"
    And response is success
    Then "tariff" is absent

  Scenario: eMSP allows changing only Tariff that are owned by current CPO
    Given CPO is registered in eMSP system
    When CPO with "party_id" "BEC" and "country_code" "US" put "tariff" with "tariff_id" "Green7" in eMSP system with data
      | country_code | US                                                                                   |
      | party_id     | BEC                                                                                  |
      | id           | Green5                                                                               |
      | currency     | any                                                                                  |
      | elements     | [{"price_components": [{"type": "TIME","price": 7.0,"vat": 70.0,"step_size": 150}]}] |
      | last_updated | 2022-06-05T20:39:09                                                                  |
    Then eMSP responded with OCPI status 2001
    And eMSP responded with HTTP status 400

  Scenario: eMSP allows changing only Tariff that are owned by current CPO
    Given CPO is registered in eMSP system
    When CPO with "party_id" "BEC" and "country_code" "US" put "tariff" with "tariff_id" "Green7" in eMSP system with data
      | country_code | DE                                                                                   |
      | party_id     | NOT_BEC                                                                              |
      | id           | 15                                                                                   |
      | currency     | any                                                                                  |
      | elements     | [{"price_components": [{"type": "TIME","price": 2.0,"vat": 10.0,"step_size": 300}]}] |
      | last_updated | 2022-06-05T20:39:09                                                                  |
    Then eMSP responded with HTTP status 400

  Scenario: CPO retrieves his Tariff as it is stored in the eMSP’s system
    Given CPO is registered in eMSP system
    And eMSP has "tariffs" data "db/tariffs.json"
    When CPO with "party_id" "BEC" and "country_code" "DE" checks "tariff" with "tariff_id" "Green5" in eMSP system
    Then "tariff" response is valid and has data
      | country_code    | DE                                                                                                  |
      | party_id        | BEC                                                                                                 |
      | id              | Green5                                                                                              |
      | currency        | any                                                                                                 |
      | type            | any                                                                                                 |
      | tariff_alt_text | any                                                                                                 |
      | tariff_alt_url  | any                                                                                                 |
      | min_price       | any                                                                                                 |
      | max_price       | any                                                                                                 |
      | elements        | [{"price_components":[{"type":"TIME","price":2.0,"vat":10.0,"step_size":300}],"restrictions":null}] |
      | start_date_time | any                                                                                                 |
      | end_date_time   | any                                                                                                 |
      | energy_mix      | any                                                                                                 |
      | last_updated    | any                                                                                                 |

  Scenario: Registered CPO retrieves his Tariff as it is stored in the eMSP’s system
    Given CPO is registered in eMSP system
    When CPO with "party_id" "BEC" and "country_code" "DE" put "tariff" with "tariff_id" "12" in eMSP system with data
      | country_code | DE                                                                                   |
      | party_id     | BEC                                                                                  |
      | id           | 12                                                                                   |
      | currency     | any                                                                                  |
      | elements     | [{"price_components": [{"type": "TIME","price": 2.0,"vat": 10.0,"step_size": 300}]}] |
      | last_updated | 2022-06-05T20:39:09                                                                  |
    Then response is success
    And CPO checks "tariff" in eMSP system
    Then "tariff" response is valid and has data
      | country_code    | DE                                                                                                  |
      | party_id        | BEC                                                                                                 |
      | id              | 12                                                                                                  |
      | currency        | any                                                                                                 |
      | type            | any                                                                                                 |
      | tariff_alt_text | any                                                                                                 |
      | tariff_alt_url  | any                                                                                                 |
      | min_price       | any                                                                                                 |
      | max_price       | any                                                                                                 |
      | elements        | [{"price_components":[{"type":"TIME","price":2.0,"vat":10.0,"step_size":300}],"restrictions":null}] |
      | start_date_time | any   |
      | end_date_time   | any   |
      | energy_mix      | any   |
      | last_updated    | any   |

  Scenario: CPO updates his Tariff in eMSP system
    Given CPO is registered in eMSP system
    When CPO with "party_id" "BEC" and "country_code" "DE" put "tariff" with "tariff_id" "12" in eMSP system with data
      | country_code | DE                                                                                   |
      | party_id     | BEC                                                                                  |
      | id           | 12                                                                                   |
      | currency     | any                                                                                  |
      | elements     | [{"price_components": [{"type": "TIME","price": 2.0,"vat": 10.0,"step_size": 300}]}] |
      | last_updated | 2022-06-05T20:39:09                                                                  |
    And response is success
    When CPO updates "tariff"
      | country_code | DE                                                                                    |
      | party_id     | BEC                                                                                   |
      | id           | 12                                                                                    |
      | currency      | UAH  |
      | elements      | [{"price_components": [{"type": "TIME","price": 2.00,"vat": 10.0,"step_size": 300}]}]  |
      | last_updated   | 2022-06-11T21:39:09 |
    And response is success
    Then CPO checks "tariff" in eMSP system
    And "tariff" response is valid and has data
      | country_code    | DE                                                                                                  |
      | party_id        | BEC                                                                                                 |
      | id              | 12                                                                                                  |
      | currency        | UAH                                                                                                 |
      | type            | any                                                                                                 |
      | tariff_alt_text | any                                                                                                 |
      | tariff_alt_url  | any                                                                                                 |
      | min_price       | any                                                                                                 |
      | max_price       | any                                                                                                 |
      | elements        | [{"price_components":[{"type":"TIME","price":2.0,"vat":10.0,"step_size":300}],"restrictions":null}] |
      | start_date_time | any                                                                                                 |
      | end_date_time   | any                                                                                                 |
      | energy_mix      | any                                                                                                 |
      | last_updated    | any                                                                                                 |


  Scenario: CPO retrieves his Tariff as it is stored in the eMSP’s system (1)
    Given CPO is registered in eMSP system
    When CPO with "party_id" "BEC" and "country_code" "DE" put "tariff" with "tariff_id" "12" in eMSP system with data
      | country_code | DE                                                                                   |
      | party_id     | BEC                                                                                  |
      | id           | 12                                                                                   |
      | currency     | USD                                                                                  |
      | elements     | [{"price_components": [{"type": "TIME","price": 2.0,"vat": 10.0,"step_size": 300}]}] |
      | last_updated | 2022-06-05T20:39:09                                                                  |
    And response is success

  Scenario: CPO retrieves his Tariff as it is stored in the eMSP’s system (2)
    Given CPO is registered in eMSP system
    Given CPO with "party_id" "BEC" and "country_code" "DE" put "tariff" with "tariff_id" "12" in eMSP system with data
      | any |
    And response is success

  Scenario: CPO retrieves his Tariff as it is stored in the eMSP’s system (3)
    Given CPO is registered in eMSP system
    When CPO with "party_id" "BEC" and "country_code" "DE" put "tariff" with "tariff_id" "12" in eMSP system with data
      | country_code | any |
      | party_id     | any |
      | id           | any |
      | currency     | any |
      | elements     | any |
      | last_updated | any |
    And response is success

  Scenario: CPO retrieves his Tariff as it is stored in the eMSP’s system (4)
    Given CPO is registered in eMSP system
    When CPO with "party_id" "BEC" and "country_code" "DE" put "tariff" with "tariff_id" "12" in eMSP system with data
      | country_code    | any |
      | party_id        | any |
      | id              | any |
      | currency        | any |
      | type            | any |
      | tariff_alt_text | any |
      | tariff_alt_url  | any |
      | min_price       | any |
      | max_price       | any |
      | elements        | any |
      | start_date_time | any |
      | end_date_time   | any |
      | energy_mix      | any |
      | last_updated    | any |
    And response is success

  Scenario: CPO retrieves his Tariff as it is stored in the eMSP’s system (5)
    Given CPO is registered in eMSP system
    When CPO with "party_id" "BEC" and "country_code" "DE" put "tariff" with "tariff_id" "12" in eMSP system with data
      | country_code    | DE                                                                                                                          |
      | party_id        | BEC                                                                                                                         |
      | id              | 12                                                                                                                          |
      | currency        | USD                                                                                                                         |
      | type            | PROFILE_GREEN                                                                                                               |
      | tariff_alt_text | [{"language": "en","text": "2.00 euro p/hour including VAT."}, {"language": "nl","text": "2.00 euro p/uur inclusief BTW."}] |
      | tariff_alt_url  | "https://company.com/tariffs/13"                                                                                            |
      | min_price       | {"excl_vat": 0.50,"incl_vat": 0.55}                                                                            |
      | max_price       | {"excl_vat": 0.80,"incl_vat": 0.95}                                                                            |
      | elements        | [{"price_components": [{"type": "TIME","price": 2.0,"vat": 10.0,"step_size": 300}]}]                                        |
      | start_date_time | 2022-06-08T20:39:09                                                                                                         |
      | end_date_time   | 2022-06-11T20:39:09                                                                                                         |
      | energy_mix      | {"is_green_energy": true}                                                                                     |
      | last_updated    | 2022-06-10T20:39:09                                                                                                         |
    And response is success