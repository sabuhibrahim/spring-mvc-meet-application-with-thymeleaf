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

import com.meet.meet.dtos.EventDto;
import com.meet.meet.dtos.GroupDto;
import com.meet.meet.exceptions.ForbiddenException;
import com.meet.meet.models.Event;
import com.meet.meet.models.UserEntity;
import com.meet.meet.services.EventService;
import com.meet.meet.services.GroupService;
import com.meet.meet.services.UserService;
import com.meet.meet.storage.StorageService;

import jakarta.validation.Valid;

@Controller
public class EventController extends AbstractController {
    @Autowired
    private EventService eventService;

    @Autowired
    private UserService userService;

    @Autowired
    private StorageService storageService;

    @Autowired
    private GroupService groupService;

    private String storageBasePath = "events";

    @GetMapping("/events")
    public String eventList(
        @RequestParam(value = "groupId", required = false) Long groupId,
        @RequestParam(value = "pageNo", required = false) Integer pageNo,
        @RequestParam(value = "pageSize", required = false) Integer pageSize,
        Model model
    ) {
        if(pageNo == null) pageNo = 1;
        if(pageSize == null) pageSize = 10;
        
        pageNo--;
        
        GroupDto group = null;

        if(groupId != null) {
            group = groupService.findById(groupId);
        }

        Page<EventDto> events = eventService.findAll(pageNo, pageSize, groupId);

        int totalPages = events.getTotalPages();
        if(totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                .boxed()
                .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
            model.addAttribute("nextPageNumber",  pageNo + 2);
            model.addAttribute("prevPageNumber", pageNo);
        }

        model.addAttribute("group", group);
        model.addAttribute("user", getCurrentUser(userService));
        model.addAttribute("events", events);

        return "event/list";
    }

    @GetMapping("/events/new")
    public String newEvent(
        @RequestParam(value = "groupId") Long groupId,
        Model model
    ) {
        GroupDto group = groupService.findById(groupId);
        UserEntity user = getCurrentUser(userService);

        if(group.getOwner().getId() != user.getId()) {
            throw new ForbiddenException("Forbidden page");
        }
        
        EventDto event = new EventDto();

        model.addAttribute("user", user);
        model.addAttribute("group", group);
        model.addAttribute("event", event);

        return "event/create";
    }

    @PostMapping("/events/new")
    public String createEvent(
        @RequestParam(value = "groupId") Long groupId,
        @Valid @ModelAttribute("event") EventDto event,
        @RequestParam("photoFile") MultipartFile photoFile,
        BindingResult result,
        RedirectAttributes redirectAttributes
    ) {
        GroupDto group = groupService.findById(groupId);
        UserEntity user = getCurrentUser(userService);

        if(group.getOwner().getId() != user.getId()) {
            throw new ForbiddenException("Forbidden page");
        }

        if(!photoFile.isEmpty()) {
            String fileUrl = storageService.store(photoFile, storageBasePath);
            event.setPhoto(fileUrl);
        }

        event.setGroup(group);
        eventService.create(event);

        redirectAttributes.addFlashAttribute(
            "successMessage", 
            "New event succesfully created"
        );

        return "redirect:/events?groupId=" + groupId;
    }


    @GetMapping("/events/{id}")
    public String getEvent(
        @RequestParam(value = "groupId", required = false) Long groupId,
        @PathVariable("id") Long id,
        Model model
    ) {

        UserEntity user = getCurrentUser(userService);
        EventDto event = eventService.findByIdJoinParticipaters(id);
        String participate = "Participate";

        for(Event e: user.getParticipations()) {
            if(event.getId() == e.getId()) {
                participate = "Cancel participation";
            }
        }
        
        model.addAttribute("user", user);
        model.addAttribute("participate", participate);
        model.addAttribute("event", event);

        return "event/show";
    }


    @GetMapping("/events/{id}/participate")
    public String participateEvent(
        @PathVariable("id") Long id,
        @RequestParam(name = "groupId", required = false) Long groupId,
        RedirectAttributes redirectAttributes
    ){

        UserEntity user = getCurrentUser(userService);
        Event event = eventService.findByIdModel(id);

        List<Event> participations = user.getParticipations();
        
        boolean isParticipate = false;

        for(Event e: participations) {
            if(e.getId() == event.getId()) {
                isParticipate = true;
                break;
            }
        }

        if(isParticipate) {
            userService.unparticipate(user, event);
            redirectAttributes.addFlashAttribute(
                "successMessage",
                "Successfully unparticipate event"
            );
        } else {
            userService.participate(user, event);
            redirectAttributes.addFlashAttribute(
                "successMessage",
                "Successfully participate event"
            );
        }

        String redirect = "redirect:/events/" + id;

        if (groupId != null) {
            redirect += "?groupId=" + groupId;
        }
        return redirect;
    }


    @GetMapping("/events/{id}/update")
    public String updatePage(
        @PathVariable("id") Long id,
        @RequestParam("groupId") Long groupId,
        Model model
    ) {
        GroupDto group = groupService.findById(groupId);
        UserEntity user = getCurrentUser(userService);

        if(group.getOwner().getId() != user.getId()) {
            throw new ForbiddenException("Forbidden page");
        }

        EventDto event = eventService.findById(id);

        model.addAttribute("event", event);
        model.addAttribute("user", user);
        model.addAttribute("group", group);
        
        return "event/update";
    }


    @PostMapping("/events/{id}/update")
    public String updateEvent(
        @PathVariable("id") Long id,
        @RequestParam("groupId") Long groupId,
        @Valid @ModelAttribute("event") EventDto event,
        @RequestParam("photoFile") MultipartFile photoFile,
        RedirectAttributes redirectAttributes
    ) {
        
        GroupDto group = groupService.findById(groupId);
        UserEntity user = getCurrentUser(userService);

        if(group.getOwner().getId() != user.getId()) {
            throw new ForbiddenException("Forbidden page");
        }

        EventDto eventOnUpdate = eventService.findById(id);

        if(!photoFile.isEmpty()) {
            String fileUrl = storageService.store(photoFile, storageBasePath);
            eventOnUpdate.setPhoto(fileUrl);
        }
        eventOnUpdate.setTitle(event.getTitle());
        eventOnUpdate.setDescription(event.getDescription());
        eventOnUpdate.setStartTime(event.getStartTime());
        eventOnUpdate.setEndTime(event.getEndTime());

        eventService.update(eventOnUpdate);

        redirectAttributes.addFlashAttribute(
            "successMessage", 
            "Event succesfully updated"
        );

        return "redirect:/events/" + id + "?groupId=" + groupId;
    }


    @GetMapping("/events/{id}/delete")
    public String deleteEvent(
        @PathVariable("id") Long id,
        @RequestParam("groupId") Long groupId,
        RedirectAttributes redirectAttributes
    ) {

        GroupDto group = groupService.findById(groupId);
        UserEntity user = getCurrentUser(userService);

        if(group.getOwner().getId() != user.getId()) {
            throw new ForbiddenException("Forbidden page");
        }

        EventDto event = eventService.findByIdAndGroupId(id, groupId);

        eventService.delete(event);

        redirectAttributes.addFlashAttribute("successMessage", "Event succesfully deleted");

        return "redirect:/events?groupId" + groupId;
    }

}
