package com.memariyan.components.search.presentation.mapper;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.function.BiFunction;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public final class ExportItemInfoContext<T> {

	private BiFunction<T, List<String>, List<String>> itemMapping;

}
