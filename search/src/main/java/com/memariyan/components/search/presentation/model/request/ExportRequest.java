package com.memariyan.components.search.presentation.model.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.lang.Nullable;

import java.util.Map;

@Setter
@Getter
@ToString(callSuper = true)
public class ExportRequest extends SearchRequest {
	private static final long serialVersionUID = -6463751252523280090L;

	@Nullable
	private String fileName;

	private Map<String, String> fields;
}
