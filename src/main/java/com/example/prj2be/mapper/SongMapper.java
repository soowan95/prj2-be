package com.example.prj2be.mapper;

import com.example.prj2be.domain.Song;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

@Mapper
public interface SongMapper {

  @Select("""
  SELECT s.id, title, lyric, album, mood, `release`, genre, a.name `artistName`,a.`group` `artistGroup` , titleHangulCode, artistHangulCode, lyricHangulCode
  FROM song s JOIN artist a ON s.artistCode = a.id
  """)
  List<Song> getAll();

  @Select("""
  SELECT s.title, s.genre, s.mood, s.id, a.name `artistName`, a.`group` `artistGroup`, a.name, s.lyric, s.album, s.`release`, s.songUrl
  FROM song s JOIN artist a ON s.artistCode = a.id
              JOIN songpoint sp ON s.title = sp.title AND a.id = sp.artistId
  ORDER BY sp.songPoint DESC 
  LIMIT 0, 100
  """)
  List<Song> getSongLimit100();

  @Select("""
  SELECT mainMood, id
  FROM mood
  """)
  List<Map<String, Object>> getMood();

  @Select("""
  SELECT genre, id
  FROM genre
  """)
  List<Map<String, Object>> getGenre();

  @Select("""
  <script>
  SELECT s.title, s.genre, s.mood, s.id, a.name, a.`group`
  FROM song s JOIN artist a ON s.artistCode = a.id
              JOIN songpoint sp ON s.title = sp.title AND a.id = sp.artistId
  <trim prefix="WHERE" suffixOverrides="AND">
  <foreach collection="genreIncludeList" item="elem" open="(" separator="OR" close=") AND" nullable="true">
  s.genre LIKE #{elem}
  </foreach>
  <foreach collection="moodIncludeList" item="elem" open="(" separator="OR" close=")">
  s.mood LIKE #{elem}
  </foreach>
  </trim>
  ORDER BY sp.songPoint DESC 
  LIMIT 0, 100
  </script>
  """)
  List<Song> getByFilter(List<String> genreIncludeList, List<String> moodIncludeList);

  @Select("""
  <script>
  SELECT s.title, s.genre, s.mood, s.id, a.name, a.`group`
  FROM song s JOIN artist a ON s.artistCode = a.id
  WHERE 
  <if test='category == "가수"'>
  a.name
  </if>
  <if test='category == "제목"'>
  title
  </if>
  <if test='category == "가사"'>
  lyric
  </if>
  LIKE #{keyword}
  <foreach collection="genreIncludeList" item="elem" open="AND ( " separator="OR" close=")" nullable="true">
  genre LIKE #{elem}
  </foreach>
  <foreach collection="moodIncludeList" item="elem" open=" AND ( " separator="OR" close=")" nullable="true">
  mood LIKE #{elem}
  </foreach>
  </script>
  """)
  List<Song> getByCategoryAndKeyword(String category, String keyword, List<String> genreIncludeList, List<String> moodIncludeList);

  @Select("""
  SELECT s.title, s.genre, s.mood, s.id, a.name, a.`group`
  FROM song s JOIN artist a ON s.artistCode = a.id
  WHERE (genre LIKE #{genre} OR mood LIKE #{mood}) AND s.id != #{id}
  """)
  List<Song> getByGenreAndMood(String genre, String mood, Integer id);

  @Insert("""
  INSERT INTO songRequest (title, artist, member)
  VALUE (#{title}, #{artist}, #{member}) 
  """)
  int insertRequest(Map<String, String> request);

  @Select("""
  SELECT *
  FROM songrequest
  ORDER BY 5 DESC;
  """)
  List<Map<String, Object>> getByRequestList();

  @Select("""
  SELECT id,title,album,mood,`release`,genre
  FROM song 
""")
  List<Song> chartlist();

  @Select("""
  SELECT id
  FROM artist
  WHERE name = #{artistName} AND `group` = #{artistGroup}
  """)
  Integer getArtistCode(Song song);

  @Insert("""
  INSERT INTO song (title, lyric, album, mood, `release`, genre, artistCode, titleHangulCode, artistHangulCode, lyricHangulCode, songUrl)
  VALUE (#{song.title}, #{song.lyric}, #{song.album}, #{song.mood}, #{song.release}, #{song.genre}, #{artistCode}, #{song.titleHangulCode}, #{song.artistHangulCode}, #{song.lyricHangulCode},#{song.songUrl})
  """)
  @Options(useGeneratedKeys = true, keyProperty = "song.id")
  Integer insertSong(Song song, Integer artistCode);

  @Select("""
  SELECT s.id, s.title, s.lyric, s.album, s.mood, s.release, s.genre, a.name `artistName`,a.`group` `artistGroup` , s.titleHangulCode, s.artistHangulCode, s.lyricHangulCode
          , a.picture artistFileUrl, s.artistCode artistId
  FROM song s JOIN artist a ON s.artistCode = a.id
  WHERE s.id = #{id}
  """)
  Song getSongById(Integer id);

  @Update("""
  UPDATE songpoint
  SET songPoint = songPoint + 1
  WHERE title = #{song.title} AND artistId = #{artistCode}
  """)
  Integer updateSongPoint(Song song, Integer artistCode);

  @Select("""
select song.id,song.genre,song.artistCode,song.mood,song.`release`,song.lyric,song.title,artist.name,artist.`group`,artist.picture
from
song join artist on song.artistCode = artist.id
WHERE album = #{album}
""")
  List<Map<String, Object>> getByAlbumList(String album);

  @Delete("""
    DELETE FROM song
    WHERE id = #{id}
    """)
  int deleteById(String id);

  @Insert("""
  <script>
  INSERT INTO artist 
  <if test='song.artistGroup == ""'>
  (name, picture) VALUE (#{song.artistName}, #{fileName})
  </if>
  <if test='song.artistGroup != ""'>
  (name, `group`, picture) VALUE (#{song.artistName}, #{song.artistGroup}, #{fileName})
  </if>
  </script>
  """)
  @Options(useGeneratedKeys = true, keyProperty = "song.artistId")
  Integer insertArtist(Song song, String fileName);

  @Insert("""
  INSERT INTO songpoint (title, artistId)
  VALUE (#{song.title}, #{artistCode})
  """)
  Integer insertSongPoint(Song song, Integer artistCode);

  @Update("""
  UPDATE songrequest
  SET updated = NOW()
  WHERE (title = #{title} OR title = #{requestTitle}) AND (artist = #{artistName} OR artist = #{requestArtist})
  """)
  void updateSongRequest(Song song);

  @Select("""
  SELECT *
  FROM songrequest
  WHERE member = #{memberId}
  ORDER BY 5 DESC ;
  """)
  List<Map<String, Object>> getMySongRequestList(String memberId);
          
  @Update("""
UPDATE song
SET title = #{song.title},
    album = #{song.album},
    artistCode = #{artistCode}
WHERE id = #{song.id}
""")
  int updateSong(Song song, Integer artistCode);
          
  @Select("""
  SELECT artistCode
  FROM song
  WHERE id = #{songId}
  """)
  Integer getArtistCodeBySongId(Integer songId);
}