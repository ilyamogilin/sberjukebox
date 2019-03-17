package ru.jointvibe.common.pojo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

import static java.util.Collections.emptyList;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TrackList {

    public static TrackList emptyPlaylist(){
        return TrackList.builder().tracks(emptyList()).build();
    }

    public TrackList(List<TrackEntity> trackList) {
        this.tracks = trackList;
    }

    private TrackEntity nowPlaying;

    private List<TrackEntity> tracks;
}
