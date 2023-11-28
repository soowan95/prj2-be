package com.example.prj2be.mapper;

import com.example.prj2be.domain.MyPlaylist;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

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
}
