#1. The CDR object shall always contain information like Location, EVSE, Tariffs and Token as they were at the start of the charging
#session.

Feature: CPO can add or retrieve Charging Details Records

  Scenario: eMSP push new CDR to eMSP
    When CPO post "cdr" with "id" "5" in eMSP system with data
      | id               | 5                                                                                                                                                                                                                                                                                                                                                                      |
      | country_code     | DE                                                                                                                                                                                                                                                                                                                                                                     |
      | party_id         | BEC                                                                                                                                                                                                                                                                                                                                                                    |
      | start_date_time  | 2022-06-05T20:39:09                                                                                                                                                                                                                                                                                                                                                    |
      | end_date_time    | 2022-06-05T20:39:09                                                                                                                                                                                                                                                                                                                                                    |
      | cdr_token        | {"uid": "012345678","type": "RFID","contract_id": "DE8ACC12E46L89"}                                                                                                                                                                                                                                                                                                    |
      | auth_method      | WHITELIST                                                                                                                                                                                                                                                                                                                                                              |
      | cdr_location     | {"id": "LOC1","name": "Gent Zuid","address": "F.Rooseveltlaan 3A","city": "Gent","postal_code": "9000","country": "BEL","coordinates": {"latitude": "3.729944","longitude": "51.047599"},"evse_uid": "3256","evse_id": "BE*BEC*E041503003","connector_id": "1","connector_standard": "IEC_62196_T2","connector_format": "SOCKET","connector_power_type": "AC_1_PHASE"} |
      | currency         | EUR                                                                                                                                                                                                                                                                                                                                                                    |
      | charging_periods | [{"start_date_time": "2015-06-29T21:39:09Z","dimensions": [{"type": "TIME","volume": 1.973}],"tariff_id": "12"}]                                                                                                                                                                                                                                                       |
      | total_cost       | {"excl_vat": 4.00,"incl_vat": 4.40}                                                                                                                                                                                                                                                                                                                                    |
      | total_energy     | 15.342                                                                                                                                                                                                                                                                                                                                                                 |
      | last_updated     | 2022-06-05T20:39:09                                                                                                                                                                                                                                                                                                                                                    |
      | total_time       | 15                                                                                                                                                                                                                                                                                                                                                                     |
    Then response is success

  Scenario: CPO checks CDR in eMSP system
    Given eMSP has "cdrs" data "db/cdrs.json"
    When CPO checks "cdr" with "id" "1" in eMSP system
    Then "cdr" response is valid and has data
      | id                 | 1                                                                                                                                                                                                                                                                                                                                                                      |
      | country_code       | DE                                                                                                                                                                                                                                                                                                                                                                     |
      | party_id           | BEC                                                                                                                                                                                                                                                                                                                                                                    |
      | start_date_time    | 2022-06-06T20:39:09                                                                                                                                                                                                                                                                                                                                                    |
      | end_date_time      | 2022-06-06T20:39:09                                                                                                                                                                                                                                                                                                                                                    |
      | cdr_token          | {"uid": "012345678","type": "RFID","contract_id": "DE8ACC12E46L89"}                                                                                                                                                                                                                                                                                                    |
      | auth_method        | WHITELIST                                                                                                                                                                                                                                                                                                                                                              |
      | cdr_location       | {"id": "LOC1","name": "Gent Zuid","address": "F.Rooseveltlaan 3A","city": "Gent","postal_code": "9000","country": "BEL","coordinates": {"latitude": "3.729944","longitude": "51.047599"},"evse_uid": "3256","evse_id": "BE*BEC*E041503003","connector_id": "1","connector_standard": "IEC_62196_T2","connector_format": "SOCKET","connector_power_type": "AC_1_PHASE"} |
      | currency           | EUR                                                                                                                                                                                                                                                                                                                                                                    |
      | charging_periods   | [{"start_date_time": "2022-06-06T17:39:09","dimensions": [{"type": "TIME","volume": 1.973}],"tariff_id": "12"}]                                                                                                                                                                                                                                                        |
      | total_cost         | {"excl_vat": 4.00,"incl_vat": 4.40}                                                                                                                                                                                                                                                                                                                                    |
      | total_energy       | 15.342                                                                                                                                                                                                                                                                                                                                                                 |
      | last_updated       | 2022-06-06T20:39:09                                                                                                                                                                                                                                                                                                                                                    |
      | total_time         | 1.973                                                                                                                                                                                                                                                                                                                                                                  |
      | total_parking_time | any                                                                                                                                                                                                                                                                                                                                                                    |
      | total_energy_cost  | any                                                                                                                                                                                                                                                                                                                                                                    |
      | total_parking_cost | any                                                                                                                                                                                                                                                                                                                                                                    |
      | tariffs            | any                                                                                                                                                                                                                                                                                                                                                                    |
      | total_time_cost    | any                                                                                                                                                                                                                                                                                                                                                                    |

  Scenario: CPO not allowed to change existed CDR
    When CPO post "cdr" with "id" "1" in eMSP system with data
      | id               | 1                                                                                                                                                                                                                                                                                                                                                                      |
      | country_code     | DE                                                                                                                                                                                                                                                                                                                                                                     |
      | party_id         | BEC                                                                                                                                                                                                                                                                                                                                                                    |
      | start_date_time  | 2022-06-06T20:39:09                                                                                                                                                                                                                                                                                                                                                    |
      | end_date_time    | 2022-06-06T20:39:09                                                                                                                                                                                                                                                                                                                                                    |
      | cdr_token        | {"uid": "012345678","type": "RFID","contract_id": "DE8ACC12E46L89"}                                                                                                                                                                                                                                                                                                    |
      | auth_method      | WHITELIST                                                                                                                                                                                                                                                                                                                                                              |
      | cdr_location     | {"id": "LOC1","name": "Gent Zuid","address": "F.Rooseveltlaan 3A","city": "Gent","postal_code": "9000","country": "BEL","coordinates": {"latitude": "3.729944","longitude": "51.047599"},"evse_uid": "3256","evse_id": "BE*BEC*E041503003","connector_id": "1","connector_standard": "IEC_62196_T2","connector_format": "SOCKET","connector_power_type": "AC_1_PHASE"} |
      | currency         | EUR                                                                                                                                                                                                                                                                                                                                                                    |
      | charging_periods | [{"start_date_time": "2015-06-29T21:39:09Z","dimensions": [{"type": "TIME","volume": 1.973}],"tariff_id": "12"}]                                                                                                                                                                                                                                                       |
      | total_cost       | {"excl_vat": 4.00,"incl_vat": 4.40}                                                                                                                                                                                                                                                                                                                                    |
      | total_energy     | 15.342                                                                                                                                                                                                                                                                                                                                                                 |
      | last_updated     | 2022-06-05T20:39:09                                                                                                                                                                                                                                                                                                                                                    |
      | total_time       | 15                                                                                                                                                                                                                                                                                                                                                                     |
    Then eMSP responded with HTTP status 400
    And eMSP responded with OCPI status 2000


