package com.sber.jukeBox.datastore;

import com.sber.jukeBox.datastore.api.JukeBox;
import com.sber.jukeBox.json.TrackList;
import com.sber.jukeBox.model.TrackEntity;
import org.junit.*;

import java.util.List;

import static com.sber.jukeBox.json.TrackList.emptyPlaylist;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.junit.Assert.*;

/**
 * @author FORESTER
 */
public class JukeBoxPushPopTests {

    private JukeBox jukeBox;

    @Before
    public void clearJukeBox() {
        jukeBox = new JukeBoxImpl();
    }

    @Test
    public void popFromEmptyTest() {
        assertEquals(emptyPlaylist(), pop());
    }

    @Test
    public void pushOnePopOneTest() {
        push(1, "1");
        assertEquals(
                trackList(track(1, "1"), emptyList()),
                pop());
    }

    @Test
    public void pushSeveralPopOneTest() {
        push(1, "1");
        push(1, "2");
        assertEquals(
                trackList(track(1, "1"), singletonList(track(1, "2"))),
                pop());
    }

    @Test
    public void pushTwoPopTwoTest() {
        push(1, "1");
        push(1, "2");
        pop();
        assertEquals(
                trackList(track(1, "2"), emptyList()),
                pop());
    }

    @Test
    public void pushOnePopTwoTest() {
        push(1, "1");
        pop();
        assertEquals(emptyPlaylist(), pop());
        assertEquals(emptyPlaylist(), pop());
    }

    @Test
    public void mixedPushPopTest1() {
        push(1, "1");
        pop();
        push(1, "2");
        assertEquals(
                trackList(track(1, "2"), emptyList()),
                pop());
    }

    @Test
    public void mixedPushPopTest2() {
        push(1, "1");
        push(1, "2");
        pop();
        push(1, "3");
        push(1, "4");
        assertEquals(
                trackList(track(1, "2"), asList(
                        track(1, "3"),
                        track(1, "4"))),
                pop());
    }

    @Test
    public void fairPushTest() {
        push(1, "1");
        push(2, "1");
        push(2, "2");
        pop();
        push(1, "2");
        assertEquals(
                trackList(track(2, "1"), asList(
                        track(1, "2"),
                        track(2, "2"))),
                pop());
    }

    @Test
    public void fairPushTest2() {
        push(1, "1");
        push(1, "2");
        push(2, "1");
        push(2, "2");
        assertEquals(
                trackList(track(1, "1"), asList(
                        track(2, "1"),
                        track(1, "2"),
                        track(2, "2"))),
                pop());
    }

    @Test
    public void fairPushTest3() {
        push(1, "1");
        push(1, "2");
        push(2, "1");
        pop();
        push(1, "3");
        assertEquals(
                trackList(track(2, "1"), asList(
                        track(1, "2"),
                        track(1, "3"))),
                pop());
    }

    @Test
    public void fairPushTest4() {
        push(1, "1");
        push(2, "1");
        push(3, "1");
        push(1, "2");
        push(2, "2");
        push(3, "2");
        pop();
        pop();
        push(1, "3");
        push(2, "3");
        assertEquals(
                trackList(track(3, "1"), asList(
                        track(1, "2"),
                        track(2, "2"),
                        track(3, "2"),
                        track(1, "3"),
                        track(2, "3"))),
                pop());
    }

    private void push(int userId, String trackName) {
        jukeBox.addTrack(track(userId, trackName));
    }

    private TrackList pop() {
        return jukeBox.popTrackListWithNowPlaying();
    }

    private TrackEntity track(int userId, String trackName) {
        return TrackEntity.builder()
                .userId(userId)
                .trackName(trackName)
                .build();
    }

    private TrackList trackList(TrackEntity nowPlaying, List<TrackEntity> tracks) {
        return TrackList.builder()
                .nowPlaying(nowPlaying)
                .tracks(tracks)
                .build();
    }
}
