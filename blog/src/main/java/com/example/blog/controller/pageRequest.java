package com.example.blog.controller;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.example.blog.entitiy.Kuser;
import com.example.blog.entitiy.comment;
import com.example.blog.entitiy.form;
import com.example.blog.repository.UserRepository;
import com.example.blog.repository.commentRepository;
import com.example.blog.repository.formRepository;



@Controller
public class pageRequest {
    @Autowired
    private formRepository formrepository;
    @Autowired
    private commentRepository commentRepository;
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/")
    public String home(Model model){
        
        List<form> forms = formrepository.findAllByOrderBySeqDesc();
        model.addAttribute("form", forms);
        System.out.println(forms);
        System.out.println("첫번째");
        List<comment> comments = commentRepository.findAll();
        System.out.println(comments);
        model.addAttribute("commentlist", comments);
        System.out.println("두번째");
        
        return "html/index";
    }
    @PostMapping("/")
    public String homePost(@RequestParam("userid") String userid,
                            @RequestParam("seq") Long seq,
                            @RequestParam("commenttext") String comment,
                            @RequestParam(required = false) Long commentnumber){
        comment comments = new comment();
        comments.setUserId(userid);
        comments.setSeq(seq);
        comments.setCommentText(comment);
        commentRepository.save(comments);

        return "redirect:/";
    }
    
    @GetMapping("/contentPlus")
    public String contentPlus(Model model, @RequestParam(required = false) Long seq){
        if(seq == null){
            model.addAttribute("form", new form());
        } else{
            form form = formrepository.findBySeq(seq);
            model.addAttribute("form", form);
            System.out.println(form);
        }
        return "html/contentPlus";
    }

    @PostMapping("/contentPlus")
    public String formSubmit(@RequestParam("userid") String userid,
                            @RequestParam("content") String content,
                            @RequestParam("file") MultipartFile mFile,
                            @RequestParam("userimg") String userimg,
                            @RequestParam(required = false) Long seq, Model model){
        form form = new form();
        String saveOFolder = "C:/Back_end/blog/blog/src/main/resources/static/contentImages/";
        String uid = UUID.randomUUID().toString();
        String oName = mFile.getOriginalFilename();
        File saveFile = new File(saveOFolder + uid);
        System.out.println(mFile);
        System.out.println("이게뭐람");
        form.setSeq(seq);
        form.setUserId(userid);
        form.setContent(content);
        form.setOriginalFileName(oName);
        form.setUuid(uid);
        form.setUserImg(userimg);
        try {
            mFile.transferTo(saveFile);
            formrepository.save(form);
        } catch (IllegalStateException | IOException e) {
            e.printStackTrace();
        }
        return "html/contentPlusSuccess";
    }

    @GetMapping("/delete")
    public String contentDelete(@RequestParam(required = false) Long seq) {
        System.out.println(seq);
        System.out.println("세번째");
        form form = new form();
        form.setSeq(seq);
        System.out.println(form);
        System.out.println("네번째");
        formrepository.delete(form);
        return "html/contentDelete";
    }

    @GetMapping("/deleteComment")
    public String commentDelete(@RequestParam(required = false) Long commentnumber){
        comment mycomment = new comment();
        mycomment.setCommentNumber(commentnumber);
        commentRepository.delete(mycomment);
        return "html/commentDelete";
    }

    @GetMapping("/userDelete")
    public String userDelete(@RequestParam("userId") String userId){
        Kuser kuser = new Kuser();
        kuser.setUserId(userId);
        System.out.println(kuser);
        userRepository.delete(kuser);
        return "html/userDeleteAlert";
    }
}
