package com.example.wenhai.listenall.data.bean;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;

/**
 * 歌单与歌曲一对多关系
 */

@Entity
public class JoinCollectsWithSongs {
    @Id
    private Long id;
    private Long songId;
    private Long collectId;

    public JoinCollectsWithSongs(Long id, Long songId, Long collectId) {
        this.id = id;
        this.songId = songId;
        this.collectId = collectId;
    }

    public JoinCollectsWithSongs() {
    }

    public static JoinCollectsWithSongs newRecord(long songId, long collectId) {
        JoinCollectsWithSongs record = new JoinCollectsWithSongs();
        record.songId = songId;
        record.collectId = collectId;
        return record;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSongId() {
        return this.songId;
    }

    public void setSongId(Long songId) {
        this.songId = songId;
    }

    public Long getCollectId() {
        return this.collectId;
    }

    public void setCollectId(Long collectId) {
        this.collectId = collectId;
    }
}
