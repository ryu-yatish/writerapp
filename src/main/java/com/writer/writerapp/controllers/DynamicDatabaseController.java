package com.writer.writerapp.controllers;

import com.writer.writerapp.Models.Book;
import com.writer.writerapp.Models.DynamicDbObject;
import com.writer.writerapp.Models.DynamicDbSchema;
import com.writer.writerapp.Models.RequestVO.DynamicDbSchemaRequest;
import com.writer.writerapp.Service.BookService;
import com.writer.writerapp.Service.DynamicDatabaseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

@RestController
@RequestMapping("/DDS")
@RequiredArgsConstructor
@Slf4j
public class DynamicDatabaseController {
    private final DynamicDatabaseService dynamicDatabaseService;
    private final BookService bookService;
    @PostMapping("/db-schemas/{bookId}")
    public ResponseEntity<DynamicDbSchema> createDynamicDbSchema(@PathVariable String bookId, @RequestBody DynamicDbSchemaRequest request) {
        DynamicDbSchema dynamicDbSchema =dynamicDatabaseService.createDynamicSchema(request);
        bookService.addDDSSchema(dynamicDbSchema,bookId);
        return ResponseEntity.ok(dynamicDbSchema);
    }
    @PutMapping("/{bookId}/db-schemas/{schemaId}")
    public ResponseEntity<Void> updateSchema(@PathVariable String bookId, @PathVariable String schemaId, @RequestBody DynamicDbSchema updatedSchema) {
        bookService.updateDDSSchema(bookId, schemaId, updatedSchema);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{bookId}/db-schemas/{schemaId}")
    public DynamicDbSchema getSchema(@PathVariable String bookId, @PathVariable String schemaId) {
        Optional<Book> bookOptional = bookService.getBookById(bookId);
        if (bookOptional.isEmpty()) {
            log.error("Book not found for ID: {}", bookId);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        AtomicReference<DynamicDbSchema> dbSchema = new AtomicReference<>(null);
        bookOptional.get().getDynamicDbSchemaList().forEach(db->{
            if(db.getId()!=null && db.getId().equals(schemaId)) {
                dbSchema.set(db);
            }
        });
        return dbSchema.get();
    }

    @DeleteMapping("/{bookId}/db-schemas/{schemaId}")
    public ResponseEntity<Void> deleteSchema(@PathVariable String bookId, @PathVariable String schemaId) {
        bookService.deleteDDSSchema(bookId, schemaId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/object")
    public ResponseEntity<DynamicDbObject> createDynamicDbObject(@RequestBody DynamicDbObject dynamicDbObject) {
        DynamicDbObject createdObject = dynamicDatabaseService.createDynamicDbObject(dynamicDbObject);
        return ResponseEntity.status(201).body(createdObject);
    }
    @GetMapping("/object/All")
    public ResponseEntity<List<DynamicDbObject>> getAllDynamicDbObjects() {
        List<DynamicDbObject> objects = dynamicDatabaseService.getAllDynamicDbObjects();
        return ResponseEntity.ok(objects);
    }
    @GetMapping("/object/schema/{schemaId}")
    public ResponseEntity<List<DynamicDbObject>> getAllDynamicDbObjectsBySchema(@PathVariable String schemaId) {
        List<DynamicDbObject> objects = dynamicDatabaseService.getAllDynamicDbObjectsBySchema(schemaId);
        return ResponseEntity.ok(objects);
    }

    @GetMapping("/object/{id}")
    public ResponseEntity<DynamicDbObject> getDynamicDbObjectById(@PathVariable String id) {
        Optional<DynamicDbObject> dynamicDbObject = dynamicDatabaseService.getDynamicDbObjectById(id);
        return dynamicDbObject.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(404).build());
    }

    @PutMapping("/object/{id}")
    public ResponseEntity<DynamicDbObject> updateDynamicDbObject(@PathVariable String id, @RequestBody DynamicDbObject dynamicDbObjectRequest) {
        dynamicDbObjectRequest.setId(id);
        DynamicDbObject updatedObject = dynamicDatabaseService.updateDynamicDbObject(dynamicDbObjectRequest);
        return ResponseEntity.ok(updatedObject);
    }

    @DeleteMapping("/object/{id}")
    public ResponseEntity<Void> deleteDynamicDbObject(@PathVariable String id) {
        if (dynamicDatabaseService.getDynamicDbObjectById(id).isEmpty()) {
            return ResponseEntity.status(404).build();
        }
        dynamicDatabaseService.deleteDynamicDbObject(id);
        return ResponseEntity.noContent().build();
    }
}
