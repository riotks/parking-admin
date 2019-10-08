SELECT *
FROM invoice
WHERE :customerId IS NULL OR customer_id = :customerId
  AND (:fromDate IS NULL OR
       :fromDate > created_at)
  AND (:toDate IS NULL OR
       :toDate < created_at)