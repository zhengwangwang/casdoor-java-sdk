// Copyright 2023 The Casdoor Authors. All Rights Reserved.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//      http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing CasdoorPermissions and
// limitations under the License.

package org.casbin.casdoor;

import org.casbin.casdoor.entity.Payment;
import org.casbin.casdoor.service.PaymentService;
import org.casbin.casdoor.support.TestDefaultConfig;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class PaymentTest {
    private final PaymentService paymentService = new PaymentService(TestDefaultConfig.InitConfig());

    @Test
    public void testPayment() {
        String name = TestDefaultConfig.getRandomName("Payment");

        // Add a new object
        Payment payment = new Payment(
                "casbin",
                name,
                LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME),
                name,
                "casbin"
        );
        assertDoesNotThrow(() -> paymentService.addPayment(payment));

        // Get all objects, check if our added object is inside the list
        List<Payment> payments;
        try {
            payments = paymentService.getPayments();
        } catch (Exception e) {
            fail("Failed to get objects: " + e.getMessage());
            return;
        }

        boolean found = payments.stream().anyMatch(item -> item.name.equals(name));
        assertTrue(found, "Added object not found in list");

        // Get the object
        Payment retrievedPayment;
        try {
            retrievedPayment = paymentService.getPayment(name);
        } catch (Exception e) {
            fail("Failed to get object: " + e.getMessage());
            return;
        }
        assertEquals(name, retrievedPayment.name, "Retrieved object does not match added object");

        // Update the object
        String updatedProductName = "Updated Casdoor Website";
        retrievedPayment.productName = updatedProductName;
        assertDoesNotThrow(() -> paymentService.updatePayment(retrievedPayment));

        // Validate the update
        Payment updatedPayment;
        try {
            updatedPayment = paymentService.getPayment(name);
        } catch (Exception e) {
            fail("Failed to get updated object: " + e.getMessage());
            return;
        }
        assertEquals(updatedProductName, updatedPayment.productName, "Failed to update object, productName mismatch");

        // Delete the object
        assertDoesNotThrow(() -> paymentService.deletePayment(name));

        // Validate the deletion
        Payment deletedPayment;
        try {
            deletedPayment = paymentService.getPayment(name);
        } catch (Exception e) {
            fail("Failed to delete object: " + e.getMessage());
            return;
        }
        assertNull(deletedPayment, "Failed to delete object, it's still retrievable");
    }
}

