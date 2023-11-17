Feature: CPO can add, change charging Session or update not completed charging Session

  Scenario: eMSP does not allow to remove session
    Given eMSP has "sessions" data "db/sessions.json"
    When CPO with "party_id" "BEC" and "country_code" "DE" removes his "session" with "session_id" "1"
    Then eMSP responded with HTTP status 405
    And CPO checks "session" in eMSP system
    And "session" response is valid and has data
      | country_code    | DE                                                                                                                              |
      | party_id        | BEC                                                                                                                             |
      | id              | 1                                                                                                                               |
      | currency        | any                                                                                                                             |
      | last_updated    | 2022-06-05T20:39:09                                                                                                             |
      | start_date_time | 2022-06-05T20:39:09                                                                                                             |
      | kwh             | any                                                                                                                             |
      | cdr_token       | {"country_code": "BE","party_id": "DE","uid": "8b25d516-dc29-4934-ab16-424f1dc33d06","type": "RFID","contract_id": "contract1"} |
      | auth_method     | any                                                                                                                             |
      | location_id     | any                                                                                                                             |
      | evse_uid        | any                                                                                                                             |
      | connector_id    | any                                                                                                                             |
      | status          | INVALID                                                                                                                         |


  Scenario: eMSP does not allow to remove session
    Given CPO with "party_id" "BEC" and "country_code" "DE" put "session" with "session_id" "1" in eMSP system with data
      | country_code     | DE                                                                                                                              |
      | party_id         | BEC                                                                                                                             |
      | id               | 1                                                                                                                               |
      | currency         | any                                                                                                                             |
      | last_updated     | 2022-06-05T20:39:09                                                                                                             |
      | cdr_token        | {"country_code": "BE","party_id": "DE","uid": "8b25d516-dc29-4934-ab16-424f1dc33d06","type": "RFID","contract_id": "contract1"} |
      | location_id      | any                                                                                                                             |
      | auth_method      | any                                                                                                                             |
      | connector_id     | any                                                                                                                             |
      | evse_uid         | any                                                                                                                             |
      | start_date_time  | 2022-06-05T20:39:09                                                                                                             |
      | kwh              | any                                                                                                                             |
      | status           | INVALID                                                                                                                         |
      | charging_periods | any                                                                                                                             |
    When CPO removes his "session"
    And response is success
    And eMSP responded with HTTP status 405
    Then CPO checks "session" in eMSP system
    And eMSP responded with HTTP status 200
    And eMSP responded with OCPI status 1000

  Scenario: CPO checks session
    Given eMSP has "sessions" data "db/sessions.json"
    And CPO with "party_id" "BEC" and "country_code" "DE" checks "session" with "session_id" "1" in eMSP system
    Then response is success

  Scenario: eMSP allows to add and update session (only required params is request)
    Given CPO with "party_id" "BEC" and "country_code" "DE" put "session" with "session_id" "1" in eMSP system with data
      | country_code    | DE                                                                                                                              |
      | party_id        | BEC                                                                                                                             |
      | id              | 1                                                                                                                               |
      | currency        | USD                                                                                                                             |
      | last_updated    | 2022-06-05T20:39:09                                                                                                             |
      | start_date_time | 2022-06-05T20:39:09                                                                                                             |
      | kwh             | 50.0                                                                                                                            |
      | cdr_token       | {"country_code": "BE","party_id": "DE","uid": "8b25d516-dc29-4934-ab16-424f1dc33d06","type": "RFID","contract_id": "contract1"} |
      | auth_method     | AUTH_REQUEST                                                                                                                    |
      | location_id     | 7                                                                                                                               |
      | evse_uid        | 5d7db911-dfc0-4774-968a-276b7bce14bf                                                                                            |
      | connector_id    | connector-1a                                                                                                                    |
      | status          | INVALID                                                                                                                         |
    Then response is success

  Scenario: eMSP allows to add and update session (any for required)
    Given CPO with "party_id" "BEC" and "country_code" "DE" put "session" with "session_id" "1" in eMSP system with data
      | country_code    | DE  |
      | party_id        | BEC |
      | id              | 1   |
      | currency        | any |
      | last_updated    | any |
      | start_date_time | any |
      | kwh             | any |
      | cdr_token       | any |
      | auth_method     | any |
      | location_id     | any |
      | evse_uid        | any |
      | connector_id    | any |
      | status          | any |
    Then response is success

  Scenario: eMSP allows to add and update session (all params in request)
    Given CPO with "party_id" "BEC" and "country_code" "DE" put "session" with "session_id" "1" in eMSP system with data
      | country_code            | DE                                                                                                                                   |
      | party_id                | BEC                                                                                                                                  |
      | id                      | 1                                                                                                                                    |
      | currency                | USD                                                                                                                                  |
      | last_updated            | 2022-06-05T20:39:09                                                                                                                  |
      | start_date_time         | 2022-06-05T20:39:09                                                                                                                  |
      | kwh                     | 50.0                                                                                                                                 |
      | cdr_token               | {"country_code": "BE","party_id": "DE","uid": "8b25d516-dc29-4934-ab16-424f1dc33d06","type": "RFID","contract_id": "contract1"}      |
      | auth_method             | AUTH_REQUEST                                                                                                                         |
      | location_id             | 7                                                                                                                                    |
      | evse_uid                | 5d7db911-dfc0-4774-968a-276b7bce14bf                                                                                                 |
      | connector_id            | connector-1a                                                                                                                         |
      | status                  | INVALID                                                                                                                              |
      | end_date_time           | 2022-06-06T20:39:09                                                                                                                  |
      | authorization_reference | dffdg                                                                                                                                |
      | meter_id                | 1                                                                                                                                    |
      | charging_periods        | [{"start_date_time": "2020-03-09T10:17:09Z","dimensions": [{"type": "ENERGY","volume": 120}, {"type": "MAX_CURRENT","volume": 30}]}] |
      | total_cost              | {"excl_vat": 2.5}                                                                                                                    |
    Then response is success

  Scenario: eMSP allows to add and update session (any for all)
    Given CPO with "party_id" "BEC" and "country_code" "DE" put "session" with "session_id" "1" in eMSP system with data
      | country_code            | DE  |
      | party_id                | BEC |
      | id                      | 1   |
      | currency                | any |
      | last_updated            | any |
      | start_date_time         | any |
      | kwh                     | any |
      | cdr_token               | any |
      | auth_method             | any |
      | location_id             | any |
      | evse_uid                | any |
      | connector_id            | any |
      | status                  | any |
      | end_date_time           | any |
      | authorization_reference | any |
      | meter_id                | any |
      | charging_periods        | any |
      | total_cost              | any |
    Then response is success