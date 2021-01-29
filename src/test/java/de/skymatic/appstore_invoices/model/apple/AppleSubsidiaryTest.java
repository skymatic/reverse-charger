package de.skymatic.appstore_invoices.model.apple;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class AppleSubsidiaryTest {

	@Test
	public void testEachExistingRPCIsMappedToASubsidiary() {
		for (var rpc : RegionNCurrency.values()) {
			Assertions.assertDoesNotThrow(() -> AppleSubsidiary.mapFromRegionNCurrency(rpc));
		}
	}

}
