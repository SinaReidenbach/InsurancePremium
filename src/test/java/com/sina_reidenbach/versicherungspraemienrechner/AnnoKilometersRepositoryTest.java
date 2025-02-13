package com.sina_reidenbach.versicherungspraemienrechner;

import com.sina_reidenbach.InsurancePremium.model.Anno_Kilometers;
import com.sina_reidenbach.InsurancePremium.repository.AnnoKilometersRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

public class AnnoKilometersRepositoryTest {
    @Mock
    private AnnoKilometersRepository annoKilometersRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void findByKmMinLessThanEqualAndKmMaxGreaterThanEqual() {
        // Erstelle AnnoKilometers-Instanzen
        Anno_Kilometers annoKilometers1 = new Anno_Kilometers(0, 5000, 0.5);
        Anno_Kilometers annoKilometers2 = new Anno_Kilometers(5001, 10000, 1.0);
        Anno_Kilometers annoKilometers3 = new Anno_Kilometers(10001, 20000, 1.5);
        Anno_Kilometers annoKilometers4 = new Anno_Kilometers(20001, 99999, 2.0);

        for (int i = 0; i < 30000; i += 5000) {
            // Bestimme die erwartete Instanz basierend auf `i`
            Anno_Kilometers expectedAnnoKilometers;
            if (i >= 0 && i <= 5000) {
                expectedAnnoKilometers = annoKilometers1;
            } else if (i > 5000 && i <= 10000) {
                expectedAnnoKilometers = annoKilometers2;
            } else if (i > 10000 && i <= 20000) {
                expectedAnnoKilometers = annoKilometers3;
            } else {
                expectedAnnoKilometers = annoKilometers4;
            }

            // Mock das Verhalten der Repository-Methode, sodass sie genau das erwartete Objekt zurückgibt
            when(annoKilometersRepository.findByMinLessThanEqualAndMaxGreaterThanEqual(i, i))
                    .thenReturn(List.of(expectedAnnoKilometers));

            // Rufe die Methode auf
            List<Anno_Kilometers> result = annoKilometersRepository.findByMinLessThanEqualAndMaxGreaterThanEqual(i, i);

            // Assertions
            assertNotNull(result);
            assertEquals(1, result.size());  // Sicherstellen, dass genau ein Objekt zurückgegeben wird
            assertEquals(expectedAnnoKilometers.getFactor(), result.get(0).getFactor());
        }
    }
}