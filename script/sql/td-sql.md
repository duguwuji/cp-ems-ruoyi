TDengine 数据库创建脚本
```
CREATE DATABASE energy KEEP 365000;
USE energy;
CREATE STABLE energy (ts timestamp, client_id varchar(50), val float) TAGS (type varchar(50));
CREATE TABLE electricityi USING energy TAGS('electricityi');
CREATE TABLE electricityu USING energy TAGS('electricityu');
CREATE TABLE electricityw USING energy TAGS('electricityw');
CREATE TABLE electricityp USING energy TAGS('electricityp');
CREATE TABLE water USING energy TAGS('water');