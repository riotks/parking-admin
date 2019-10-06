UPDATE parking_record
SET status = :status,
    end_by = :end_by
WHERE id = :id
  AND status not in ('COMPLETED', 'ERROR', 'DUPLICATE');