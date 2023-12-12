package com.example.prj2be.mapper;

import com.example.prj2be.domain.PlaylistLike;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface LikeMapper {
    @Delete("""
                DELETE FROM playlistlike
                WHERE memberId = #{memberId} AND likelistId = #{likelistId}
            """)
    int delete(PlaylistLike playlistLike);

    @Insert("""
                INSERT INTO playlistlike (memberId, likelistId)
                values (#{memberId}, #{likelistId})
            """)
    int insert(PlaylistLike playlistLike);


    @Select("""
            SELECT count(id) FROM playlistlike
            WHERE  likelistId = #{likelistId}
            """)
    Integer countByBoardId(String likelistId);

    @Select("""
            SELECT * FROM playlistlike
            WHERE likelistId = #{likelistId}
            AND memberId = #{memberId}
            """)
    PlaylistLike selectByBoardIdAndMemberId(String likelistId, String memberId);

    @Select("""
    SELECT COUNT(id)
    FROM playlistlike
    WHERE memberId = #{memberId} AND likelistId = #{likelistId}
    """)
    Integer isLike(String memberId, String likelistId);

    @Delete("""
            delete from playlistlike
            where memberId = #{id}
            """)
    int deleteByMemberId(String id);

    @Select("""
    SELECT Count(id)
    FROM song
    where id = #{id} 
""")
    Integer countBySongId(Integer id);
}


