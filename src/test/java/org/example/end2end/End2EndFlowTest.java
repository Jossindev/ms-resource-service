//package org.example.end2end;
//
//import static java.util.Optional.ofNullable;
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.testcontainers.shaded.org.awaitility.Awaitility.await;
//
//import java.io.IOException;
//import java.io.InputStream;
//import java.time.Duration;
//
//import org.apache.commons.compress.utils.IOUtils;
//import org.example.dto.SongDTO;
//import org.example.model.Resource;
//import org.example.repository.ResourceRepository;
//import org.junit.Ignore;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.web.client.TestRestTemplate;
//import org.springframework.http.HttpEntity;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.MediaType;
//import org.springframework.http.ResponseEntity;
//import org.springframework.test.context.ActiveProfiles;
//import org.springframework.test.context.junit.jupiter.SpringExtension;
//
//@ExtendWith(SpringExtension.class)
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
//@ActiveProfiles("test")
//public class End2EndFlowTest {
//
//    @Autowired
//    private TestRestTemplate restTemplate;
//    @Autowired
//    private ResourceRepository resourceRepository;
//
//    @Test
//    public void testMicroservicesIntegration() throws IOException {
//        int testResourceId = 1;
//        String testTitle = "The Beginning Of All Things";
//        String testArtist = "In Flames";
//        String testAlbum = "Foregone";
//        String testDuration = "2533.878662109375";
//        int testYear = 2023;
//        SongDTO expectedResult = SongDTO.builder()
//            .album(testAlbum)
//            .name(testTitle)
//            .artist(testArtist)
//            .length(testDuration)
//            .resourceId(testResourceId)
//            .year(testYear)
//            .build();
//
//        InputStream data = getClass().getClassLoader()
//            .getResourceAsStream("data/test.mp3");
//        byte[] mp3Data = IOUtils.toByteArray(ofNullable(data)
//            .orElse(InputStream.nullInputStream()));
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.valueOf("audio/mpeg"));
//        HttpEntity<byte[]> request = new HttpEntity<>(mp3Data, headers);
//
//        // check if resource exist before execution the flow = false
//        Resource resource = resourceRepository.findById(testResourceId).orElse(null);
//        assertThat(resource).isEqualTo(null);
//
//        ResponseEntity<String> postResponse = restTemplate.postForEntity(
//            "/resources", request, String.class);
//        assertThat(postResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
//        assertThat(postResponse.getBody()).isEqualTo("{\"id\":1}");
//
//        await().atMost(Duration.ofSeconds(30)).untilAsserted(() -> {
//            ResponseEntity<byte[]> response = restTemplate.getForEntity("/resources/1", byte[].class);
//            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
//        });
//
//        await().atMost(Duration.ofSeconds(30)).untilAsserted(() -> {
//            ResponseEntity<SongDTO> response = restTemplate.getForEntity("http://localhost:8082/songs/1", SongDTO.class);
//            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
//            assertThat(response.getBody()).isEqualTo(expectedResult);
//        });
//
//        // check if resource exist after execution the flow = true
//        Resource actualResource = resourceRepository.findById(testResourceId).orElse(Resource.builder().build());
//        assertThat(actualResource.getS3Key().contains(".mp3")).isEqualTo(true);
//    }
//}
