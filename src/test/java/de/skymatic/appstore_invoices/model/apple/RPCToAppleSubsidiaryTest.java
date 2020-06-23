package de.skymatic.appstore_invoices.model.apple;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class RPCToAppleSubsidiaryTest {

	@Test
	public void testEachExistingRPCIsMappedToASubsidiary() {
		for (var rpc : RegionPlusCurrency.values()) {
			Assertions.assertDoesNotThrow(() -> AppleUtility.mapRegionPlusCurrencyToSubsidiary(rpc));
		}
	}

}
