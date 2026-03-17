package framework.utils;

import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.FormulaError;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

public final class ExcelReader {

    private ExcelReader() {
    }

    public static List<ExcelRowData> readRows(String resourcePath, String... sheetNames) {
        List<ExcelRowData> rows = new ArrayList<>();

        try (InputStream is = ExcelReader.class.getClassLoader().getResourceAsStream(resourcePath)) {
            if (is == null) {
                throw new RuntimeException("Cannot find Excel file: " + resourcePath);
            }

            try (Workbook workbook = WorkbookFactory.create(is)) {
                for (String sheetName : sheetNames) {
                    Sheet sheet = workbook.getSheet(sheetName);
                    if (sheet == null) {
                        throw new RuntimeException("Cannot find sheet: " + sheetName);
                    }

                    Row headerRow = sheet.getRow(0);
                    if (headerRow == null) {
                        continue;
                    }

                    int lastRowNum = sheet.getLastRowNum();

                    for (int r = 1; r <= lastRowNum; r++) {
                        Row row = sheet.getRow(r);
                        if (row == null || isEntireRowEmpty(row, headerRow.getLastCellNum())) {
                            continue;
                        }

                        String username = getCellValue(row.getCell(0));
                        String password = getCellValue(row.getCell(1));
                        String col3 = getCellValue(row.getCell(2));
                        String description = getCellValue(row.getCell(3));

                        ExcelRowData rowData;
                        if ("SmokeCases".equalsIgnoreCase(sheetName)) {
                            rowData = new ExcelRowData(sheetName, username, password, col3, "", description);
                        } else {
                            rowData = new ExcelRowData(sheetName, username, password, "", col3, description);
                        }

                        rows.add(rowData);
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Error reading Excel file: " + resourcePath, e);
        }

        return rows;
    }

    public static Object[][] to2DArray(List<ExcelRowData> rows) {
        Object[][] data = new Object[rows.size()][1];
        for (int i = 0; i < rows.size(); i++) {
            data[i][0] = rows.get(i);
        }
        return data;
    }

    public static String getCellValue(Cell cell) {
        if (cell == null) {
            return "";
        }

        return switch (cell.getCellType()) {
            case STRING -> cell.getStringCellValue().trim();

            case NUMERIC -> {
                DecimalFormat decimalFormat = new DecimalFormat("0");
                yield decimalFormat.format(cell.getNumericCellValue());
            }

            case BOOLEAN -> String.valueOf(cell.getBooleanCellValue());

            case FORMULA -> {
                CellType cachedType = cell.getCachedFormulaResultType();
                yield switch (cachedType) {
                    case STRING -> cell.getStringCellValue().trim();
                    case NUMERIC -> {
                        DecimalFormat decimalFormat = new DecimalFormat("0");
                        yield decimalFormat.format(cell.getNumericCellValue());
                    }
                    case BOOLEAN -> String.valueOf(cell.getBooleanCellValue());
                    case ERROR -> FormulaError.forInt(cell.getErrorCellValue()).getString();
                    default -> "";
                };
            }

            case BLANK -> "";
            default -> "";
        };
    }

    private static boolean isEntireRowEmpty(Row row, short totalCells) {
        for (int c = 0; c < totalCells; c++) {
            if (!getCellValue(row.getCell(c)).isBlank()) {
                return false;
            }
        }
        return true;
    }
}