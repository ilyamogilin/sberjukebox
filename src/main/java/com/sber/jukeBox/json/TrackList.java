package com.sber.jukeBox.json;

import com.sber.jukeBox.model.TrackEntity;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class TrackList {

    private TrackEntity nowPlaying;

    private List<TrackEntity> trackList;
}
