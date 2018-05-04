package com.akchen.WebIDE.Controller;

import com.akchen.WebIDE.Service.PythonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class WebIDEController {
                @GetMapping("/")
                public String home(){
                    return "web";
        }
}
