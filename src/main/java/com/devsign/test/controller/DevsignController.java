package com.devsign.test.controller;

import com.devsign.test.dto.DevsignDTO;
import com.devsign.test.service.DevsignService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class DevsignController {
    private final DevsignService devsignService;

    @GetMapping("/devsign/save")
    public String saveForm() {
        return "save";
    }

    @PostMapping("/devsign/save")
    public String save(@ModelAttribute DevsignDTO devsignDTO) {
        devsignService.save(devsignDTO);
        return "login";
    }

    @GetMapping("/devsign/login")
    public String loginForm() {
        return "login";
    }

    @PostMapping("/devsign/login")
    public String login(@ModelAttribute DevsignDTO devsignDTO, HttpSession session) {
        DevsignDTO loginResult = devsignService.login(devsignDTO);
        if (loginResult != null) {
            // login 성공
            session.setAttribute("loginUserId", loginResult.getUserId());
            return "main";
        } else {
            // login 실패
            return "login";
        }
    }

    @GetMapping("/devsign/")
    public String findAll(Model model) {
        List<DevsignDTO> devsignDTOList = devsignService.findAll();
        model.addAttribute("devsignList", devsignDTOList);
        return "list";
    }

    @GetMapping("/devsign/{id}")
    public String findById(@PathVariable Long id, Model model) {
        DevsignDTO devsignDTO = devsignService.findById(id);
        model.addAttribute("devsign", devsignDTO);
        return "detail";
    }

    @GetMapping("/devsign/update")
    public String updateForm(HttpSession session, Model model) {
        String myUserId = (String) session.getAttribute("loginUserId");
        DevsignDTO devsignDTO = devsignService.updateForm(myUserId);
        model.addAttribute("updateDevsign", devsignDTO);
        return "update";
    }

    @PostMapping("/devsign/update")
    public String update(@ModelAttribute DevsignDTO devsignDTO, HttpSession session) {
        String myUserId = (String) session.getAttribute("loginUserId");
        System.out.println(myUserId);
        devsignService.update(devsignDTO, myUserId);
        return "redirect:/devsign/" + devsignDTO.getId();
    }

    @GetMapping("/devsign/delete/{id}")
    public String delete(@PathVariable Long id) {
        devsignService.delete(id);
        return "redirect:/devsign/";
    }

    @GetMapping("/devsign/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "index";
    }

    @PostMapping("/devsign/userId-check")
    public @ResponseBody String emailCheck(@RequestParam("userId") String userId){
        System.out.println("ID = " + userId);
        String checkResult = devsignService.userIdCheck(userId);
        if(checkResult != null){
            return "ok";
        } else {
            return "no";
        }
    }
}
