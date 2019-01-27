package com.sber.jukeBox.json;

import com.sber.jukeBox.model.TrackEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TrackList {

    private TrackEntity nowPlaying;

    private List<TrackEntity> trackList;
}
