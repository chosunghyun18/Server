package hackathon.nomadworker.api;

import static hackathon.nomadworker.dto.UserDtos.*;

import hackathon.nomadworker.domain.User;
import hackathon.nomadworker.service.AuthService;
import hackathon.nomadworker.service.UserService;

import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.*;


import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
public class UserApiController
{

    private final UserService userService;
    private final AuthService authService;


    @PostMapping(value="/api/user" , produces = "application/json;charset=UTF-8")
    public UserResultResponse userPost(@Valid @RequestBody UserPostRequest request) throws Exception {

        //토큰 발행
        String token= authService.createToken(request.getU_nickname());
        userService.userPost(token, request.getU_email(),request.getU_password(),request.getU_nickname());

        String message = "회원 등록이 완료되었습니다.";
        UserPostResponse data = new UserPostResponse(request.getU_nickname(), token,(float)38.11,(float)128.111);
        UserResultResponse userResultResponse = new UserResultResponse(message, 200, data);

        return userResultResponse;
    }

    @GetMapping(value="/api/users", produces = "application/json;charset=UTF-8")
    public UserResult userAll()
    {
        List<User> findUsers = userService.findUsers();
        List<UserDto> collect = findUsers.stream().map(user -> new UserDto(user)).collect(Collectors.toList());
        return new UserResult(collect.size(), collect);
    }

    @GetMapping(value = "/api/user/nicknamecheck", produces = "application/json;charset=UTF-8")
    public NicknameSearchGetResponse NicknameSearch(@Param("userNickname") String userNickname)
    {
        List<User> users = userService.findOneByNickName(userNickname);
        List<UserDto> collect = users.stream().map(f -> new UserDto(f))
                .collect(Collectors.toList());

        if(!collect.isEmpty()) {
            NicknameSearchGetResponse result = new NicknameSearchGetResponse("중복된 닉네임 입니다",400);
            return result;
        }else { // 중복이 없으면 false
            NicknameSearchGetResponse result = new NicknameSearchGetResponse("사용가능한 닉네임 입니다",200);
            return result;
        }
    }

    @PutMapping(value = "api/user/geographic", produces = "application/json;charset=UTF-8")
    public UserCoordinatePutResponse usercoordinateupdate(@RequestHeader("Authorization") String u_uid,
                                                    @Valid @RequestBody UserCoordinatePutRequest request)
    {
        userService.updateCoordinate(u_uid,request.getLatitude(),request.getLongitude());

        return new UserCoordinatePutResponse("갱신 성공",200);
    }


}
