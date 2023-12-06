package com.example.prj2be.domain;

import lombok.Data;

import java.util.List;

@Data
public class Artist {
    private Integer id;
    private String picture;
    private List<String> fileNames;
}
