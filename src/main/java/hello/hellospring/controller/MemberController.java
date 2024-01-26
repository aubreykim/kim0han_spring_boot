package hello.hellospring.controller;

import hello.hellospring.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("member")
public class MemberController {
    private final MemberService memberService;


    //1. 생성자 주입 제일 권장
    @Autowired
    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping("login")
    public String login(Model model) throws Exception {
        long startTime = System.currentTimeMillis();

        long responseTime = System.currentTimeMillis() - startTime;
        System.out.println(responseTime + "milliseconds" + ", " + responseTime*0.001 + "seconds");
        return "monitoring";
    }
    @GetMapping("update")
    public String monitoring(Model model) throws Exception {
        long startTime = System.currentTimeMillis();

        long responseTime = System.currentTimeMillis() - startTime;
        System.out.println(responseTime + "milliseconds" + ", " + responseTime*0.001 + "seconds");
        return "monitoring";
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
