-- Create the basic data for the demo. This gets loaded automatically because of the file's name and location.

INSERT INTO customer (name, balance, creditLimit, preferred) VALUES ('Alpha and Sons', 85, 900, true);
INSERT INTO customer (name, balance, creditLimit, preferred) VALUES ('Bravo Hardware', 60, 5000, false);
INSERT INTO customer (name, balance, creditLimit, preferred) VALUES ('Charlie''s Construction', 220, 1500, true);
INSERT INTO customer (name, balance, creditLimit, preferred) VALUES ('Delta Engineering', 0, 0, false);
INSERT INTO customer (name, balance, creditLimit, preferred) VALUES ('Delflogflorengineering', 0, 0, false);

INSERT INTO purchaseorder (order_number, amount_total, paid, notes, customer_name) VALUES (1, 35, 0, 'This is a small order', 'Alpha and Sons');
INSERT INTO purchaseorder (order_number, amount_total, paid, notes, customer_name) VALUES (2, 675, 1, '', 'Bravo Hardware');
INSERT INTO purchaseorder (order_number, amount_total, paid, notes, customer_name) VALUES (3, 60, 0, 'Please rush this order', 'Bravo Hardware');
INSERT INTO purchaseorder (order_number, amount_total, paid, notes, customer_name) VALUES (4, 85, 0, 'Deliver by overnight with signature required', 'Charlie''s Construction');
INSERT INTO purchaseorder (order_number, amount_total, paid, notes, customer_name) VALUES (5, 135, 0, '', 'Charlie''s Construction');
INSERT INTO purchaseorder (order_number, amount_total, paid, notes, customer_name) VALUES (6, 50, 0, 'Pack with care - fragile merchandise', 'Alpha and Sons');

INSERT INTO product (product_number, name, price) VALUES (1, 'Hammer', 10);
INSERT INTO product (product_number, name, price) VALUES (2, 'Shovel', 25);
INSERT INTO product (product_number, name, price) VALUES (3, 'Drill', 315);

INSERT INTO lineitem (lineitem_id, product_number, order_number, qty_ordered, product_price, amount) VALUES (1, 1, 1, 1, 10, 10);
INSERT INTO lineitem (lineitem_id, product_number, order_number, qty_ordered, product_price, amount) VALUES (2, 1, 2, 2, 10, 20);
INSERT INTO lineitem (lineitem_id, product_number, order_number, qty_ordered, product_price, amount) VALUES (3, 2, 2, 1, 25, 25);
INSERT INTO lineitem (lineitem_id, product_number, order_number, qty_ordered, product_price, amount) VALUES (4, 3, 2, 2, 315, 630);
INSERT INTO lineitem (lineitem_id, product_number, order_number, qty_ordered, product_price, amount) VALUES (5, 1, 3, 1, 10, 10);
INSERT INTO lineitem (lineitem_id, product_number, order_number, qty_ordered, product_price, amount) VALUES (6, 2, 3, 2, 25, 50);
INSERT INTO lineitem (lineitem_id, product_number, order_number, qty_ordered, product_price, amount) VALUES (7, 1, 4, 1, 10, 10);
INSERT INTO lineitem (lineitem_id, product_number, order_number, qty_ordered, product_price, amount) VALUES (8, 2, 4, 3, 25, 75);
INSERT INTO lineitem (lineitem_id, product_number, order_number, qty_ordered, product_price, amount) VALUES (9, 1, 5, 1, 10, 10);
INSERT INTO lineitem (lineitem_id, product_number, order_number, qty_ordered, product_price, amount) VALUES (10, 2, 5, 5, 25, 125);
INSERT INTO lineitem (lineitem_id, product_number, order_number, qty_ordered, product_price, amount) VALUES (11, 2, 6, 2, 25, 50);
INSERT INTO lineitem (lineitem_id, product_number, order_number, qty_ordered, product_price, amount) VALUES (12, 2, 1, 1, 25, 25);
