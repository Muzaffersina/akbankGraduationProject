# GLOBAL BANKING SYSTEM :bank:
* In the global bank system, a system has been created where users can open accounts in different banks.
* There are admin and normal users in this system. But users "admin" and "account_manager" must be registered in the database.
* admin user; can create bank, delete account, open account and enable/disable regular users
* account_manager user; can delete account, create account and enable/disable regular user
* Normal user can only open account. (Account type must be Try, Usd, Eur, Gau)
* Transaction fees apply if bank accounts are different. (TRY = 3 , USD = 1 , EUR = 1 , GAU = 0) 
# 

:exclamation: :exclamation: Each user can log into the system with their own user name and can make transactions with their own account number.

:exclamation: :exclamation: You have to write your Collection API Key in exchangeServis.properties.

:star: Apache kafka was used for account logs.

:star: myBatis was used to access the database. (MyBatis couples objects with stored procedures or SQL statements using an XML descriptor or annotations)

:star: Jwt was used for Web Security;

 **id**        | **username**                     | **password**        | **enabled** | **authorities** |
| ------------- |:-------------                  | :------------          | :------------| :------------| 
| 1          | admin                  |  123456(With Encode)                 | true|  CREATE_BANK,CREATE_ACCOUNT,ACTIVATE_DEACTIVATE_USER,REMOVE_ACCOUNT |
| 2         | account_manager                   |  1234567(With Encode)                 | true|  CREATE_ACCOUNT,ACTIVATE_DEACTIVATE_USER,REMOVE_ACCOUNT |
| 3         | newUser                   |  password(With Encode)                 | false|  CREATE_ACCOUNT|

* :star:  users table should be as given in the table


#

:exclamation:  MySql bankingsystem database ; 

* banks table

```
    {
        int id PRIMARY KEY AUTO_INCREMENT,
        name varchar NOT NULL UNIQUE
    }
```
* users table
```
{
    int id PRIMARY KEY AUTO_INCREMENT,
    String username NOT NULL UNIQUE,
    String email NOT NULL UNIQUE,
    String password NOT NULL,
    boolean enabled DEFAULT true,
    String authorities
}

```

* accounts table
```
    {
        id int PRIMARY KEY AUTO_INCREMENT,
        user_id FOREING KEY(users.id),
        bank_id FOREIGN_KEY(banks.id),
        number int(10),
        type varchar,
        double balance DEFAULT 0,
        timestamp creation_date,
        timestamp last_update_date,
        boolean is_deleted DEFAULT false
    }
```


:checkered_flag: Firstall you have to start zookeeper server and apache kafka server from console then the topic will be created automatically when the application is started. (https://kafka.apache.org/quickstart)


#### CollectAPI is used in this project -> https://collectapi.com/api/economy/gold-currency-and-exchange-api/exchange

#

## Build with

<img src="https://raw.githubusercontent.com/github/explore/5b3600551e122a3277c2c5368af2ad5725ffa9a1/topics/java/java.png" align="left" height="50" width="50" />
<img src= "https://raw.githubusercontent.com/github/explore/80688e429a7d4ef2fca1e82350fe8e3517d3494d/topics/spring-boot/spring-boot.png" align="left" height="50" width="50">
<img src="https://raw.githubusercontent.com/github/explore/80688e429a7d4ef2fca1e82350fe8e3517d3494d/topics/mysql/mysql.png" align="left" height="50" width="50" />
<img src="https://cdn.jsdelivr.net/npm/simple-icons@v7/icons/apachekafka.svg" align="left" height="50" width="50" />
<img src= "https://camo.githubusercontent.com/cf460363010bff63a7ba0f3773819739c8daddd25284e90eaab6e947a35deabe/687474703a2f2f6d7962617469732e6769746875622e696f2f696d616765732f6d7962617469732d6c6f676f2e706e67" height="50">

