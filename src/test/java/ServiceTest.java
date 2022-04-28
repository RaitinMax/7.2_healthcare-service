import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import ru.netology.patient.entity.BloodPressure;
import ru.netology.patient.entity.HealthInfo;
import ru.netology.patient.entity.PatientInfo;
import ru.netology.patient.repository.PatientInfoRepository;
import ru.netology.patient.service.alert.SendAlertService;
import ru.netology.patient.service.alert.SendAlertServiceImpl;
import ru.netology.patient.service.medical.MedicalService;
import ru.netology.patient.service.medical.MedicalServiceImpl;

import java.math.BigDecimal;

public class ServiceTest {

    PatientInfo patientInfo = Mockito.mock(PatientInfo.class);;
    HealthInfo healthInfo = Mockito.mock(HealthInfo.class);;
    PatientInfoRepository patientInfoRepository = Mockito.mock(PatientInfoRepository.class);;
    SendAlertService sendAlertService = Mockito.spy(SendAlertServiceImpl.class);;



    @Test
    public void testCheckBloodPressure() {
        BloodPressure bloodPressure1 = new BloodPressure(120, 120);
        BloodPressure bloodPressure2 = new BloodPressure(160, 120);
        Mockito.when(healthInfo.getBloodPressure()).thenReturn(bloodPressure2);
        Mockito.when(patientInfo.getHealthInfo()).thenReturn(healthInfo);
        Mockito.when(patientInfoRepository.getById(Mockito.any())).thenReturn(patientInfo);
        MedicalService medicalService = new MedicalServiceImpl(patientInfoRepository, sendAlertService);
        medicalService.checkBloodPressure("patient1", bloodPressure1);
        Mockito.verify(sendAlertService, Mockito.only()).send(Mockito.any());
    }

    @Test
    public void testCheckNoBloodPressure() {
        BloodPressure bloodPressure1 = new BloodPressure(160, 120);
        BloodPressure bloodPressure2 = new BloodPressure(160, 120);
        Mockito.when(healthInfo.getBloodPressure()).thenReturn(bloodPressure2);
        Mockito.when(patientInfo.getHealthInfo()).thenReturn(healthInfo);
        Mockito.when(patientInfoRepository.getById(Mockito.any())).thenReturn(patientInfo);
        MedicalService medicalService = new MedicalServiceImpl(patientInfoRepository, sendAlertService);
        medicalService.checkBloodPressure("patient2", bloodPressure1);
        Mockito.verify(sendAlertService, Mockito.never()).send(Mockito.any());
    }

    @Test
    public void testCheckTemperature() {
        Mockito.when(healthInfo.getNormalTemperature()).thenReturn(new BigDecimal(36.6));
        Mockito.when(patientInfo.getHealthInfo()).thenReturn(healthInfo);
        Mockito.when(patientInfoRepository.getById(Mockito.any())).thenReturn(patientInfo);
        MedicalService medicalService = new MedicalServiceImpl(patientInfoRepository, sendAlertService);
        medicalService.checkTemperature(null, new BigDecimal(33.3));
        Mockito.verify(sendAlertService, Mockito.only()).send(Mockito.any());
    }

    @Test
    public void testCheckNoMessage() {
        BloodPressure bloodPressure = new BloodPressure(120, 100);
        Mockito.when(healthInfo.getBloodPressure()).thenReturn(bloodPressure);
        Mockito.when(healthInfo.getNormalTemperature()).thenReturn(new BigDecimal(36.6));
        Mockito.when(patientInfo.getHealthInfo()).thenReturn(healthInfo);
        Mockito.when(patientInfoRepository.getById(Mockito.any())).thenReturn(patientInfo);
        MedicalService medicalService = new MedicalServiceImpl(patientInfoRepository, sendAlertService);
        medicalService.checkBloodPressure(null, bloodPressure);
        medicalService.checkTemperature(null, new BigDecimal(36.6));
        Mockito.verify(sendAlertService, Mockito.never()).send(Mockito.any());
    }
}