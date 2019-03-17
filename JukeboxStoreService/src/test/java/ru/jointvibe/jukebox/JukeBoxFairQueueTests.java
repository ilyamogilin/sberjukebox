package ru.jointvibe.jukebox;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.jointvibe.common.pojo.TrackEntity;
import ru.jointvibe.common.pojo.TrackList;
import ru.jointvibe.jukebox.store.api.JukeBox;
import ru.jointvibe.jukebox.store.JukeBoxImpl;

import java.util.List;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author FORESTER
 */
public class JukeBoxFairQueueTests {

    private JukeBox jukeBox;

    @BeforeEach
    public void clearJukeBox() {
        jukeBox = new JukeBoxImpl();
    }

    @Test
    public void directOrderTest() {
        test(asList(1, 2, 3, 4, 5), asList(1, 2, 3, 4, 5));
    }

    @Test
    public void mixedOrderTest() {
        test(asList(1, 1, 2, 2, 3, 3, 4, 2), asList(1, 2, 3, 4, 1, 2, 3, 2));
    }

    @Test
    public void reverseOrderTest() {
        test(asList(4, 2, 3, 1), asList(4, 2, 3, 1));
    }

    private void test(List<Integer> inputUserIds, List<Integer> outputUserIds) {
        inputUserIds.forEach(userId -> jukeBox.addTrack(track(userId)));
        assertOrder(jukeBox.getTrackList(), outputUserIds);
    }

    private TrackEntity track(int userId) {
        return TrackEntity.builder()
                .userId(userId)
                .build();
    }

    private void assertOrder(TrackList trackList, List<Integer> outputUserIds) {
        assertEquals(outputUserIds,
                trackList.getTracks().stream().map(TrackEntity::getUserId).collect(toList()));
    }
}
