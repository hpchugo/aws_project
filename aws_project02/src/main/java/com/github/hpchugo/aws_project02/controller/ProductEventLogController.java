package com.github.hpchugo.aws_project02.controller;

import com.github.hpchugo.aws_project02.model.ProductEventLogDto;
import com.github.hpchugo.aws_project02.repository.ProductEventLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@RestController
@RequestMapping("/api")
public class ProductEventLogController {

    private final ProductEventLogRepository productEventLogRepository;

    @Autowired
    public ProductEventLogController(ProductEventLogRepository productEventLogRepository) {
        this.productEventLogRepository = productEventLogRepository;
    }

    @GetMapping("/events")
    public List<ProductEventLogDto> getAllEvents() {
        return StreamSupport
                .stream(productEventLogRepository.findAll().spliterator(), false)
                .map(ProductEventLogDto::new)
                .collect(Collectors.toList());
    }


    @GetMapping("/events/{code}")
    public ResponseEntity<List<ProductEventLogDto>> getEventsByCode(@PathVariable String code){
        return ResponseEntity.ok(StreamSupport
                .stream(productEventLogRepository.findAllByPk(code).spliterator(), false)
                .map(ProductEventLogDto::new)
                .collect(Collectors.toList())
        );
    }

    @GetMapping("/events/{code}/{event}")
    public ResponseEntity<List<ProductEventLogDto>> getEventsByCodeAndEventType(@PathVariable String code, @PathVariable String event){
        return ResponseEntity.ok(StreamSupport
                .stream(productEventLogRepository.findAllByPkAndSkStartsWith(code, event).spliterator(), false)
                .map(ProductEventLogDto::new)
                .collect(Collectors.toList())
        );
    }
}
