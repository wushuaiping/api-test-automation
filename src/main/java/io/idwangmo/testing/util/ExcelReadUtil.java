package io.idwangmo.testing.util;

import io.idwangmo.testing.model.jira.IssuesRequest;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Ts
 * 2019-03-15
 * 18:22
 */
public class ExcelReadUtil {

    public  static List<IssuesRequest> getIssues() throws IOException {
        XSSFRow row;

        // 1. 读取文件输入流
        String FILE_NAME = "/Users/Ts/Desktop/issues.xlsx";
        File file = new File(FILE_NAME);
        FileInputStream excelFile = new FileInputStream(file);
        XSSFWorkbook workbook = new XSSFWorkbook(excelFile);

        // 2. 获取 sheet
        XSSFSheet spreadsheet = workbook.getSheetAt(0);
        Iterator<Row> rowIterator = spreadsheet.iterator();

        List<IssuesRequest> requests = new ArrayList<>();

        while (rowIterator.hasNext()) {

            // 获得行 迭代

            row = (XSSFRow) rowIterator.next();

            // 获得单元格

            IssuesRequest request = new IssuesRequest();
            request.setProjectId((int) row.getCell(0).getNumericCellValue());
            request.setIssuesTypeId((int) row.getCell(1).getNumericCellValue());
            request.setSummary(row.getCell(2).getStringCellValue());
            request.setDescription(row.getCell(3).getStringCellValue());
            request.setComponents((int) row.getCell(4).getNumericCellValue());
            request.setScenarioType(row.getCell(5).getStringCellValue());
            request.setScenario(row.getCell(6).getStringCellValue());

            requests.add(request);
        }

        excelFile.close();

        return  requests;
    }
}
