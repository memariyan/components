package com.memariyan.components.search.presentation.mapper;

import java.util.HashMap;
import java.util.Map;

public abstract class SearchFieldInfoContext {

	private Map<String, FieldInfo<?, ?>> fieldsInfoMap;

	public void addToFieldsInfo(FieldInfo<?, ?> fieldInfo) {
		if (this.fieldsInfoMap == null)
			fieldsInfoMap = new HashMap<>();
		this.fieldsInfoMap.put(fieldInfo.getPropertyName(), fieldInfo);
	}

	public FieldInfo<?, ?> findFieldInfo(String fieldName) {
		return this.fieldsInfoMap.get(fieldName);
	}
}
