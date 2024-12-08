package com.memariyan.components.search.service.export.impl;

import com.memariyan.components.common.model.BaseEntity;
import com.memariyan.components.search.service.export.ExportService;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.data.domain.Page;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.stream.IntStream;

public class ExportServiceImpl implements ExportService {

	private static final int HEADER_ROW = 0;
	private static final int MAX_WINDOW_SIZE = 1000;

	public <T extends BaseEntity> byte[] exportAsExcel(
			Page<T> items, Class<T> targetClass, Map<String, String> requestedFields,
			BiFunction<T, List<String>, List<String>> itemMapping) throws IOException {
		SXSSFWorkbook workbook = new SXSSFWorkbook(MAX_WINDOW_SIZE);
		Sheet sheet = workbook.createSheet();

		List<String> requestedFieldNames = new ArrayList<>(requestedFields.keySet());
		List<String> requestedFieldValues = new ArrayList<>(requestedFields.values());

		setHeaderRow(sheet.createRow(HEADER_ROW), requestedFieldValues);
		setItemsRow(sheet, items.getContent(), itemMapping, requestedFieldNames);
		return toByteArray(workbook);
	}

	private void setHeaderRow(Row row, List<String> fields) {
		IntStream.range(0, fields.size()).forEach(index -> {
			Cell cell = row.createCell(index);
			cell.setCellValue(fields.get(index));
		});
	}

	private <T extends BaseEntity> void setItemsRow(
			Sheet sheet, List<T> items, BiFunction<T, List<String>, List<String>> itemMapping, List<String> requestedFields) {
		for (int cellIndex = 0; cellIndex < items.size(); cellIndex++) {
			Row row = sheet.createRow(cellIndex + 1);
			T item = items.get(cellIndex);
			List<String> fieldValues = itemMapping.apply(item, requestedFields);

			IntStream.range(0, fieldValues.size()).forEach(index -> {
				Cell cell = row.createCell(index);
				cell.setCellValue(fieldValues.get(index));
			});
		}
	}

	private byte[] toByteArray(SXSSFWorkbook workbook) throws IOException {
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		workbook.write(os);
		os.close();
		workbook.dispose();
		workbook.close();
		return os.toByteArray();
	}
}
