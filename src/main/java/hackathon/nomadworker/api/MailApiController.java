package hackathon.nomadworker.api;
import hackathon.nomadworker.dto.MailDtos.*;
import hackathon.nomadworker.service.MailService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class MailApiController
{
    private final MailService mailService;
    @PostMapping(value="/api/mail" , produces = "application/json;charset=UTF-8")
    public MailPostResponse mailPost(@Param("userEmail") String userEmail)
    {

        if(! mailService.findOneByEmail(userEmail))
        {
            MailPostResponse mailPostResponse = new MailPostResponse("400", "이미존재하는 이메일입니다","0");
            return mailPostResponse;
        }else {
            String randomcode = mailService.mailSend(userEmail);

            MailPostResponse mailPostResponse = new MailPostResponse("200","가입 가능한 이메일 입니다",randomcode);
            return mailPostResponse;
        }

    }


}
