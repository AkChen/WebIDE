package com.akchen.WebIDE.Util;

import java.io.*;

public class IOHelper {
    public static  String  SaveCode(String fileName,String code){

        try {

            FileOutputStream writerStream = new FileOutputStream(fileName);
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(writerStream, "UTF-8"));
            writer.write(code);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fileName;
    }
    public static void DeleteCode(String fileName){
      File f = new File(fileName);
      if(f.exists()){
         f.delete();
      }
    }
}
