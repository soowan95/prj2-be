package com.example.prj2be.mapper;

import com.example.prj2be.domain.MemberPlayList;
import com.example.prj2be.domain.MyPlaylist;
import com.example.prj2be.domain.Song;
import org.apache.ibatis.annotations.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Mapper
public interface myPlaylistMapper {
    @Select("""
            select distinct myplaylistcount, memberId as id, mpl.listName, mpl.id as listId, member.nickName, mpl.coverimage
                              from memberplaylist mpl
                                  left join myplaylist myl on mpl.id = myl.playlistId
                                  join member on mpl.memberId = member.id
                              where member.id = #{id}
            """)
    List<MyPlaylist> getMyPlayList(String id);
//    where에 memeber에 Id가 같으면 SELECT실행



    @Delete("""
            delete from memberplaylist
            where memberId = #{id}
            """)
    int deleteByMemberId(String id);

    @Select("""
            SELECT COUNT(songId) as countLike , ml.memberId, ml.listName,ml.id as listId, ml.coverimage, member.nickName
FROM memberplaylist ml JOIN playlistlike pl ON ml.id = pl.likelistId
                        JOIN myplaylist myl ON ml.id = myl.playlistId
                        JOIN member on ml.memberId = member.id
where pl.memberId = #{id}
group by pl.id;
            """)
    List<MyPlaylist> selectFavoriteList(String id);

    @Select("""
    SELECT songId FROM myplaylist m join memberplaylist p on m.playlistId = p.id
    join song s on m.songId = s.id
    where m.playlistId = #{id};
""")
    //myplaylist에 playlistId이 입력값이 같은면 songId를 출력하는 것
    List<Integer> chartlist(Integer id);

    @Select("""
  
            SELECT a.memberId as id, a.listName, a.id listId, b.nickName, a.myplaylistcount, a.coverimage, a.inserted
   FROM memberplaylist a
            join member b on a.memberId = b.id
   where a.id = #{id};
    """)
    MyPlaylist getByListId(Integer listId);
            
    @Select("""
            SELECT s.title, s.genre, s.mood, s.id, a.name `artistName`, a.`group` `artistGroup`, a.name, s.lyric, s.album, s.`release`, s.songUrl,myl.songId,myl.playlistId
FROM myplaylist myl JOIN memberplaylist mpl ON myl.playlistId = mpl.id
JOIN song s ON myl.songId = s.id
JOIN artist a ON s.artistCode = a.id
WHERE mpl.id = #{listId}
""")
    List<Song> selectByFavoriteListName(String listId); //favoriteName?


    @Delete("""
delete from myplaylist where songId = #{songId} and playlistId = #{playlistId} 
""")
    int deleteByFavoriteList(String songId, String playlistId);

    @Select("""
select a.name,a.picture,mpl.memberId, mpl.listName, pll.memberId,pll.likelistId,song.title,song.lyric,song.album,song.`release`,song.genre,song.mood,COUNT(distinct myl.songId) as songs, count.count, a.`group`, mpl.coverimage `cover`, mpl.id
from song join artist a on song.artistCode = a.id
          join myplaylist myl on song.id = myl.songId
          join memberplaylist mpl on myl.playlistId = mpl.id
          join playlistlike pll on mpl.id = pll.likelistId
          join member on mpl.memberId = member.id
          join (SELECT COUNT(*) as count, likelistId FROM playlistlike GROUP BY likelistId) `count` on pll.likelistId = count.likelistId
group by myl.playlistId
order by count desc
limit 5;

""")
    List<MemberPlayList> getRecommendPlaylist();

@Select("""
select a.name,a.picture,mpl.memberId, mpl.listName, pll.memberId,pll.likelistId,song.title,song.lyric,song.album,song.`release`,song.genre,song.mood,COUNT(distinct myl.songId) as songs, a.`group`, a.picture, mpl.coverimage `cover`, mpl.id,mpl.myplaylistcount as playlistCount
from song join artist a on song.artistCode = a.id
          join myplaylist myl on song.id = myl.songId
          join memberplaylist mpl on myl.playlistId = mpl.id
          join playlistlike pll on mpl.id = pll.likelistId
          join member on mpl.memberId = member.id
          join (SELECT COUNT(*) as count, likelistId FROM playlistlike GROUP BY likelistId) `count` on pll.likelistId = count.likelistId
group by myl.playlistId
order by myplaylistcount desc
limit 5;
""")
    List<MemberPlayList> getRecommendByViews();