#    When CPO checks "cdr" with "id" "1" in eMSP system
#    Then "cdr" response is valid and has data
#      | id              | 1                                                                                                                                                                                                                                                                                                                                                              |
#      | countryCode     | DE                                                                                                                                                                                                                                                                                                                                                             |
#      | partyId         | BEC                                                                                                                                                                                                                                                                                                                                                            |
#      | startDateTime   | 2022-06-05T20:39:09                                                                                                                                                                                                                                                                                                                                            |
#      | endDateTime     | 2022-06-05T20:39:09                                                                                                                                                                                                                                                                                                                                            |
#      | cdrToken        | {"uid": "012345678","type": "RFID","contractId": "DE8ACC12E46L89"}                                                                                                                                                                                                                                                                                             |
#      | authMethod      | WHITELIST                                                                                                                                                                                                                                                                                                                                                      |
#      | cdrLocation     | {"id": "LOC1","name": "Gent Zuid","address": "F.Rooseveltlaan 3A","city": "Gent","postalCode": "9000","country": "BEL","coordinates": {"latitude": "3.729944","longitude": "51.047599"},"evseUid": "3256","evseId": "BE*BEC*E041503003","connectorId": "1","connectorStandard": "IEC_62196_T2","connectorFormat": "SOCKET","connectorPowerType": "AC_1_PHASE"} |
#      | currency        | EUR                                                                                                                                                                                                                                                                                                                                                            |
#      | tariffs         | any                                                                                                                                               |
#      | chargingPeriods | [{"startDateTime": "2015-06-29T21:39:09Z","dimensions": [{"type": "TIME","volume": 1.973}],"tariffId": "12"}]                                                                                                                                                                                                                                                  |
#      | totalCost       | {"exclVat": 4.00,"inclVat": 4.40}                                                                                                                                                                                                                                                                                                                              |
#      | totalEnergy     | 15.342                                                                                                                                                                                                                                                                                                                                                         |
#      | totalTimeCost   | {"exclVat": 4.00,"inclVat": 4.40}                                                                                                                                                                                                                                                                                                                              |
#      | lastUpdated     | 2022-06-05T20:39:09                                                                                                                                                                                                                                                                                                                                            |



#
#      | id              | 1                                                                                                                                                                                                                                                                                                                                                              |
#      | countryCode     | DE                                                                                                                                                                                                                                                                                                                                                             |
#      | partyId         | BEC                                                                                                                                                                                                                                                                                                                                                            |
#      | startDateTime   | 2022-06-05T20:39:09                                                                                                                                                                                                                                                                                                                                            |
#      | endDateTime     | 2022-06-05T20:39:09                                                                                                                                                                                                                                                                                                                                            |
#      | cdrToken        | {"uid": "012345678","type": "RFID","contractId": "DE8ACC12E46L89"}                                                                                                                                                                                                                                                                                             |
#      | authMethod      | WHITELIST                                                                                                                                                                                                                                                                                                                                                      |
#      | cdrLocation     | {"id": "LOC1","name": "Gent Zuid","address": "F.Rooseveltlaan 3A","city": "Gent","postalCode": "9000","country": "BEL","coordinates": {"latitude": "3.729944","longitude": "51.047599"},"evseUid": "3256","evseId": "BE*BEC*E041503003","connectorId": "1","connectorStandard": "IEC_62196_T2","connectorFormat": "SOCKET","connectorPowerType": "AC_1_PHASE"} |
#      | currency        | EUR                                                                                                                                                                                                                                                                                                                                                            |
#      | tariffs         | [{"countryCode": "BE","partyId": "BEC","id": "12","currency": "EUR","elements": [{"priceComponents": [{"type": "TIME","price": 2.00,"vat": 10.0,"stepSize": 300}]}],"lastUpdated": "2015-02-02T14:15:01Z"}]                                                                                                                                                    |
#      | chargingPeriods | [{"startDateTime": "2015-06-29T21:39:09Z","dimensions": [{"type": "TIME","volume": 1.973}],"tariffId": "12"}]                                                                                                                                                                                                                                                  |
#      | totalCost       | {"exclVat": 4.00,"inclVat": 4.40}                                                                                                                                                                                                                                                                                                                              |
#      | totalEnergy     | 15.342                                                                                                                                                                                                                                                                                                                                                         |
#      | totalTimeCost   | {"exclVat": 4.00,"inclVat": 4.40}                                                                                                                                                                                                                                                                                                                              |
#      | lastUpdated     | 2022-06-05T20:39:09