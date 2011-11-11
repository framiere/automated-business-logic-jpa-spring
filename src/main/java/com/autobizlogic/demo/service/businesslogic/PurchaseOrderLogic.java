package com.autobizlogic.demo.service.businesslogic;

import com.autobizlogic.abl.businesslogic.annotations.*;

public class PurchaseOrderLogic {

	@Sum("lineitems.amount")
	public void deriveAmountTotal() { }
}
