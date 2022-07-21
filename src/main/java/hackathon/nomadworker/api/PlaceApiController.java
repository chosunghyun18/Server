package hackathon.nomadworker.api;

import hackathon.nomadworker.domain.Place;
import hackathon.nomadworker.dto.PlaceDtos.*;
import hackathon.nomadworker.service.PlaceService;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.stream.Collectors;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class PlaceApiController {

    private final PlaceService placeService;

    @GetMapping(value="/api/place/location" , produces = "application/json;charset=UTF-8")
    public PlaceResultResponse placeByCatagoryGet(@RequestHeader("Authorization") String u_uid,
                                                  @RequestParam("locationCategory") String p_cate) {
        List<Place> places = placeService.findPlacesByCategory(p_cate);
        if (places.isEmpty())
        {
            return new PlaceResultResponse("지역별 근무 장소 조회 실패", 400,null);
        } else {
            List<PlaceDtoCat> collect = places.stream().map(place -> new PlaceDtoCat(place)).collect(Collectors.toList());
            PlaceByCategoryResponse placeByCategoryResponse = new PlaceByCategoryResponse(p_cate, collect);
            return new PlaceResultResponse("지역별 근무 장소 조회 성공", 200, placeByCategoryResponse);
        }

    }

}
