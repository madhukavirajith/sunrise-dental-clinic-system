package dentalclinic.dao.mapper;

import dentalclinic.model.Appointment;
import dentalclinic.util.JsonUtil;

public class AppointmentJsonMapper {

    public static String toJson(Appointment a) {
        return "{"
                + "\"appointmentNumber\":" + JsonUtil.jsonString(a.getAppointmentNumber()) + ","
                + "\"status\":" + JsonUtil.jsonString(a.getStatus()) + ","
                + "\"appointmentDate\":" + JsonUtil.jsonString(a.getAppointmentDate().toString()) + ","
                + "\"appointmentTime\":" + JsonUtil.jsonString(a.getAppointmentTime().toString()) + ","
                + "\"patient\":{"
                +     "\"name\":" + JsonUtil.jsonString(a.getPatient().getName()) + ","
                +     "\"contactNumber\":" + JsonUtil.jsonString(a.getPatient().getContactNumber())
                + "},"
                + "\"dentist\":{"
                +     "\"name\":" + JsonUtil.jsonString(a.getDentist().getName()) + ","
                +     "\"specialization\":" + JsonUtil.jsonString(a.getDentist().getSpecialization())
                + "},"
                + "\"treatmentType\":{"
                +     "\"name\":" + JsonUtil.jsonString(a.getTreatmentType().getName()) + ","
                +     "\"baseFee\":" + a.getTreatmentType().getBaseFee()
                + "}"
                + "}";
    }

    public static String toJsonError(String message) {
        return "{\"error\":" + JsonUtil.jsonString(message) + "}";
    }
}