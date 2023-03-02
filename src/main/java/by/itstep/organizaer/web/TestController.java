package by.itstep.organizaer.web;

import by.itstep.organizaer.client.WeatherClient;
import by.itstep.organizaer.model.dto.weather.Forecast;
import freemarker.cache.ClassTemplateLoader;
import freemarker.cache.FileTemplateLoader;
import freemarker.cache.TemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/test")
@RequiredArgsConstructor
public class TestController {

    private final WeatherClient client;

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/hello")
    public String hello(String name, HttpServletResponse response) throws TemplateException, IOException {
        Configuration cfg = new Configuration(Configuration.VERSION_2_3_27);

        final ClassTemplateLoader loader = new ClassTemplateLoader(TestController.class, "/ftl");
        cfg.setTemplateLoader(loader);
        cfg.setDefaultEncoding("UTF-8");

// модель данных
        Map<String, Object> root = new HashMap<>();
        root.put("name", "Maxim");
        root.put("currentDate", LocalDateTime.now());
// шаблон
        Template temp = cfg.getTemplate("test.ftl");
// обработка шаблона и модели данных
// вывод в консоль
        Writer w = new StringWriter();
        temp.process(root, w);
        return w.toString();
    }

    @GetMapping("/echo")
    public String echo(@RequestParam String string, @RequestHeader(HttpHeaders.USER_AGENT) String userAgent) {
        if (StringUtils.containsAnyIgnoreCase(userAgent, "PostmanRuntime")) {
            return "Postman is not supported";
        }
        return string;
    }

    @GetMapping("/forecast")
    public ResponseEntity<Forecast> forecast(@RequestParam String city) {
        return ResponseEntity.ok(client.getForecat(city));
    }
}
