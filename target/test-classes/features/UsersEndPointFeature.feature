Feature: Users Rest API test

  @users @getuser
  Scenario Outline: Obtain user details with a valid username
    Given the user is already registered an account with "<userName>"
    When a client app attempts to request user "<userName>" details
    Then the success response have <statusCode> "<name>" "<userName>" "<email>" "<dateOfBirth>" "<superPower>" "<isAdmin>"

    Examples:
      | name       | userName | email                | dateOfBirth | superPower      | isAdmin | statusCode |
      | The Admin  | admin    | admin@somewhere.com  | 1984-09-18  | Create all.     | true    | 200        |
      | The Tester | tester   | tester@somewhere.com | 2014-07-15  | Test.           | false   | 200        |
      | The Dev    | dev      | dev@somewhere.com    | 1999-10-10  | Code and debug. | false   | 200        |

  @users @getaccess
  Scenario Outline: get access with valid credentials
    Given the user is already registered an account with "<userName>"
    When a client app attempts to get access using "<userName>" "<password>"
    Then the success response have <statusCode> "<name>" "<userName>" "<email>" "<dateOfBirth>" "<superPower>" "<isAdmin>"

    Examples:
      | name       | userName | password | email                | dateOfBirth | superPower      | isAdmin | statusCode |
      | The Admin  | admin    | power    | admin@somewhere.com  | 1984-09-18  | Create all.     | true    | 200        |
      | The Tester | tester   | maniac   | tester@somewhere.com | 2014-07-15  | Test.           | false   | 200        |
      | The Dev    | dev      | wizard   | dev@somewhere.com    | 1999-10-10  | Code and debug. | false   | 200        |

  @users @getuser
  Scenario Outline: Validate error message while obtaining invalid user
    When a client app attempts to request user "<userName>" details
    Then the error response have <statusCode> "<error>" "<message>" "<path>"
    Examples:
      | userName | error     | message                          | path                      | statusCode |
      | dev1     | Not Found | Username dev1 does not exist.    | uri=/api/v1/users/details | 404        |
      | admin1   | Not Found | Username admin1 does not exist.  | uri=/api/v1/users/details | 404        |
      | tester1  | Not Found | Username tester1 does not exist. | uri=/api/v1/users/details | 404        |

  @users @getuser
  Scenario Outline: Obtain user details with a valid username and "<scenario>" after registration
    Given  register account with "<name>" "<userName>" "<password>" "<email>" "<dateOfBirth>" "<superPower>" "<isAdmin>" <createStatusCode>
    When a client app attempts to request user "<userName>" details
    Then the success response have <getStatusCode> "<name>" "<userName>" "<email>" "<dateOfBirth>" "<superPower>" "<isAdmin>"
    Examples:
      | scenario      | name          | userName     | password | email                      | dateOfBirth | superPower | isAdmin | createStatusCode | getStatusCode |
      | Valid details | Scrum master  | scrummaster  | w0r!d    | scrummaster@somewhere.com  | 1985-01-01  | scrum      | false   | 201              | 200           |
      | Valid details | Finance Admin | financeAdmin | w0r!d    | financeAdmin@somewhere.com | 1982-01-01  | Super all. | true    | 201              | 200           |

  @users @new
  Scenario Outline:  User registration with invalid detail "<scenario>"
    Given  register account with "<name>" "<userName>" "<password>" "<email>" "<dateOfBirth>" "<superPower>" "<isAdmin>" <createStatusCode>
    When a client app attempts to request user "<userName>" details
    Then verify <getStatusCode>
    Examples:
      | scenario           | name        | userName   | password | email                     | dateOfBirth | superPower | isAdmin | createStatusCode | getStatusCode |
      | Invalid email id   | Super Admin | sheldon    | w0r!d    | superadmin                | 1982-01-01  | Super all. | true    | 400              | 404           |
      | Future date in DOB | Super Admin | penny      | w0r!d    | superadmin3@somewhere.com | 2051-01-02  | Super all. | true    | 400              | 404           |
      | Blank password     | Super Admin | bernadedth |          | superadmin4@somewhere.com | 1982-01-01  | Super all. | true    | 400              | 404           |

  @users @getall
  Scenario Outline: Get all user with "<userName>"
    Given the user is already registered an account with "<userName>"
    When a client app attempts to request all user details with "<userName>" "<password>"
    Then verify <statusCode>
    Examples:
      | userName | password | statusCode |
      | admin    | power    | 200        |
      | tester   | maniac   | 403        |
      | dev      | wizard   | 403        |

  @users @delete
  Scenario Outline: Delete user
    Given register account with "<name>" "<userName>" "<password>" "<email>" "<dateOfBirth>" "<superPower>" "<isAdmin>" <createStatusCode>
    When admin delete the "<usersName>"
    Then verify "<usersName>" is deleted
    Examples:
      | name         | userName    | password | email                     | dateOfBirth | superPower | isAdmin | createStatusCode |
      | Scrum master | scrummaster | w0r!d    | scrummaster@somewhere.com | 1985-01-01  | scrum      | false   | 201              |

  @users @update
  Scenario Outline: Update user
    Given  register account with "<name>" "<userName>" "<password>" "<email>" "<dateOfBirth>" "<superPower>" "<isAdmin>" <statusCode>
    When update the existing user details "<userName>" "<password>" "<newName>" "<newEmail>" "<newPassword>" "<newDateOfBirth>" "<newSuperPower>" "<newIsAdmin>"
    Then Validate the "<userName>" is updated with "<newName>" "<newEmail>" "<newPassword>" "<newDateOfBirth>" "<newSuperPower>" "<newIsAdmin>"
    And admin delete the "<userName>"
    Examples:
      | name      | userName | password | email                  | dateOfBirth | superPower | isAdmin | statusCode | newName         | newEmail                    | newPassword | newDateOfBirth | newSuperPower | newIsAdmin |
      | IT Admin2 | itadmin2 | w0r!d    | itadmin2@somewhere.com | 1989-01-01  | it admin   | false   | 201        | Senior IT Admin | senioritadmin@somewhere.com | w0r!dnew    | 1990-01-01     | IT Manager    | true       |

  @users @new
  Scenario Outline: Register already registered user and verify error
    Given  register account with "<name>" "<userName>" "<password>" "<email>" "<dateOfBirth>" "<superPower>" "<isAdmin>" <createStatusCode>
    When  register user with "<name>" "<userName>" "<password>" "<email>" "<dateOfBirth>" "<superPower>" "<isAdmin>" <recreateStatusCode>
    Then verify <recreateStatusCode> and error "Username or email already registered. Please select different values."
    And admin delete the "<userName>"
    Examples:
      | name         | userName    | password | email                     | dateOfBirth | superPower | isAdmin | createStatusCode | recreateStatusCode |
      | Scrum master | scrummaster | w0r!d    | scrummaster@somewhere.com | 1985-01-01  | scrum      | false   | 201              | 403                |

  @users @update
  Scenario Outline: Update userName and email of new user with userName already Registered
    Given  register account with "<name1>" "<userName1>" "<password>" "<email1>" "<dateOfBirth1>" "<superPower>" "<isAdmin>" <createStatusCode>
    Given  register account with "<name2>" "<userName2>" "<password>" "<email2>" "<dateOfBirth2>" "<superPower>" "<isAdmin>" <createStatusCode>
    When "<userName2>" updated with "<userName1>" "<name1>" "<password>" "<email1>" "<dateOfBirth1>" "<superPower>" "<isAdmin>" <updateStatus>
    And verify <updateStatus>
    Examples:
      | name1       | userName1 | email1               | name2    | userName2 | email2                | dateOfBirth1 | dateOfBirth2 | superPower | isAdmin | password | createStatusCode | updateStatus |
      | Devops user | devops    | devops@somewhere.com | Info sec | infosec   | infosec@somewhere.com | 1985-01-01   | 1985-01-11   | Infra      | false   | w0r!d    | 201              | 400          |