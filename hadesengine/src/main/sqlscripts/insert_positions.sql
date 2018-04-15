DROP TABLE IF EXISTS position;
CREATE TABLE position (trader_id integer, asset_id varchar(40), position_size numeric);

INSERT INTO position (trader_id, asset_id, position_size) VALUES(1, 'BTC', 2);
INSERT INTO position (trader_id, asset_id, position_size) VALUES(2, 'BTC', 2);
INSERT INTO position (trader_id, asset_id, position_size) VALUES(3, 'BTC', 3);
INSERT INTO position (trader_id, asset_id, position_size) VALUES(2, 'LTC', 5.9);
INSERT INTO position (trader_id, asset_id, position_size) VALUES(1, 'XRP', 300.8);