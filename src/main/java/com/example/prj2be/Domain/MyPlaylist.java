package com.example.prj2be.Domain;

import com.example.prj2be.Util.AppUtil;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MyPlaylist {
    private Integer id;
    private String listId;
    private String listName;




//    public String getAgo() {
//        return AppUtil.getAgo(listId);
//    }

}
