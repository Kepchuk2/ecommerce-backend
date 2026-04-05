ALTER TABLE carts
ALTER COLUMN session_id TYPE varchar(255)
      USING session_id::varchar;