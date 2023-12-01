package com.example.prj2be.mapper;

import com.example.prj2be.domain.Song;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

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
  SELECT s.title, s.genre, s.mood, s.id, a.name, a.`group`
  FROM song s JOIN artist a ON s.artistCode = a.id
              JOIN songpoint sp ON s.title = sp.title AND a.name = sp.artistName
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
              JOIN songpoint sp ON s.title = sp.title AND a.name = sp.artistName
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
  WHERE (genre=#{genre} OR mood=#{mood}) AND id != #{s.id}
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
  """)
  List<Map<String, Object>> getByRequestList();

  @Select("""
  SELECT s.id, s.title, s.lyric, s.album, s.mood, s.`release`, s.genre, a.name `artistName`,a.`group` `artistGroup` , s.titleHangulCode, s.artistHangulCode, s.lyricHangulCode
  FROM song s JOIN artist a ON s.artistCode = a.id
  WHERE s.id = #{id}
  """)
  Song getSongById(Integer id);

  @Update("""
  UPDATE songpoint
  SET songPoint = songPoint + 1
  WHERE title = #{title} AND artistName = #{artistName}
  """)
  Integer updateSongPoint2(Song song);
}
