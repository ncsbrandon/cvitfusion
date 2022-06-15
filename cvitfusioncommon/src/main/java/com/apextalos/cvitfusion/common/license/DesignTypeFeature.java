package com.apextalos.cvitfusion.common.license;

import com.apextalos.cvitfusion.common.opflow.Type;

public class DesignTypeFeature extends Feature {

	public DesignTypeFeature(Type type) {
		super(String.format("DesignType[%d]", type.getTypeID()),
			  String.format("Design Type %s", type.getName()),
			  Boolean.class, "false", Feature.MODE_FEATURE);
	}

}
