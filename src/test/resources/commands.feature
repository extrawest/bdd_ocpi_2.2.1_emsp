Feature: eMSP receives the result of previously sent Command

  Scenario: eMSP is able to receive Commands results
    Given CPO is registered in eMSP system
    When "command_type" path param is "RESERVE_NOW"
    And "unique_id" path param is "1245"
    And CPO post "command" in eMSP system with data
      | result  | ACCEPTED |
      | message | any      |
    Then response is success