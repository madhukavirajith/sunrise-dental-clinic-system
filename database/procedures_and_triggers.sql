-- Advanced database features (Task B top-band requirement).
-- Run this in phpMyAdmin: sunrise_dental_clinic -> SQL tab -> paste -> Go

DELIMITER $$

-- TRIGGER: automatically marks an appointment COMPLETED the moment a
-- bill is generated for it - enforces a real business rule at the
-- database level rather than trusting application code to remember it.
CREATE TRIGGER after_bill_insert
    AFTER INSERT ON bill
    FOR EACH ROW
BEGIN
    UPDATE appointment
    SET status = 'COMPLETED'
    WHERE appointment_id = NEW.appointment_id;
    END$$

    -- STORED PROCEDURE: calculates total expected revenue for a given date.
-- Called from Java via CallableStatement in ReportServlet, rather than
-- summing in application code - moves the aggregation logic into the
-- database itself.
    CREATE PROCEDURE GetDailyRevenue(IN reportDate DATE, OUT totalRevenue DECIMAL(10,2))
    BEGIN
    SELECT COALESCE(SUM(t.base_fee), 0)
    INTO totalRevenue
    FROM appointment a
             JOIN treatment_type t ON a.treatment_type_id = t.treatment_type_id
    WHERE a.appointment_date = reportDate;
    END$$

    DELIMITER ;