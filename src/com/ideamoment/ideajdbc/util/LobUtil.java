/*
 * IdeaJdbc is a JDBC Data Access Framework. It depends on IdeaData Framework.
 *
 * copyright © www.ideamoment.com
 */
package com.ideamoment.ideajdbc.util;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.SQLException;

import com.ideamoment.ideajdbc.IdeaJdbc;


/**
 * @author Chinakite
 *
 */
public class LobUtil {
    /**
     * 创建一个Blob
     * 
     * @param bytes
     * @return
     * @throws SQLException
     * @throws IOException
     */
    public static Blob createBlob(byte[] bytes) throws SQLException, IOException {
        return createBlob(bytes, null);
    }
    
    /**
     * 创建一个Blob
     * 
     * @param bytes
     * @param dbName
     * @return
     * @throws SQLException
     * @throws IOException
     */
    public static Blob createBlob(byte[] bytes, String dbName) throws SQLException, IOException {
        Connection conn = null;
        if(dbName == null) {
            conn = IdeaJdbc.getCurrentTransaction().getConnection();
        }else{
            conn = IdeaJdbc.db(dbName).getCurrentTransaction().getConnection();
        }
        Blob blob = conn.createBlob();
        OutputStream out = blob.setBinaryStream(1);
        InputStream ins = new ByteArrayInputStream(bytes);
        byte[] temp = new byte[4096];  
        int length;  
        while ((length = ins.read(temp)) != -1) {  
            out.write(temp, 0, length);  
        }
        out.close();
        return blob;
    }
    
    public static void main(String[] args) throws SQLException, IOException {
        IdeaJdbc.beginTransaction();
        String str = "AAABBBCCCDDD";
        Blob b = createBlob(str.getBytes());
        System.out.println("aa");
        IdeaJdbc.endTransaction();
    }
}
