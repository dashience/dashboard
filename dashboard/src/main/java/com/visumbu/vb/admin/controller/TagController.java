/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visumbu.vb.admin.controller;

import com.visumbu.vb.admin.service.TagService;
import com.visumbu.vb.admin.service.UserService;
import com.visumbu.vb.bean.TagWidgetBean;
import com.visumbu.vb.controller.BaseController;
import com.visumbu.vb.model.TabWidget;
import com.visumbu.vb.model.Tag;
import com.visumbu.vb.model.VbUser;
import com.visumbu.vb.model.WidgetTag;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author deldot
 */
@Controller
@RequestMapping("tag")
public class TagController extends BaseController {

    @Autowired
    private TagService tagService;
    @Autowired
    private UserService userService;

    @RequestMapping(method = RequestMethod.POST, produces = "application/json")
    public @ResponseBody
    Tag create(HttpServletRequest request, HttpServletResponse response, @RequestBody Tag tag) {
        return tagService.create(tag);
    }

    @RequestMapping(method = RequestMethod.PUT, produces = "application/json")
    public @ResponseBody
    Tag update(HttpServletRequest request, HttpServletResponse response, @RequestBody Tag tag) {
        return tagService.update(tag);
    }

    @RequestMapping(method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody
    List read(HttpServletRequest request, HttpServletResponse response) {
        return tagService.read();
    }

    @RequestMapping(value = "{id}", method = RequestMethod.DELETE, produces = "application/json")
    public @ResponseBody
    Tag delete(HttpServletRequest request, HttpServletResponse response, @PathVariable Integer id) {
        return tagService.delete(id);
    }

    @RequestMapping(method = RequestMethod.DELETE, produces = "application/json")
    public @ResponseBody
    Tag delete(HttpServletRequest request, HttpServletResponse response, @RequestBody Tag tag) {
        return tagService.delete(tag);
    }

    @RequestMapping(value = "widgetTag", method = RequestMethod.POST, produces = "application/json")
    public @ResponseBody
    WidgetTag createWidgetTag(HttpServletRequest request, HttpServletResponse response, @RequestBody TagWidgetBean tagWidgetBean) {
        return tagService.createWidgetTag(tagWidgetBean);
    }

    @RequestMapping(value = "widgetTag", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody
    List getWidgetTag(HttpServletRequest request, HttpServletResponse response) {
        return tagService.getWidgetTag();
    }

//    @RequestMapping(value = "widgetTag/{tagId}", method = RequestMethod.GET, produces = "application/json")
//    public @ResponseBody
//    List getWidgetTagById(HttpServletRequest request, HttpServletResponse response, @PathVariable Integer tagId) {
//        return tagService.getWidgetTagById(tagId);
//    }
    @RequestMapping(value = "widgetTag/{tagName}", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody
    List getWidgetTagByName(HttpServletRequest request, HttpServletResponse response, @PathVariable String tagName) {
        VbUser user = userService.findByUsername(getUser(request));
        return tagService.getWidgetTagByName(tagName, user);
    }
    
    @RequestMapping(value = "widgetTag/{tagName}/{userId}", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody
    List getWidgetTagByUserId(HttpServletRequest request, HttpServletResponse response, @PathVariable String tagName, @PathVariable Integer userId) {
        return tagService.getWidgetTagByUserId(tagName, userId);
    }

    @RequestMapping(value = "widgetTag/{widgetTagId}", method = RequestMethod.DELETE, produces = "application/json")
    public @ResponseBody
    WidgetTag deleteWidgetTag(HttpServletRequest request, HttpServletResponse response, @PathVariable Integer widgetTagId) {
        return tagService.deleteWidgetTag(widgetTagId);
    }

    @RequestMapping(value = "selectedTag", method = RequestMethod.POST, produces = "application/json")
    public @ResponseBody
    WidgetTag selectWidgetTag(HttpServletRequest request, HttpServletResponse response, @RequestBody TagWidgetBean tagWidgetBean) {
        return tagService.selectWidgetTag(tagWidgetBean);
    }

    @RequestMapping(value = "removeFav/{widgetId}", method = RequestMethod.POST, produces = "application/json")
    public @ResponseBody
    Boolean removeFav(HttpServletRequest request, HttpServletResponse response, @PathVariable Integer widgetId) {
        String username = getUser(request);
        VbUser user = userService.findByUsername(username);

        return tagService.removeFav(widgetId, user);
    }

    @RequestMapping(value = "setFav/{widgetId}", method = RequestMethod.POST, produces = "application/json")
    public @ResponseBody
    Boolean setFav(HttpServletRequest request, HttpServletResponse response, @PathVariable Integer widgetId) {
        String username = getUser(request);
        VbUser user = userService.findByUsername(username);

        return tagService.setFav(widgetId, user);
    }

    @RequestMapping(value = "getAllFav", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody
    List<TabWidget> getAllFav(HttpServletRequest request, HttpServletResponse response) {
        String username = getUser(request);
        VbUser user = userService.findByUsername(username);
        return tagService.getAllFav(user);
    }

    @RequestMapping(value = "favWidgetUpdateOrder/{favId}", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody
    Object updateFavWidgetOrder(HttpServletRequest request, HttpServletResponse response, @PathVariable Integer favId) {
        String widgetOrder = request.getParameter("widgetOrder");
        tagService.updateFavWidgetOrder(favId, widgetOrder);
        return null;
    }

//    @RequestMapping(value = "widgetTag/{widgetTagId}", method = RequestMethod.DELETE, produces = "application/json")
//    public @ResponseBody
//    WidgetTag deleteWidgetTag(HttpServletRequest request, HttpServletResponse response, @PathVariable Integer widgetTagId) {
//        return tagService.deleteWidgetTag(widgetTagId);
//    }
}
