set hive.variable.substitute=true;

USE ${hiveconf:dbname};

CREATE TABLE IF NOT EXISTS product (product_id int, product_code string, add_dt timestamp, remove_dt timestamp) row format delimited fields terminated by ',';

CREATE TABLE IF NOT EXISTS store (store_id int, store_num string, city string, address string, open_date timestamp, close_date timestamp) row format delimited fields terminated by ',';

CREATE TABLE IF NOT EXISTS employee (employee_id int, employee_num string, store_num string, employee_name string, joining_date timestamp,      designation string) row format delimited fields terminated by ',';

CREATE TABLE IF NOT EXISTS loyalty (loyalty_id int, cust_num int, card_no string, joining_date timestamp, points decimal(10,2)) row format delimited fields terminated by ',';

CREATE TABLE IF NOT EXISTS promotion (promotion_id int, promo_code string, description string, promo_start_date timestamp, promo_end_date timestamp) row format delimited fields terminated by ',';

CREATE TABLE IF NOT EXISTS transaction(transactionID char(32),product_id int, store_id int, loyalty_id int, amount double, discount double) PARTITIONED BY (transaction_date string) ROW FORMAT DELIMITED FIELDS TERMINATED BY ',';
