package com.example.prj2be.mapper;

import com.example.prj2be.domain.MyPlaylist;
import com.example.prj2be.domain.Song;
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
    SELECT *
    FROM myplaylist
    """)
    List<MyPlaylist> getAllList();

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
            SELECT COUNT(songId) as songs , ml.memberId, ml.listName, myl.playlistId
FROM memberplaylist ml JOIN playlistlike pl ON ml.id = pl.likelistId
                        JOIN myplaylist myl ON ml.id = myl.playlistId
where pl.memberId = #{id}
group by pl.id;
            """)
    List<Map<String, Object>> selectFavoriteList(String id);

    @Select("""

            SELECT s.title, s.genre, s.mood, s.id, a.name `artistName`, a.`group` `artistGroup`, a.name, s.lyric, s.album, s.`release`, s.songUrl,myl.songId,myl.playlistId
FROM myplaylist myl JOIN memberplaylist mpl ON myl.playlistId = mpl.id
JOIN song s ON myl.songId = s.id
JOIN artist a ON s.artistCode = a.id
WHERE mpl.id = #{listId}
""")
    List<Song> selectByFavoriteListName(String listId);


    @Delete("""
delete from myplaylist where songId = #{songId} and playlistId = #{playlistId} 
""")
    int deleteByFavoriteList(String songId, String playlistId);



}
