package com.memariyan.components.search.service.export;

import com.memariyan.components.common.model.BaseEntity;
import org.springframework.data.domain.Page;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

public interface ExportService {

    <T extends BaseEntity> byte[] exportAsExcel(
            Page<T> items, Class<T> targetClass, Map<String, String> requestedFields,
            BiFunction<T, List<String>, List<String>> itemMapping) throws IOException;
}
