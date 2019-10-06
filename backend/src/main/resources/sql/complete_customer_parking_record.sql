UPDATE parking_record
SET status = :status,
    end_by = :end_by
WHERE customer_id = :customer_id
  AND status not in ('COMPLETED', 'ERROR', 'DUPLICATE');