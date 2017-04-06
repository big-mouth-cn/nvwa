/*
 * Copyright 2017 big-mouth.cn
 *
 * The Project licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package org.bigmouth.nvwa.office.excel;

import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Comment;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.bigmouth.nvwa.utils.DateUtils;

public class ExcelHelper {
    
    public static final int V_2003 = 2003;
    public static final int V_2007 = 2007;
    
    public static void write(int version, Excel excel, FileOutputStream fos) throws IOException {
        if (null == excel)
            throw new NullPointerException();
        Workbook workbook = version == V_2007 ? new XSSFWorkbook() : new HSSFWorkbook();
        
        List<Sheet> sheets = excel.getSheets();
        for (Sheet sheet : sheets) {
            org.apache.poi.ss.usermodel.Sheet s = null;
            String name = sheet.getName();
            if (StringUtils.isBlank(name)) {
                s = workbook.createSheet();
            }
            else {
                s = workbook.createSheet(name);
            }
            
            List<ColumnWidth> widths = sheet.getWidths();
            for (ColumnWidth columnWidth : widths) {
                s.setColumnWidth(columnWidth.getIndex(), columnWidth.getWidth());
            }
            
            List<Row> rows = sheet.getRows();
            for (Row row : rows) {
                org.apache.poi.ss.usermodel.Row r = s.createRow(row.getIndex());
                
                List<Cell> cells = row.getCells();
                for (Cell cell : cells) {
                    
                    org.apache.poi.ss.usermodel.Cell c = r.createCell(cell.getIndex());
                    
                    if (null == cell.getValue()) {
                        continue;
                    }
                    
                    if (cell.getCls().isAssignableFrom(Number.class)) {
                        c.setCellType(org.apache.poi.ss.usermodel.Cell.CELL_TYPE_NUMERIC);
                        c.setCellValue(new BigDecimal(cell.getValue().toString()).doubleValue());
                    }
                    else if (cell.getCls().isAssignableFrom(Date.class)) {
                        String format = cell.getFormat();
                        if (StringUtils.isBlank(format)) {
                            format = DateUtils.DEFAULT_DATE_FORMAT;
                        }
                        c.setCellValue((Date) cell.getValue());
                        
                        CellStyle style = workbook.createCellStyle();
                        DataFormat dataFormat = workbook.createDataFormat();
                        style.setDataFormat(dataFormat.getFormat(format));
                        c.setCellStyle(style);
                    }
                    else {
                        c.setCellType(org.apache.poi.ss.usermodel.Cell.CELL_TYPE_STRING); 
                        c.setCellValue(cell.getValue().toString());
                    }
                }
            }
        }
        workbook.write(fos);
    }
    
    public static Excel parse(int version, byte[] content, Map<Integer, Class<?>> cellValueTranslateDict, 
            Set<Integer> excludeRowIndex,
            Set<Integer> includeCellIndex) {
        try {
            if (null == content || content.length == 0)
                return null;
            
            Excel excel = new Excel();
            ByteArrayInputStream inputstream = new ByteArrayInputStream(content);
            org.apache.poi.ss.usermodel.Workbook workbook = 
                    version == V_2007 ? new XSSFWorkbook(inputstream) : new HSSFWorkbook(inputstream);
            
            boolean translate = null != cellValueTranslateDict && cellValueTranslateDict.size() > 0;
            boolean excludeRow = null != excludeRowIndex && excludeRowIndex.size() > 0;
            boolean includeCell = null != includeCellIndex && includeCellIndex.size() > 0;
            
            int numberOfSheets = workbook.getNumberOfSheets();

            for (int sheetIndex = 0; sheetIndex < numberOfSheets; sheetIndex++) {
                Sheet s = new Sheet(sheetIndex);
                excel.addSheet(s);
                
                org.apache.poi.ss.usermodel.Sheet sheet = workbook.getSheetAt(sheetIndex);
                int numberOfRows = sheet.getPhysicalNumberOfRows();
                String sheetName = sheet.getSheetName();
                s.setName(sheetName);
                
                for (int rowIndex = 0; rowIndex < numberOfRows; rowIndex++) {
                    
                    if (excludeRow && excludeRowIndex.contains(rowIndex)) {
                        continue;
                    }
                    
                    Row r = new Row(rowIndex);
                    s.addRow(r);
                    
                    org.apache.poi.ss.usermodel.Row row = sheet.getRow(rowIndex);
                    if (null == row)
                        continue;
                    
                    int numberOfCell = row.getLastCellNum();
                    
                    for (int cellIndex = 0; cellIndex < numberOfCell; cellIndex++) {
                        
                        if (includeCell && !includeCellIndex.contains(cellIndex)) {
                            // Added null cell
                            r.addCell(null);
                            
                            continue;
                        }
                        
                        org.apache.poi.ss.usermodel.Cell cell = row.getCell(cellIndex);
                        
                        if (null == cell)
                            continue;
                        
                        Class<?> cls = String.class;
                        Object value = null;
                        
                        if (cell.getCellType() == org.apache.poi.ss.usermodel.Cell.CELL_TYPE_BOOLEAN) {
                            cls = Boolean.class;
                            value = String.valueOf(cell.getBooleanCellValue());
                        }
                        else if (cell.getCellType() == org.apache.poi.ss.usermodel.Cell.CELL_TYPE_NUMERIC) {
                            if (DateUtil.isCellDateFormatted(cell)) {
                                cls = Date.class;
                                value = cell.getDateCellValue();
                            }
                            else {
                                cls = Number.class;
                                value = String.valueOf(cell.getNumericCellValue());
                            }
                        }
                        else {
                            cls = String.class;
                            value = String.valueOf(cell.getStringCellValue());
                        }
                        
                        if (translate) {
                            Class<?> translateCls = cellValueTranslateDict.get(cellIndex);
                            if (null != translateCls) {
                                if (translateCls.isAssignableFrom(Integer.class) || translateCls.isAssignableFrom(Float.class) 
                                        || translateCls.isAssignableFrom(Double.class)) {
                                    cls = Number.class;
                                    value = cell.getNumericCellValue();
                                }
                                else if (translateCls.isAssignableFrom(Date.class)) {
                                    cls = Date.class;
                                    value = cell.getDateCellValue();
                                }
                                else if (translateCls.isAssignableFrom(Boolean.class)) {
                                    cls = Boolean.class;
                                    value = cell.getBooleanCellValue();
                                }
                                else {
                                    cls = String.class;
                                    value = cell.getStringCellValue();
                                }
                            }
                        }
                        
                        Cell c = new Cell(cellIndex, cls, value);
                        Comment cellComment = cell.getCellComment();
                        if (null != cellComment) {
                            c.setComment(cellComment.getString().getString());
                        }
                        r.addCell(c);
                        
                    }
                }
            }
            
            return excel;
        }
        catch (IOException e) {
            return null;
        }
    }
}
