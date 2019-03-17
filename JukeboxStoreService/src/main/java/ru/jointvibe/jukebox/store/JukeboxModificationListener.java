package ru.jointvibe.jukebox.store;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.jointvibe.jukebox.service.PlayerWebServiceClient;
import ru.jointvibe.jukebox.store.api.JukeBoxStore;

@Component
public class JukeboxModificationListener {

    @Autowired
    private JukeBoxStore jukeBoxStore;

    @Autowired
    private PlayerWebServiceClient playerWebServiceClient;

    public void onTrackAdded(String jukeBoxId){
       playerWebServiceClient.refreshTrackList(jukeBoxId, jukeBoxStore.getTrackListOrPopIfEmpty(jukeBoxId));
    }
}
