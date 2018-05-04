package com.akchen.WebIDE.Interface;

import com.akchen.WebIDE.Interface.PTLib;
import com.sun.jna.Native;
import com.sun.jna.NativeLibrary;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public abstract class JNAService {
    private static PTLib ptlib = null;

    @Value("${PTLib.Name64}")
    String PTLibName;
    @PostConstruct
    void loadLib() {
        ptlib = (PTLib) Native.loadLibrary(PTLibName, PTLib.class);
    }
    public static PTLib getInstance(){
        return ptlib;
    }

    public abstract Long CreateSession(String srcFile);
    public abstract InputStream GetInputStream(Long session);
    public abstract OutputStream GetOutpuStream(Long session);
    public abstract Integer GetSessionState(Long session);
    public abstract Integer CloseSession(Long session);

}