    @Select("""
        SELECT s.title, s.genre, s.mood, s.id, a.name `artistName`, a.`group` `artistGroup`, s.lyric, s.album, s.`release`, s.songUrl,myl.songId, myl.playlistId, a.picture, mpl.memberId,mpl.myplaylistcount
        FROM myplaylist myl JOIN memberplaylist mpl ON myl.playlistId = mpl.id
        JOIN song s ON myl.songId = s.id
        JOIN artist a ON s.artistCode = a.id
        WHERE mpl.id = #{listId}
""")
    List<Map<String, Object>> getTopPlaylist(String listId);
            
    @Insert("""
    INSERT INTO hits (memberId, playlistId)
    values (#{memberId}, #{playlistId})
    """)
    int Inserthits(MyPlaylist myPlaylist);

    @Select("""
    SELECT count(id) FROM hits
    WHERE memberId = {#memberId}
""")
    String countBymemberId(String memberId);

    @Update("""
update memberplaylist
set myplaylistcount = myplaylistcount + 1
where id = #{id}
""")
    Integer updateHitsCount(String id);


    @Select("""
    SELECT myplaylistcount
    FROM memberplaylist
    WHERE id = #{id}
    """)
    Integer getCountById(String id);

    @Select("""
    SELECT songId
    FROM myplaylist
    WHERE playlistId = #{listId}
    """)
    List<Integer> getSongIdBylistId(String listId);
            
    @Insert("""
    insert into myplaylist (songId, playlistId) VALUES (#{songId}, #{listId})
""")
    int insertMyPlaylist(String listId, Integer songId);

    @Select("""
select * from memberplaylist
where listName = #{listName}
""")
    String selectByListName(String listName);



    @Select("""
    SELECT COUNT(*)
    FROM myplaylist
    WHERE songId = #{songId} AND playlistId = #{listId}
    """)
    Integer getCountBySongId(String songId, String listId);

    // 플레이리스트 삭제 매퍼
    @Delete("""
delete from memberplaylist
where id = #{listId}
""")
    Integer deleteByListId(String listId);

    @Delete("""
delete from myplaylist
where playlistId = #{listId}
""")
    Integer deleteSongByMyPlaylist(String listId);

    @Delete("""
delete from playlistlike
where likelistId = #{listId}
""")
    Integer deleteLikeCountByPlaylistLike(String listId);
    // 플레이리스트 삭제 매퍼 끝
            
    @Select("""
select coverimage
from memberplaylist
where id = #{id}
""")
    String getCoverImageByPlaylistId(MemberPlayList memberPlayList);

    @Insert("""
insert into memberplaylist (memberId, listName, coverimage) values (#{memberId},#{listName},#{picture})
""")
    @Options(keyProperty = "id", useGeneratedKeys = true)
    int createPlaylist(MemberPlayList memberPlayList);

    @Insert("""
    INSERT INTO memberplaylist (memberId, listName, coverimage) value (#{memberId}, #{listName}, #{picture})
    """)
    Integer createPlaylistWithDefaultImg(MemberPlayList memberPlayList);


    @Update("""
    update memberplaylist set listName = #{listName}, coverimage = #{picture}
    where id = #{id}
""")
    int editPlaylist(MemberPlayList memberPlayList);

    @Update("""
update memberplaylist set listName = #{listName}
where id = #{id}
""")
    int editPlaylistWithDefaultImg(MemberPlayList memberPlayList);
}

