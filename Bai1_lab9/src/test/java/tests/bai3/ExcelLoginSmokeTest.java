package tests.bai3;

import org.testng.annotations.Test;

import framework.utils.ExcelDataProvider;
import framework.utils.ExcelRowData;

public class ExcelLoginSmokeTest extends AbstractExcelLoginTest {

    @Test(
        dataProvider = "smokeExcelData",
        dataProviderClass = ExcelDataProvider.class
    )
    public void testLoginFromSmokeExcel(ExcelRowData rowData) {
        executeLoginCase(rowData);
    }
}