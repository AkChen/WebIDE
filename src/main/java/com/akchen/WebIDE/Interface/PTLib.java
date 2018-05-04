package com.akchen.WebIDE.Interface;

import com.sun.jna.Library;

public interface PTLib extends Library{
        Long Open(byte[] command);
        Integer Read (Long session,byte[] buffer,int bufferSize);
        Integer Write(Long session,byte[] buffer,int length);
        Integer State(Long session);
        Integer Close(Long session);
}
