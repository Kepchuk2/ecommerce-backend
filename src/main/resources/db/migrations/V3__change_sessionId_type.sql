ALTER TABLE carts
ALTER COLUMN session_id TYPE bigint
      USING session_id::bigint;