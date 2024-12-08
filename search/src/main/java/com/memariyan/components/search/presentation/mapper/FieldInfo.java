package com.memariyan.components.search.presentation.mapper;

import lombok.*;

import java.util.function.Function;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class FieldInfo<T, R> {

	private String propertyName;

	private String mappedPropertyName;

	private Function<T, R> mapper;

	public FieldInfo(String propertyName, String mappedPropertyName) {
		this.propertyName = propertyName;
		this.mappedPropertyName = mappedPropertyName;
	}

}
