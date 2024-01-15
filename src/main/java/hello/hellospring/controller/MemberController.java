package hello.hellospring.controller;

import hello.hellospring.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class MemberController {
    private final MemberService memberService;


    //1. 생성자 주입 제일 권장
    @Autowired
    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }


 /*
    2. 필드 주입 - 추후 변경 불가
    @Autowired  private final MemberService memberService;

    3. setter 주입 - setter injection
    @Autowired
    public void setMemberService (MemberService memberService) {
        this.memberService = memberService;
    }
 * */


}
