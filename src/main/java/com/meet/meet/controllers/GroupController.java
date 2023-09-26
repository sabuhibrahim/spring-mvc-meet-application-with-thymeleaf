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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.meet.meet.dtos.GroupDto;
import com.meet.meet.exceptions.ForbiddenException;
import com.meet.meet.models.Group;
import com.meet.meet.models.UserEntity;
import com.meet.meet.services.GroupService;
import com.meet.meet.services.UserService;
import com.meet.meet.storage.StorageService;

import jakarta.validation.Valid;

@Controller
public class GroupController extends AbstractController {

    @Autowired
    private GroupService groupService;

    @Autowired
    private UserService userService;

    @Autowired
    private StorageService storageService;

    private String storageBasePath = "groups";
    
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
        @RequestParam("photoFile") MultipartFile photoFile,
        BindingResult result,
        RedirectAttributes redirectAttributes
    ) {
        if(result.hasErrors()){
            return "group/create";
        }
        if(!photoFile.isEmpty()) {
            String fileUrl = storageService.store(photoFile, storageBasePath);
            group.setPhoto(fileUrl);
        }

        group.setOwner(getCurrentDtoUser(userService));

        groupService.create(group);
        redirectAttributes.addFlashAttribute("successMessage", "New group succesfully created");
        return "redirect:/groups";
    }


    @GetMapping("/groups/{id}")
    public String getGroup(
        @PathVariable("id") Long id,
        Model model
    ) {
        UserEntity user = getCurrentUser(userService);
        GroupDto group = groupService.findByIdJoinEvents(id);

        String subscribe = null;

        if(user.getId() != group.getOwner().getId()) {
            subscribe = "Subscribe";
            for (Group g : user.getSubscriptions()) {
                if(g.getId() == group.getId()) {
                    subscribe = "Unsubscribe";
                    break;
                }
            }
        }

        model.addAttribute("user", user);
        model.addAttribute("subscribe", subscribe);
        model.addAttribute("group", group);
        return "group/show";
    }


    @GetMapping("/groups/{id}/update")
    public String updateGroupPage(
        @PathVariable("id") Long id,
        Model model
    ) {
        UserEntity user = getCurrentUser(userService);
        GroupDto group = groupService.findById(id);
        if(group.getOwner().getId() != user.getId()) {
            throw new ForbiddenException("Forbidden page");
        }

        model.addAttribute("user", user);
        model.addAttribute("group", group);
        return "group/update";
    }

    @PostMapping("/groups/{id}/update")
    public String updateGroup(
        @PathVariable("id") Long id,
        @Valid @ModelAttribute("group") GroupDto group,
        @RequestParam("photoFile") MultipartFile photoFile,
        RedirectAttributes redirectAttributes,
        BindingResult result
    ) {
        
        UserEntity user = getCurrentUser(userService);
        GroupDto groupOnUpdate = groupService.findById(id);

        if(groupOnUpdate.getOwner().getId() != user.getId()) {
            throw new ForbiddenException("Forbidden page");
        }

        if(result.hasErrors()){
            return "group/update";
        }
        if(!photoFile.isEmpty()) {
            String fileUrl = storageService.store(photoFile, storageBasePath);
            groupOnUpdate.setPhoto(fileUrl);
        }

        groupOnUpdate.setTitle(group.getTitle());
        groupOnUpdate.setDescription(group.getDescription());

        groupService.update(groupOnUpdate);

        redirectAttributes.addFlashAttribute(
            "successMessage", 
            "Group succesfully updated"
        );

        return "redirect:/groups/" + id;
    }


    @GetMapping("/groups/{id}/subscribe")
    public String subscribe(
        @PathVariable("id") Long id,
        RedirectAttributes redirectAttributes
    ) {
        UserEntity user = getCurrentUser(userService);
        Group group = groupService.findByIdModel(id);
        if(group.getOwner().getId() == user.getId()) {
            throw new ForbiddenException("Forbidden page");
        }
        List<Group> subscriptions = user.getSubscriptions();
        
        boolean unsubscripe = false;
        
        for (Group g : subscriptions) {
            if(g.getId() == group.getId()) {
                unsubscripe = true;
                break;
            }
        }
        if(!unsubscripe) {
            userService.subscribe(user, group);
            redirectAttributes.addFlashAttribute(
                "successMessage", 
                "Successfully subscribe group"
            );
        } else {
            userService.unsubscribe(user, group);
            redirectAttributes.addFlashAttribute(
                "successMessage", 
                "Successfully unsubscribe group"
            );
        }
        return "redirect:/groups/" + id + "?succesMessage=";
    }


    @GetMapping("/groups/{id}/delete")
    public String delete(
        @PathVariable("id") Long id,
        RedirectAttributes redirectAttributes
    ) {
        UserEntity user = getCurrentUser(userService);
        System.out.println(user.toString());
        GroupDto group = groupService.findById(id);
        if(group.getOwner().getId() != user.getId()) {
            throw new ForbiddenException("Forbidden page");
        }
        groupService.delete(group);

        redirectAttributes.addFlashAttribute("successMessage", "Group succesfully deleted");
        return "redirect:/groups";
    }
}
