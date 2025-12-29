package com.example.MobileAppBackend.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Slf4j
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Document(collection = "tags")
public class Tag {

    @Id
    private String id;
    private String name;
}
