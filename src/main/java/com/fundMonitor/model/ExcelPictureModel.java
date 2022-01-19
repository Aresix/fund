package com.fundMonitor.model;

import lombok.Data;

import java.io.ByteArrayOutputStream;


/**
 * @author lli.chen
 */
@Data
public class ExcelPictureModel {
    private int column;
    private int row;
    private ByteArrayOutputStream photo;
}
