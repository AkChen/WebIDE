package com.akchen.WebIDE.Service;


import com.akchen.WebIDE.Interface.JNAService;
import com.akchen.WebIDE.Interface.PTLib;
import com.akchen.WebIDE.Interface.PTLibInputStream;
import com.akchen.WebIDE.Interface.PTLibOutputStream;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.InputStream;
import java.io.OutputStream;

@Service
public class JavaService extends JNAService {
    private static PTLib ptLib = null;
    @PostConstruct
    void get(){
        ptLib = JNAService.getInstance();

    }
    @Override
    public Long CreateSession(String srcFile) {
        String cmd  = "python "+srcFile;
        byte[] cmdBytes = cmd.getBytes();
        return ptLib.Open(cmdBytes);
    }


    @Override
    public InputStream GetInputStream(Long session) {
        return new PTLibInputStream(session,ptLib);
    }

    @Override
    public OutputStream GetOutpuStream(Long session) {
        return new PTLibOutputStream(session,ptLib);
    }

    @Override
    public Integer GetSessionState(Long session) {
        return ptLib.State(session);
    }

    @Override
    public Integer CloseSession(Long session) {
        return ptLib.Close(session);
    }
}
