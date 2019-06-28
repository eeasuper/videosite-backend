package com.nano.videosite.controllers;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.internal.verification.VerificationModeFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.FileSystemUtils;

import com.nano.videosite.models.Playlist;
import com.nano.videosite.models.User;
import com.nano.videosite.models.Video;
import com.nano.videosite.repositories.PlaylistRepository;
import com.nano.videosite.repositories.UserRepository;
import com.nano.videosite.repositories.VideoRepository;
import com.nano.videosite.services.JWTAuthenticationService;
import com.nano.videosite.services.PlaylistService;
import com.nano.videosite.services.utilities.JsonUtil;
import com.nano.videosite.services.utilities.PlaylistInstanceInit;
import com.nano.videosite.storage.FileSystemStorageService;

import junit.framework.TestCase;

import static org.hamcrest.Matchers.hasSize;
@RunWith(SpringRunner.class)
@WebMvcTest(PlaylistController.class)
public class PlaylistControllerIntegrationTest extends TestCase{
	@AfterClass
    public static void setDown() {
		/*upload-dir--1 means the upload-directory of the user whose id is -1.*/
		Path uploadDir = Paths.get("upload-dir/upload-dir--1");
		FileSystemUtils.deleteRecursively(uploadDir.toFile());
        Path thumbnailDir = Paths.get("thumbnail-dir/thumbnail-dir--1");
        FileSystemUtils.deleteRecursively(thumbnailDir.toFile());
    }  
	@TestConfiguration
	static class PlaylistControllerTestContextConfiguration{

		@Bean
		public JWTAuthenticationService jwtService() {
			return new JWTAuthenticationService();
		}
		@Bean
		public BCryptPasswordEncoder passwordEncoder() {
			return new BCryptPasswordEncoder();
		}
		@Bean
		public PlaylistInstanceInit playlistInit() throws IOException {
			PlaylistInstanceInit init =  new PlaylistInstanceInit();
			PlaylistControllerIntegrationTest.playlist = init.playlist;
			return init;
		}
	}
	@Autowired
	private MockMvc mvc;
	@MockBean
	PlaylistRepository playlistRepository;
	@MockBean
	VideoRepository videoRepository;
	@MockBean
	UserRepository userRepository;
	@MockBean
	PlaylistService playlistService;
	@Autowired
	BCryptPasswordEncoder passwordEncoder;
	@MockBean
	FileSystemStorageService uploadService;
	private static Playlist playlist = new Playlist();
	
	private final User user = PlaylistInstanceInit.user;
	private final Long userId = user.getId();
	private final String userUsername = PlaylistInstanceInit.user.getUsername();
	
	@Before
	public void setUp() {
		
		Mockito.when(userRepository.existsByUsername(user.getUsername())).thenReturn(true);
	}
	@Test
	public void whenAllPlaylists_thenReturnSet() throws Exception {
		Set<Playlist> set = new HashSet<Playlist>();
		set.add(playlist);
		given(playlistService.allPlaylists(Mockito.eq(userId))).willReturn(set);
		
		mvc.perform(get("/user/"+userId+"/playlist").header(HttpHeaders.AUTHORIZATION,"Bearer "+JWTAuthenticationService.createToken(userUsername, userId))).andExpect(status().isOk()).andExpect(jsonPath("$.[0].id",is(1)));
		verify(playlistService,VerificationModeFactory.times(1)).allPlaylists(Mockito.eq(userId));
		reset(playlistService);
	}
	
	@Test
	public void whenAdd_thenReturnPlaylist() throws Exception {
		Playlist playlistToAdd = playlist;
		playlistToAdd.setId(5L);
		given(playlistService.add(Mockito.any(Playlist.class))).willReturn(playlistToAdd);
		
		mvc.perform(post("/playlist")
				.header(HttpHeaders.AUTHORIZATION,"Bearer "+JWTAuthenticationService.createToken(userUsername, userId))
				.contentType(MediaType.APPLICATION_JSON)
				.content(JsonUtil.toJson(playlistToAdd))
				.characterEncoding("utf-8"))
				.andExpect(status().isCreated()).andExpect(jsonPath("$.id",is(5)));
		
		verify(playlistService,VerificationModeFactory.times(1)).add(Mockito.any(Playlist.class));
		reset(playlistService);
		playlist.setId(1L);
	}
	
	@Test
	public void whenOnePlaylistList_thenReturnMap() throws Exception {
		given(playlistService.onePlaylistList(Mockito.eq(playlist.getId()))).willReturn(playlist.getPlaylist());
		
		mvc.perform(get("/playlist/list/"+playlist.getId()).header(HttpHeaders.AUTHORIZATION,"Bearer "+JWTAuthenticationService.createToken(userUsername, userId))
				).andExpect(status().isOk()).andExpect(jsonPath("$.*",hasSize(2)));
		
		verify(playlistService,VerificationModeFactory.times(1)).onePlaylistList(Mockito.eq(playlist.getId()));
		reset(playlistService);
	}
	
