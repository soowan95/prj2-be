package com.example.prj2be.domain;

import lombok.Data;

@Data
public class MyPlaylist {
    private Integer id;
    private String listId;
    private String listName;
    private Boolean isLike;




//    public String getAgo() {
//        return AppUtil.getAgo(listId);
//    }

}
