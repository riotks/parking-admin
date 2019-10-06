INSERT INTO CUSTOMER (id, customer_type, first_name, last_name)
VALUES (1, 'REGULAR', 'Reggie', 'One');
INSERT INTO CUSTOMER (id, customer_type, first_name, last_name)
VALUES (2, 'PREMIUM', 'Premmie', 'Two');

INSERT INTO parking_record (id, customer_id, customer_type, status, begin_at, end_by)
VALUES (1001, 1, 'REGULAR', 'COMPLETED', '2019-09-06T08:12:00', '2019-09-06T10:45:00');
INSERT INTO parking_record (id, customer_id, customer_type, status, begin_at, end_by)
VALUES (1002, 1, 'REGULAR', 'COMPLETED', '2019-09-06T19:40:00', '2019-09-06T20:35:00');

INSERT INTO parking_record (id, customer_id, customer_type, status, begin_at, end_by)
VALUES (2001, 2, 'PREMIUM', 'COMPLETED', '2019-09-06T08:12:00', '2019-09-06T10:45:00');
INSERT INTO parking_record (id, customer_id, customer_type, status, begin_at, end_by)
VALUES (2002, 2, 'PREMIUM', 'COMPLETED', '2019-09-06T19:40:00', '2019-09-06T20:35:00');
INSERT INTO parking_record (id, customer_id, customer_type, status, begin_at, end_by)
VALUES (2003, 2, 'PREMIUM', 'COMPLETED', '2019-09-06T08:12:00', '2019-09-06T10:45:00');
INSERT INTO parking_record (id, customer_id, customer_type, status, begin_at, end_by)
VALUES (2004, 2, 'PREMIUM', 'COMPLETED', '2019-09-07T19:40:00', '2019-09-06T20:35:00');


