INSERT INTO invoice (id, amount, currency)
VALUES (invoice_seq.nextval, :amount, :currency)
RETURNING id;