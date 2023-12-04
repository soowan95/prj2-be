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

            SELECT a.memberId, a.listName FROM memberplaylist a
            join member b on a.memberId = b.id
            where b.id = #{id}
            
            """)
    List<MyPlaylist> getMyPlayList(String id);
            
    @Select("""
    SELECT *
    FROM myplaylist
    """)
    List<MyPlaylist> getAllList();

    @Select("""
            select title,lyric,album,`release`,genre, artist.name
            from song join artist on song.artistCode = artist.id
            """)
    List<Map<String,Object>> selectRecommended();
}
