package com.example.prj2be.mapper;

import com.example.prj2be.domain.MyPlaylist;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

@Mapper
public interface myPlaylistMapper {
    @Insert("""
            insert into myplaylist (listId,listName)
            values (#{listId}, #{listName})
            """)
    int insert(MyPlaylist playlist);

    @Select("""
            SELECT *
            FROM myplaylist
            where listId = #{listId}
            """)
    List<MyPlaylist> getMyPlayList(String listId);

    @Select("""
            select * from myplaylist
            where listId = #{listId}            
            """)
    List<MyPlaylist> myListByListName(String listId, String listName);
}
