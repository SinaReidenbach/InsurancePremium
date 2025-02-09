package com.sina_reidenbach.versicherungspraemienrechner;

import com.sina_reidenbach.InsurancePremium.model.AnnoKilometers;
import com.sina_reidenbach.InsurancePremium.repository.AnnoKilometersRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
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
        AnnoKilometers annoKilometers1 = new AnnoKilometers(0, 5000, 0.5);
        AnnoKilometers annoKilometers2 = new AnnoKilometers(5001, 10000, 1.0);
        AnnoKilometers annoKilometers3 = new AnnoKilometers(10001, 20000, 1.5);
        AnnoKilometers annoKilometers4 = new AnnoKilometers(20001, 99999, 2.0);

        for (int i = 0; i < 30000; i += 5000) {
            // Bestimme die erwartete Instanz basierend auf `i`
            AnnoKilometers expectedAnnoKilometers;
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
            when(annoKilometersRepository.findByKmMinLessThanEqualAndKmMaxGreaterThanEqual(i, i))
                    .thenReturn(List.of(expectedAnnoKilometers));

            // Rufe die Methode auf
            List<AnnoKilometers> result = annoKilometersRepository.findByKmMinLessThanEqualAndKmMaxGreaterThanEqual(i, i);

            // Assertions
            assertNotNull(result);
            assertEquals(1, result.size());  // Sicherstellen, dass genau ein Objekt zurückgegeben wird
            assertEquals(expectedAnnoKilometers.getKmFactor(), result.get(0).getKmFactor());
        }
    }
}