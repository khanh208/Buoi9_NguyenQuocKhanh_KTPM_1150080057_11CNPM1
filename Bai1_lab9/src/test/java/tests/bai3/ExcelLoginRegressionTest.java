package tests.bai3;

import org.testng.annotations.Test;

import framework.utils.ExcelDataProvider;
import framework.utils.ExcelRowData;

public class ExcelLoginRegressionTest extends AbstractExcelLoginTest {

    @Test(
        dataProvider = "regressionExcelData",
        dataProviderClass = ExcelDataProvider.class
    )
    public void testLoginFromRegressionExcel(ExcelRowData rowData) {
        executeLoginCase(rowData);
    }
}