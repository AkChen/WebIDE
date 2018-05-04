package com.akchen.WebIDE.Interface;

import com.sun.jna.Pointer;

import java.io.IOException;
import java.io.InputStream;

public class PTLibInputStream extends InputStream {
    private  Long session;
    private  PTLib ptLib;
    public PTLibInputStream(Long session,PTLib ptLib){
        this.session = session;
        this.ptLib = ptLib;
    }
    /***
     *
     * @return the number of bytes have read
     * @throws IOException
     */
    @Override
    public int read(byte b[]) throws IOException {
        return ptLib.Read(session,b,b.length);
    }

    /***
     *
     * @return the number of bytes have read
     * @throws IOException
     */
    @Override
    public int read() throws IOException {
        byte[] bytes = new byte[1];
        int nread = ptLib.Read(session,bytes,1);
        if(nread==1){
            return bytes[1];
        }else{
            return -1;
        }
    }
    @Override
    public void close() throws IOException {
        ptLib.Close(session);
    }
}
