package com.akchen.WebIDE.Thread;

import com.akchen.WebIDE.Bean.PTSessionBean;
import com.akchen.WebIDE.Util.IDEStringHelper;
import com.akchen.WebIDE.Util.IOHelper;
import org.springframework.web.socket.TextMessage;

import java.io.IOException;

import java.io.UnsupportedEncodingException;
import java.util.Date;





public class PTDaemonThread implements Runnable {
    private static final Long MAX_DELAY = 20*1000L;//20 seconds
    private PTSessionBean bean;
    private Long lastAlive = new Date().getTime();
    public PTDaemonThread(PTSessionBean bean) {
        this.bean = bean;
    }

    private void print(byte[] temp,int nread){
        byte[] msg = new byte[nread];
        for (int i = 0; i < nread; i++) {
            msg[i] = temp[i];
        }
        String s = null;
        try {
            s = new String(msg,"gbk");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String[] lines = s.split("\r\n");
        for (String line : lines) {
            if (bean.getWsSession().isOpen()) {
                try {

                    bean.getWsSession().sendMessage(new TextMessage(IDEStringHelper.doEncode("op@" + line)));//send to client
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void run() {
        // is alive
        byte[] temp = new byte[1024];
        int nread = 0;
        //正常退出是0，非正常退出为1
        while (true) {
            try {
                nread = bean.getInputStream().read(temp);
            } catch (IOException e) {
                e.printStackTrace();
            }
            int code = bean.getJnaService().GetSessionState(bean.getPtSession());

            if((code ==0 || code == 1) && nread ==0 || (new Date().getTime() - lastAlive) > MAX_DELAY){
                Long t1 = new Date().getTime();
                while(new Date().getTime() - t1 < 1000){
                    try {
                        nread = bean.getInputStream().read(temp);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if(nread >0){
                        print(temp,nread);
                        lastAlive = new Date().getTime();//update time
                    }
                }
                break;
            }

            if (nread > 0) {
                print(temp,nread);
                lastAlive = new Date().getTime();//update time
            }
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        //send exit
        if (bean.getWsSession().isOpen()) {
            try {
                bean.getWsSession().sendMessage(new TextMessage(IDEStringHelper.doEncode("et@et")));//send to client
                IOHelper.DeleteCode(bean.getFilePath());
                bean.getJnaService().CloseSession(bean.getPtSession());
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                bean.getWsSession().close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
