package hackathon.nomadworker.api;

import hackathon.nomadworker.domain.User;
;
import hackathon.nomadworker.dto.ImageDtos.*;
import hackathon.nomadworker.service.FileUploadService;
import hackathon.nomadworker.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@CrossOrigin
@RequiredArgsConstructor
@RestController
public class ImageUploadController {

    private final FileUploadService fileUploadService;
    private final UserService userService;

    @PutMapping(value = "/api/user/profile", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public PutResponse uploadImage(@RequestHeader("Authorization") String u_uid, @RequestPart(value = "file", required = false) MultipartFile file)
    {
        String imageUrl =  fileUploadService.uploadImage(file);
        User result = userService.userImagePost(u_uid, imageUrl);
        if(result!= null) {
            return new PutResponse("이미지 갱신 성공", 200);
        }else
        {
            return new PutResponse("이미지 갱신 실패", 400);
        }
    }

}
