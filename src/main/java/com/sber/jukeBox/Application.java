package com.sber.jukeBox;

import com.sber.jukeBox.datastore.JukeBoxStoreImpl;
import com.sber.jukeBox.model.TrackEntity;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Application {

//    @Bean
//    public CommandLineRunner demoData() {
//        return args -> {
//
//            TrackEntity track1 = new TrackEntity(1, 1, "track name", "artist", "someUrl");
//            TrackEntity track2 = new TrackEntity(1, 1, "track", "art", "url");
//            TrackEntity track3 = new TrackEntity(2, 1, "track name2", "artist2", "someUrl2");
//
//            JukeBoxStoreImpl jukeBoxStore = JukeBoxStoreImpl.getInstance();
//
//            jukeBoxStore.addTrack(track1);
//            jukeBoxStore.addTrack(track2);
//            jukeBoxStore.addTrack(track3);
//            jukeBoxStore.getTracksWithNowPlaying(1);
//        };
//    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }


}
