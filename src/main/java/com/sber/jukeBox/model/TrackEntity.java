package com.sber.jukeBox.model;

import lombok.AllArgsConstructor;
import lombok.Data;


/**
 * This java Entity for a music which you get from VK bot
 * @userId - vk's user id
 * @trackName - the name of the music
 * @artistName - the name of the music's artist
 * @trackUrl - the url of the music from Vk
 */
@Data
@AllArgsConstructor
public class TrackEntity {

    private int userId;
    private String trackName;
    private String artistName;
    private String trackUrl;
}
