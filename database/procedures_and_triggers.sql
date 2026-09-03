
DELIMITER $$

CREATE TRIGGER after_bill_insert
    AFTER INSERT ON bill
    FOR EACH ROW
BEGIN
    UPDATE appointment
    SET status = 'COMPLETED'
    WHERE appointment_id = NEW.appointment_id;
    END$$

    CREATE PROCEDURE GetDailyRevenue(IN reportDate DATE, OUT totalRevenue DECIMAL(10,2))
    BEGIN
    SELECT COALESCE(SUM(t.base_fee), 0)
    INTO totalRevenue
    FROM appointment a
             JOIN treatment_type t ON a.treatment_type_id = t.treatment_type_id
    WHERE a.appointment_date = reportDate;
    END$$

    DELIMITER ;