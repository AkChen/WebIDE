package com.akchen.WebIDE.Service;

import com.akchen.WebIDE.Bean.PTSessionBean;
import com.akchen.WebIDE.Interface.JNAService;
import com.akchen.WebIDE.Thread.PTDaemonThread;
import com.akchen.WebIDE.Util.IDEStringHelper;
import com.akchen.WebIDE.Util.IOHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
    public class WebIDESocketService {
        @Autowired
        PythonService pythonService;
        @Autowired
        JavaService   javaService;

        @Value("${PTLib.CODETEMP}")
        String CodeTempPath;
        private Map<WebSocketSession,PTSessionBean> maps;

        public  WebIDESocketService(){
            maps = new HashMap<>();
        }

        @PostConstruct
        void init(){
            File file = new File(CodeTempPath);
            if(!file.exists()){
                file.mkdir();
            }
        }
        public void HandleMessage(WebSocketSession session, String message){
            // client to server
            String[] split= message.split("@");
            //open a new process; us@[lang]@code
            if(split[0].equals("us") && maps.get(session) == null){//upload to server
                //save code file
                String lang = split[1];
                String code = split[2];
                String guid = java.util.UUID.randomUUID().toString();
                PTSessionBean ptSessionBean = new PTSessionBean();
                JNAService jnaService = null ;
                if(lang.equals("1")){//python
                    guid += ".py";
                    jnaService = pythonService;
                }
               else if(lang.equals("2")){//java
                    guid += ".java";
                    jnaService = javaService;
                }else{//default
                    guid += ".py";
                    jnaService = pythonService;
                }
                //and more lang. support
               String savedFileName =  IOHelper.SaveCode(CodeTempPath+"\\"+guid,code);
                //Create Session
                Long ptSession =  jnaService.CreateSession(savedFileName);
                ptSessionBean.setFilePath(savedFileName);
                ptSessionBean.setWsSession(session);
                ptSessionBean.setPtSession(ptSession);
                ptSessionBean.setInputStream(jnaService.GetInputStream(ptSession));
                ptSessionBean.setOutputStream(jnaService.GetOutpuStream(ptSession));
                ptSessionBean.setJnaService(jnaService);
                //Create Thread for daemon and output.
                new Thread(new PTDaemonThread(ptSessionBean)).start();
                //return ok
                try {
                    session.sendMessage(new TextMessage(IDEStringHelper.doEncode("us@ok")));
                } catch (IOException e) {
                    e.printStackTrace();
                }

                //add
                maps.put(session,ptSessionBean);
            }else if(split[0].equals("ip")){//input
                PTSessionBean bean = maps.get(session);
                String inputString = split[1]+"\r\n";//mean press 'Enter'
                byte[] inputBytes = inputString.getBytes();
                //write them
                try {
                    bean.getOutputStream().write(inputBytes);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }else{//非法
                try {
                    session.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
      public void HandleConnect(WebSocketSession session){

      }
        public void HandleClose(WebSocketSession session){
            PTSessionBean bean = maps.get(session);
            if(bean != null){
                bean.getJnaService().CloseSession(bean.getPtSession());
                maps.remove(session);
                IOHelper.DeleteCode(bean.getFilePath());
            }
        }
}
