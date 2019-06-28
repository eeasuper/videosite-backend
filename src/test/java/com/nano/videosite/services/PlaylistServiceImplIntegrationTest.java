package com.nano.videosite.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.entry;
import static org.mockito.BDDMockito.given;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.assertj.core.util.Arrays;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.internal.verification.VerificationModeFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.FileSystemUtils;

import com.nano.videosite.configurations.MyUserPrincipal;
import com.nano.videosite.models.Playlist;
import com.nano.videosite.models.User;
import com.nano.videosite.models.Video;
import com.nano.videosite.repositories.PlaylistRepository;
import com.nano.videosite.repositories.UserRepository;
import com.nano.videosite.repositories.VideoRepository;
import com.nano.videosite.services.utilities.PlaylistInstanceInit;

import junit.framework.TestCase;

@RunWith(SpringRunner.class)
public class PlaylistServiceImplIntegrationTest extends TestCase{
	@AfterClass
    public static void setDown() {
		/*upload-dir--1 means the upload-directory of the user whose id is -1.*/
		Path uploadDir = Paths.get("upload-dir/upload-dir--1");
		FileSystemUtils.deleteRecursively(uploadDir.toFile());
        Path thumbnailDir = Paths.get("thumbnail-dir/thumbnail-dir--1");
        FileSystemUtils.deleteRecursively(thumbnailDir.toFile());
    }  
	@TestConfiguration
	static class PlaylistServiceImplTestContextConfiguration{
		
		@Bean
		public PlaylistService playlistService() {
			return new PlaylistService();
		}
		@Bean
		public BCryptPasswordEncoder passwordEncoder() {
			return new BCryptPasswordEncoder();
		}
		@Bean
		public PlaylistInstanceInit playlistInit() throws IOException {
			PlaylistInstanceInit init =  new PlaylistInstanceInit();
			PlaylistServiceImplIntegrationTest.playlist = init.playlist;
			return init;
		}
	}
	
	@MockBean
	PlaylistRepository playlistRepository;
	@MockBean
	VideoRepository videoRepository;
	@MockBean
	UserRepository userRepository;
	@Autowired
	PlaylistService playlistService;
	@Autowired
	BCryptPasswordEncoder passwordEncoder;
	
	private static Playlist playlist = new Playlist();
	
	private final User user = PlaylistInstanceInit.user;
	
	private final Long playlistId = 1L;
	private final Long userId = user.getId();
	
	@Before
	public void setUp() throws IOException {		
		System.out.println("test:"+playlist.getUserId());
		Authentication authentication = Mockito.mock(Authentication.class);
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		Mockito.when(authentication.getPrincipal()).thenReturn(new MyUserPrincipal(user));
		
		Mockito.when(playlistRepository.findById(playlistId)).thenReturn(Optional.of(playlist));
		given(playlistRepository.save(playlist)).willReturn(playlist);
	}
	
	@Test
	public void whenAdd_thenNewPlaylistShouldBeFound() throws IOException {
		Mockito.when(userRepository.findById(userId)).thenReturn(Optional.of(user));
		
		Playlist newPlaylist = playlistService.add(playlist);
		
		Mockito.verify(playlistRepository,VerificationModeFactory.times(1)).save(newPlaylist);
		Mockito.reset(playlistRepository);
		assertThat(playlist.getTitle()).isEqualTo(newPlaylist.getTitle());
	}
	
	@Test
	public void whenAllPlaylists_thenSetShouldBeReturned() throws IOException {
		List<Playlist> listOfPlaylists = new ArrayList<Playlist>();
		listOfPlaylists.add(playlist);
		listOfPlaylists.add(playlist);
		Mockito.when(playlistRepository.findByUserId(userId)).thenReturn(Optional.of(listOfPlaylists));
//		Mockito.when(userRepository.findById(userId)).thenReturn(Optional.of(user));
		
		Set<Playlist> playlists = playlistService.allPlaylists(userId);
		
//		Mockito.verify(userRepository,VerificationModeFactory.times(2)).findById(userId);
//		Mockito.reset(userRepository);
//		String username = user.getUsername();
//		assertThat(playlists).allSatisfy((p)->{
//			assertThat(p.getUsername()).isEqualTo(username);
//		});
		assertThat(playlists).allSatisfy((p)->{
			assertThat(p.getUserId()).isEqualTo(userId);
		});
	}
	
	@Test
	public void whenOnePlaylist_thenPlaylistShouldBeReturned() {
//		Mockito.when(userRepository.findById(userId)).thenReturn(Optional.of(user));
		Playlist foundPlaylist = playlistService.onePlaylist(playlistId);
//		System.out.println(foundPlaylist.getPlaylist())
		assertThat(foundPlaylist).isEqualTo(playlist);
	}
	
	@Test
	public void whenOnePlaylistList_thenMapShouldBeReturned() {
		Map<Integer,Video> playlistList = playlist.getPlaylist();
		
		Map<Integer,Video> foundMap = playlistService.onePlaylistList(1L);
		
		assertThat(foundMap).isEqualTo(playlistList);
	}
	
	@Test
	public void whenOnePlaylistThumbnail_thenByteShouldBeReturned() throws IOException {
		byte[] image = playlistService.onePlaylistThumbnail(1L);
		
		assertThat(image).isInstanceOf(byte[].class);
	}
	
	@Test
	public void whenEditOrder_thenMapShouldBeReturned() {
		Map<Integer,Video> configuredMap = playlist.getPlaylist();
		Video first = configuredMap.get(1);
		Video second = configuredMap.get(2);
		configuredMap.replace(1, second);
		configuredMap.replace(2, first);
		
		Map<Integer,Video> orderEditedMap = playlistService.editOrder(1L, configuredMap);
		
		assertThat(orderEditedMap).isEqualTo(configuredMap);
	}
	
	@Test
	public void whenEditTitle_thenReturnPlaylist() {
		Playlist configuredPlaylist = playlist;
		String newTitle= "changedTitle";
		configuredPlaylist.setTitle(newTitle);
		
		Playlist editedPlaylist = playlistService.editTitle(1L, configuredPlaylist);
		
		assertThat(editedPlaylist.getTitle()).isEqualTo(newTitle);
	}
	
	@Test
	public void whenEditAddVideo_thenReturnPlaylist() {
		Long videoId = 5L;
		List<Video> newVideoList = new ArrayList<Video>();
		Video newVideo = PlaylistInstanceInit.v1;
		newVideo.setId(videoId);
		newVideoList.add(newVideo);
		
		Mockito.when(videoRepository.findById(videoId)).thenReturn(Optional.of(newVideo));
		
		Playlist playlistWithMoreVideos = playlistService.editAddVideo(1L, newVideoList);
		
		/*Two videos existed in the playlist before the newVideo was added so entry(3,newVideo) is applicable*/
		assertThat(playlistWithMoreVideos.getPlaylist()).containsAnyOf(entry(3,newVideo));
	}
	
	@Test
	public void whenDeletePlaylist_thenVerifyDeletion() {
		
		boolean deleted = playlistService.deletePlaylist(playlistId, userId);
		
		assertThat(deleted).isTrue();
	}
}
