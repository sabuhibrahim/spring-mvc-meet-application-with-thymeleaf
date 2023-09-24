package com.meet.meet.controllers;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.meet.meet.dtos.GroupDto;
import com.meet.meet.services.GroupService;
import com.meet.meet.services.UserService;

import jakarta.validation.Valid;

@Controller
public class GroupController extends AbstractController {

    @Autowired
    private GroupService groupService;

    @Autowired
    private UserService userService;
    
    @GetMapping("/")
    public String index() {
        return "redirect:/groups";
    }

    @GetMapping("/groups")
    public String groupList(
        @RequestParam(value = "q", required = false) String q,
        @RequestParam(value = "pageNo", required = false) Integer pageNo,
        @RequestParam(value = "pageSize", required = false) Integer pageSize,
        Model model
    ){
        Page<GroupDto> groups;
        
        if(pageNo == null) pageNo = 1;
        if(pageSize == null) pageSize = 10;
        
        pageNo--;
        
        if(q == null) {
            groups = groupService.findAll(pageNo, pageSize);
        } else {
            groups = groupService.search(q, pageNo, pageSize);
        }

        int totalPages = groups.getTotalPages();
        if(totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                .boxed()
                .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
            model.addAttribute("nextPageNumber",  pageNo + 2);
            model.addAttribute("prevPageNumber", pageNo);
        }
        model.addAttribute("user", getCurrentUser(userService));
        model.addAttribute("groups", groups);
        
        return "group/list";
    }

    @GetMapping("/groups/new")
    public String newGroup(Model model) {
        GroupDto group = new GroupDto();
        
        model.addAttribute("user", getCurrentUser(userService));
        model.addAttribute("group", group);
        return "group/create";
    }

    @PostMapping("/groups/new")
    public String createGroup(
        @Valid @ModelAttribute("group") GroupDto group,
        BindingResult result
    ) {
        if(result.hasErrors()){
            return "group/create";
        }
        groupService.create(group);
        return "redirect:/groups";
    }
}
