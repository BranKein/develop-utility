package com.yhkim.developutility.controller;

import io.swagger.annotations.Api;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Api(tags = "Json API")
@RequestMapping("/api/json")
@RestController
@AllArgsConstructor
public class JsonController {

    @PostMapping("/stringify")
    public ResponseEntity<String> stringify(@RequestBody String json) {
        String result = json.replaceAll(" ", "");
        result = result.replaceAll("\t", "");
        return ResponseEntity.ok(result);
    }
}
