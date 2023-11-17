Feature: CPO can retrieve tokens

#  1. Check that whitelist "NEVER" not present in response

  Scenario: CPO retrieves Tokens in eMSP system
    Given CPO is registered in eMSP system
    And eMSP has "tokens" data "db/tokens.json"
    When "date_from" query param is "2022-01-01T07:15:10"
    And "date_to" query param is "2025-10-24T07:15:10"
    And "limit" query param is "3"
#    And "offset" path param is "10"
    And CPO checks "tokens" in eMSP system
    Then list of tokens response is valid and is
      | country_code | party_id | uid                      | type | contract_id    | visual_number     | issuer       | group_id        | valid | whitelist | last_updated        |
      | NL           | TNM      | 65383539b7e8d40c369f9931 | RFID | NL8ACC12E46L89 | DF000-2001-8999-1 | TheNewMotion | DF000-2001-8999 | true  | ALWAYS    | 2022-06-05T20:39:09 |
      | BE           | BEC      | 65383539b7e8d40c369f9932 | RFID | NL8ACC12E46L89 | DF000-2001-8999-1 | TheNewMotion | DF000-2001-8999 | true  | ALWAYS    | 2022-06-05T20:39:09 |
      | CE           | CET      | 65383539b7e8d40c369f9933 | RFID | NL8ACC12E46L89 | DF000-2001-8999-1 | TheNewMotion | DF000-2001-8999 | true  | ALWAYS    | 2022-06-05T20:39:09 |


    #POST Authorize request to check if a Token is known by the eMSP, and if it is valid
  Scenario: CPO authorize Token in eMSP system in real-time (1)
    Given eMSP has "tokens" data "db/tokens.json"
    When "token_uid" path param is "65383539b7e8d40c369f9931"
    And CPO post "location reference" with "type" "RFID" in eMSP system with data
      | location_id | LOC1         |
      | evse_uids   | [3256, 3257] |
    Then "authorization info" response is valid and has data
      | allowed                 | ALLOWED |
      | token                   | any     |
      | authorization_reference | any     |
      | location                | any     |


  Scenario: CPO authorize Token in eMSP system in real-time (2)
    Given eMSP has "tokens" data "db/tokens.json"
    When "token_uid" path param is "65383539b7e8d40c369f9931"
    And CPO post "location reference" with "type" "RFID" in eMSP system with data
      | location_id | any         |
      | evse_uids   | any |
    Then "authorization info" response is valid and has data
      | allowed                 | ALLOWED |
      | token                   | any     |
      | authorization_reference | any     |
      | location                | any     |

  Scenario: CPO authorize Token in eMSP system in real-time (2)
    Given eMSP has "tokens" data "db/tokens.json"
    When "token_uid" path param is "65383539b7e8d40c369f9931"
    And CPO post "authorize" with "type" "RFID" in eMSP system with data
      | location_id | any         |
    Then "authorization info" response is valid and has data
      | allowed                 | ALLOWED |
      | token                   | any     |
      | authorization_reference | any     |
      | location                | any     |