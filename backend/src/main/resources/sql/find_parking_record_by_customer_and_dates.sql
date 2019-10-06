SELECT *
FROM parking_record
WHERE :customerId IS NULL OR customer_id = :customerId
  AND (:fromDate IS NULL OR
       :fromDate > begin_at)
  AND (:toDate IS NULL OR
       :toDate < end_by)