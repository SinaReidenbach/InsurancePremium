package com.sina_reidenbach.versicherungspraemienrechner.serviceTests;

import org.junit.jupiter.api.Test;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

public class DataBaseServiceTest {
    @Test
    void testInit() {
    }

    @Test
    void testReadCSV() {
    }

    @Test
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    void testCreateAnno_Kilometers() {
    }

    @Test
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    void testCreateVehicle() {
    }

    @Test
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    void testCreateRegion() {
    }

    @Test
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    void testCreateCity() {
    }

    @Test
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    void testCreatePostcode() {
    }

    @Test
    @Transactional(noRollbackFor = Exception.class)
    void testSaveDataTransactional() {
    }
}

