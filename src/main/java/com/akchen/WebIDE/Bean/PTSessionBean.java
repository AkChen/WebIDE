package com.akchen.WebIDE.Bean;

import com.akchen.WebIDE.Interface.JNAService;
import com.akchen.WebIDE.Interface.PTLib;
import jdk.internal.util.xml.impl.Input;
import org.springframework.web.socket.WebSocketSession;

import java.io.InputStream;
import java.io.OutputStream;

public class PTSessionBean {
    private Long ptSession;
    private WebSocketSession wsSession;
    private String FilePath;
    private InputStream inputStream ;
    private OutputStream outputStream;
    private JNAService jnaService;

    public JNAService getJnaService() {
        return jnaService;
    }

    public void setJnaService(JNAService jnaService) {
        this.jnaService = jnaService;
    }

    public InputStream getInputStream() {
        return inputStream;
    }

    public void setInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    public OutputStream getOutputStream() {
        return outputStream;
    }

    public void setOutputStream(OutputStream outputStream) {
        this.outputStream = outputStream;
    }

    public Long getPtSession() {
        return ptSession;
    }

    public void setPtSession(Long ptSession) {
        this.ptSession = ptSession;
    }

    public WebSocketSession getWsSession() {
        return wsSession;
    }

    public void setWsSession(WebSocketSession wsSession) {
        this.wsSession = wsSession;
    }

    public String getFilePath() {
        return FilePath;
    }

    public void setFilePath(String filePath) {
        FilePath = filePath;
    }
}
