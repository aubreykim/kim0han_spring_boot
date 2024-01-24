package hello.hellospring.controller;

import hello.hellospring.service.Prometheus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Map;

@Controller
public class MonitoringController {

    private final Prometheus prometheus;

    @Autowired
    public MonitoringController(Prometheus prometheus) {
        this.prometheus = prometheus;
    }

    @GetMapping("monitoring")
    public String monitoring(Model model) throws Exception {
        Map<String, Object> resultMap = prometheus.getPrometheus();
        model.addAttribute("data", resultMap);
        return "monitoring";
    }

}