	@Test
	public void whenOnePlaylist_thenReturnPlaylist() throws Exception {
		given(playlistService.onePlaylist(Mockito.eq(playlist.getId()))).willReturn(playlist);
		
		mvc.perform(get("/playlist/"+playlist.getId()).header(HttpHeaders.AUTHORIZATION,"Bearer "+JWTAuthenticationService.createToken(userUsername, userId))
				).andExpect(status().isOk()).andExpect(jsonPath("$.id",is(1)));
		
		verify(playlistService,VerificationModeFactory.times(1)).onePlaylist(Mockito.eq(playlist.getId()));
		reset(playlistService);
		
	}
	
	@Test
	public void whenOnePlaylistThumbnail_thenReturnByte() throws Exception {
		given(playlistService.onePlaylistThumbnail(Mockito.eq(playlist.getId()))).willReturn(Mockito.any(byte[].class));
		mvc.perform(get("/playlist/"+playlist.getId()+"/thumbnail").header(HttpHeaders.AUTHORIZATION,"Bearer "+JWTAuthenticationService.createToken(userUsername, userId))
				).andExpect(status().isOk()).andExpect(content().contentType(MediaType.IMAGE_JPEG));
		
		verify(playlistService,VerificationModeFactory.times(1)).onePlaylistThumbnail(Mockito.eq(playlist.getId()));
		reset(playlistService);
	}
	
	@Test
	public void whenEditOrder_thenReturnMap() throws Exception {
		Map<Integer,Video> configuredMap = playlist.getPlaylist();
		Video first = configuredMap.get(1);
		Video second = configuredMap.get(2);
		configuredMap.replace(1, second);
		configuredMap.replace(2, first);
		given(playlistService.editOrder(Mockito.eq(playlist.getId()),Mockito.any())).willReturn(configuredMap);
		
		mvc.perform(put("/playlist/"+playlist.getId()+"/edit/order-change")
				.header(HttpHeaders.AUTHORIZATION,"Bearer "+JWTAuthenticationService.createToken(userUsername, userId))
				.contentType(MediaType.APPLICATION_JSON)
				.content(JsonUtil.toJson(configuredMap))
				.characterEncoding("utf-8")
				)
			.andExpect(status().isOk()).andExpect(jsonPath("$.*",hasSize(2)));
		
		verify(playlistService,VerificationModeFactory.times(1)).editOrder(Mockito.eq(playlist.getId()),Mockito.any());
		reset(playlistService);
	}
	
	@Test
	public void whenEditTitle_thenReturnPlaylist() throws IOException, Exception {
		Playlist configuredPlaylist = playlist;
		configuredPlaylist.setTitle("testTitle");
		given(playlistService.editTitle(Mockito.eq(playlist.getId()), Mockito.any(Playlist.class))).willReturn(configuredPlaylist);
		
		mvc.perform(put("/playlist/"+playlist.getId()+"/edit/title-change")
				.header(HttpHeaders.AUTHORIZATION,"Bearer "+JWTAuthenticationService.createToken(userUsername, userId))
				.contentType(MediaType.APPLICATION_JSON)
				.content(JsonUtil.toJson(configuredPlaylist))
				.characterEncoding("utf-8")
				)
			.andExpect(status().isOk()).andExpect(jsonPath("$.title",is("testTitle")));
		
		verify(playlistService,VerificationModeFactory.times(1)).editTitle(Mockito.eq(playlist.getId()),Mockito.any(Playlist.class));
		reset(playlistService);
	}
	
	@Test
	public void whenEditAddVideo_thenReturnPlaylist() throws IOException, Exception {
		Long videoId = 5L;
		List<Video> newVideoList = new ArrayList<Video>();
		Video newVideo = PlaylistInstanceInit.v1;
		newVideo.setId(videoId);
		newVideoList.add(newVideo);
		playlist.getPlaylist().put(3, newVideo);
		given(playlistService.editAddVideo(Mockito.eq(playlist.getId()),Mockito.any())).willReturn(playlist);
		
		mvc.perform(post("/playlist/"+playlist.getId()+"/edit/add-video")
				.header(HttpHeaders.AUTHORIZATION,"Bearer "+JWTAuthenticationService.createToken(userUsername, userId))
				.contentType(MediaType.APPLICATION_JSON)
				.content(JsonUtil.toJson(newVideoList))
				.characterEncoding("utf-8")
				)
			.andExpect(status().isCreated()).andExpect(jsonPath("$.id",is(1)));
		
		verify(playlistService,VerificationModeFactory.times(1)).editAddVideo(Mockito.eq(playlist.getId()),Mockito.any());
		reset(playlistService);
	}
	
	@Test
	public void whenDeletePlaylist_thenReturnNoContent() throws Exception {
		given(playlistService.deletePlaylist(playlist.getId(), userId)).willReturn(true);
		
		mvc.perform(delete("/playlist/"+playlist.getId()+"/"+userId)
				.header(HttpHeaders.AUTHORIZATION,"Bearer "+JWTAuthenticationService.createToken(userUsername, userId))
				)
			.andExpect(status().isNoContent());
		
		verify(playlistService,VerificationModeFactory.times(1)).deletePlaylist(Mockito.eq(playlist.getId()),Mockito.eq(userId));
		reset(playlistService);
	}
}
