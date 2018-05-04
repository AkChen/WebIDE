package com.akchen.WebIDE.Interface;

import java.io.IOException;
import java.io.OutputStream;


public class PTLibOutputStream extends OutputStream {
    private Long session;
    private PTLib ptLib;
    public PTLibOutputStream(Long session,PTLib ptLib){
        this.session = session;
        this.ptLib = ptLib;
    }
    @Override
    public void write(int b) throws IOException {
        byte[] bytes = new byte[1];
        while(ptLib.Write(session,bytes,1)!=1){
        }
    }
    @Override
    public void write(byte b[]) throws IOException {
        ptLib.Write(session,b, b.length);
    }

    @Override
    public void close() throws IOException {
        ptLib.Close(session);
    }
}
