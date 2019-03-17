package ru.jointvibe.common.pojo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * This java Entity for a music which you get from VK bot
 * @userId - vk's user id
 * @trackName - the name of the music
 * @artistName - the name of the music's artist
 * @trackUrl - the url of the music from Vk
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TrackEntity {

    private int userId;
    private String jukeboxId;
    private String trackName;
    private String artistName;
    private String trackUrl;

    public String getFullName(){
        return artistName + " - " + trackName;
    }
}
