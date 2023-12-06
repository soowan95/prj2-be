package com.example.prj2be.mapper;

import com.example.prj2be.domain.MyPlaylist;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

@Mapper
public interface myPlaylistMapper {
    @Select("""
            SELECT a.memberId as id, a.listName, a.id listId FROM memberplaylist a
            join member b on a.memberId = b.id
            where b.id = #{id}
            """)
    List<MyPlaylist> getMyPlayList(String id);
//    where에 memeber에 Id가 같으면 SELECT실행

    @Select("""
            select title,lyric,album,`release`,genre, artist.name
            from song join artist on song.artistCode = artist.id
            """)
    List<Map<String, Object>> selectRecommended();

    @Delete("""
            delete from memberplaylist
            where memberId = #{id}
            """)
    int deleteByMemberId(String id);

    @Select("""
            select b.memberId, b.listName as songs from playlistlike a
            join memberplaylist b on a.likelistId = b.id
        
            where a.memberId = #{id}
            """)
    List<Map<String, Object>> selectFavoriteList(String id);
}
