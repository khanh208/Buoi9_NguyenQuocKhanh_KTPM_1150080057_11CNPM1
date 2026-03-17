package framework.utils;

import java.util.List;
import org.testng.annotations.DataProvider;

public class ExcelDataProvider {

    private static final String EXCEL_PATH = "testdata/login_data.xlsx";

    @DataProvider(name = "smokeExcelData", parallel = true)
    public static Object[][] smokeData() {
        List<ExcelRowData> rows = ExcelReader.readRows(EXCEL_PATH, "SmokeCases");
        return ExcelReader.to2DArray(rows);
    }

    @DataProvider(name = "regressionExcelData", parallel = true)
    public static Object[][] regressionData() {
        List<ExcelRowData> rows = ExcelReader.readRows(
                EXCEL_PATH,
                "SmokeCases",
                "NegativeCases",
                "BoundaryCases"
        );
        return ExcelReader.to2DArray(rows);
    }
}