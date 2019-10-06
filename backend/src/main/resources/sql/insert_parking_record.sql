INSERT INTO parking_record (id, customer_id, customer_type, status, begin_at)
VALUES (parking_record_seq.nextval,:customerId, :customerType, :status, :beginAt);