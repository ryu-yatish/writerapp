package com.writer.writerapp.Service;

import com.writer.writerapp.Models.DDSProperty;
import com.writer.writerapp.Models.DynamicDbObject;
import com.writer.writerapp.Models.DynamicDbSchema;
import com.writer.writerapp.Models.RequestVO.DynamicDbSchemaRequest;
import com.writer.writerapp.Repositories.DynamicDbObjectRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;


@Service
@Slf4j
@RequiredArgsConstructor
public class DynamicDatabaseService {

    private final DynamicDbObjectRepository dynamicDbObjectRepository;

    public DynamicDbObject createDynamicDbObject(DynamicDbObject dynamicDbObject) {
        return dynamicDbObjectRepository.save(dynamicDbObject);
    }

    public List<DynamicDbObject> getAllDynamicDbObjects() {
        return dynamicDbObjectRepository.findAll();
    }
    public List<DynamicDbObject> getAllDynamicDbObjectsBySchema(String schemaId) {
        return dynamicDbObjectRepository.findBySchemaId(schemaId);
    }

    public Optional<DynamicDbObject> getDynamicDbObjectById(String id) {
        return dynamicDbObjectRepository.findById(id);
    }

    public DynamicDbObject updateDynamicDbObject(DynamicDbObject dynamicDbObjectRequest) {
        Optional<DynamicDbObject> dynamicDbObjectOptional = dynamicDbObjectRepository.findById(dynamicDbObjectRequest.getId());
        if (dynamicDbObjectOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatusCode.valueOf(404));
        }
        DynamicDbObject dynamicDbObject = dynamicDbObjectOptional.get();
        Map<String,String> oldData = dynamicDbObject.getData();
        oldData.putAll(dynamicDbObjectRequest.getData());
        dynamicDbObject.setData(oldData);
        return dynamicDbObjectRepository.save(dynamicDbObject);
    }

    public void deleteDynamicDbObject(String id) {
        dynamicDbObjectRepository.deleteById(id);
    }

    public DynamicDbSchema createDynamicSchema(DynamicDbSchemaRequest request) {

        return DynamicDbSchema.builder()
                .id(String.valueOf(UUID.randomUUID()))
                .name(request.getName())
                .propertiesMap(request.getPropertiesMap().entrySet().stream()
                        .collect(Collectors.toMap(
                                Map.Entry::getKey,
                                e -> DDSProperty.builder()
                                        .type(e.getValue().getType())
                                        .regex(e.getValue().getRegex())
                                        .required(e.getValue().isRequired())
                                        .defaultValue(e.getValue().getDefaultValue())
                                        .description(e.getValue().getDescription())
                                        .minLength(e.getValue().getMinLength())
                                        .maxLength(e.getValue().getMaxLength())
                                        .minValue(e.getValue().getMinValue())
                                        .maxValue(e.getValue().getMaxValue())
                                        .allowedValues(e.getValue().getAllowedValues())
                                        .customAttributes(e.getValue().getCustomAttributes())
                                        .build()
                        )))
                .build();
    }

}
