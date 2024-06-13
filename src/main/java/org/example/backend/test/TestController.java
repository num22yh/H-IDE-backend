package org.example.backend.test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class TestController {

    @Autowired
    private TestService testService;

    @GetMapping("/dbtest")
    public String getAllEntities(Model model) {
        List<TestEntity> entities = testService.getAllEntities();
        model.addAttribute("entities", entities);
        return "dbtest";  // Thymeleaf 템플릿 이름 반환 (src/main/resources/templates/dbtest.html)
    }
}
