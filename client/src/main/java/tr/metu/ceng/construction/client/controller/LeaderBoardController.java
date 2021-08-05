package tr.metu.ceng.construction.client.controller;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import tr.metu.ceng.construction.client.DTO.ScoreDTO;
import tr.metu.ceng.construction.client.constant.RequestConstants;

import java.util.List;

/**
 * Responsible for communication with backend in displaying leaderboard feature
 */
public class LeaderBoardController {

    private final RestTemplate restTemplate = new RestTemplate();

    /**
     * This method provides an API GET request to retrieve score list by given parameter.
     *
     * @param specificUri this is the related uri portion of API GET request,
     *                    and can be one of the following: weekly, monthly, or allTimes.
     * @return scoreDTO list if retrieval operation successful.
     * if not prints stack trace by catching RestClientException
     */
    public List<ScoreDTO> getScoresBySpecificUri(String specificUri) {
        ParameterizedTypeReference<List<ScoreDTO>> ptr = new ParameterizedTypeReference<>() {
        };
        List<ScoreDTO> scoreDTOList = null;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        MultiValueMap<String, String> map= new LinkedMultiValueMap<>();
        map.add("id", "1");

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);

        ResponseEntity<List<ScoreDTO>> response;
        try {
            response = restTemplate.exchange(RequestConstants.baseURL + "score/" + specificUri,
                    HttpMethod.GET,
                    request,
                    ptr);
            scoreDTOList = response.getBody();
        } catch (RestClientException e) {
            System.out.println("leaderboard get request exception:" + e.getLocalizedMessage());
            e.printStackTrace();
        }

        if (scoreDTOList != null) {
            List<ScoreDTO> finalScoreDTOList = scoreDTOList;
            scoreDTOList.forEach(scoreDTO -> scoreDTO.setNumber(finalScoreDTOList.indexOf(scoreDTO) + 1));
        }
        return scoreDTOList;
    }
}
