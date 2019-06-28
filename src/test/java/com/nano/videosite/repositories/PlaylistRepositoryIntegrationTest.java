package com.nano.videosite.repositories;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.util.List;
import java.util.Properties;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import com.nano.videosite.exceptions.ElementNotFoundException;
import com.nano.videosite.models.Playlist;
import com.nano.videosite.models.User;
import com.nano.videosite.services.PlaylistService;
import com.nano.videosite.services.utilities.PlaylistInstanceInit;
import com.nano.videosite.storage.FileSystemStorageService;
import com.nano.videosite.thumbnails.VideoThumbnails;

import junit.framework.TestCase;

//@RunWith(SpringRunner.class)
//@DataJpaTest
public class PlaylistRepositoryIntegrationTest extends TestCase{
//	@Autowired
//	private TestEntityManager entityManager;
//	@Autowired
//	PlaylistRepository playlistRepository;
//	
//	@TestConfiguration
//	static class PlaylistRepositoryTestContextConfiguration{
//		@Bean
//		public VideoThumbnails videoThumbnails() {
//			return new VideoThumbnails();
//		}
//		@Bean
//		public FileSystemStorageService fileSystemStorageService() {
//			return new FileSystemStorageService();
//		}
//		@Bean
//		public PlaylistService playlistService() {
//			return new PlaylistService();
//		}
//		@Bean
//		public BCryptPasswordEncoder passwordEncoder() {
//			return new BCryptPasswordEncoder();
//		}
//		@Bean
//		public PlaylistInstanceInit playlistInit() throws IOException {
//			PlaylistInstanceInit init =  new PlaylistInstanceInit();
//			PlaylistRepositoryIntegrationTest.playlist = PlaylistInstanceInit.playlist;
//			return init;
//		}
//	}
//	private static Playlist playlist = new Playlist();
//	
//	private final User user = PlaylistInstanceInit.user;
//
//	@Before
//	public void setUp() {
//		System.out.println("test: "+playlist.getId());
//		entityManager.persist(PlaylistInstanceInit.v1);
//		entityManager.persist(PlaylistInstanceInit.v2);
//		entityManager.persist(user);
//		entityManager.persist(playlist);
//		
//		entityManager.flush();
//	}
//	
//	@Test
//	public void whenFindByIdAndUserId_thenReturnPlaylist() {
//
//		Playlist foundPlaylist = playlistRepository.findByIdAndUserId(playlist.getId(), user.getId());
//		
//		assertThat(foundPlaylist.getId()).isEqualTo(playlist.getId());
//	}
//	
//	@Test
//	public void whenFindByUserId_thenReturnListOfPlaylist() {
//		Playlist differentPlaylist = playlist;
//		differentPlaylist.setId(5L);
//		entityManager.persist(differentPlaylist);
//		entityManager.flush();
//		
//		List<Playlist> listPlaylist = playlistRepository.findByUserId(user.getId()).orElseThrow(()->new ElementNotFoundException());
//		
//		assertThat(listPlaylist.size()).isEqualTo(2);
//	}
//	
}
