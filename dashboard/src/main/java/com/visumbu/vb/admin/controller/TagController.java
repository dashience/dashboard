/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visumbu.vb.admin.controller;

import com.visumbu.vb.admin.service.TagService;
import com.visumbu.vb.bean.TagWidgetBean;
import com.visumbu.vb.model.Tag;
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
public class TagController {

    @Autowired
    private TagService tagService;

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
    
    @RequestMapping(value = "widgetTag/{tagWidgetId}", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody
    List getWidgetTagById(HttpServletRequest request, HttpServletResponse response, @PathVariable Integer tagWidgetId) {
        return tagService.getWidgetTagById(tagWidgetId);
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
    
//    @RequestMapping(value = "widgetTag/{widgetTagId}", method = RequestMethod.DELETE, produces = "application/json")
//    public @ResponseBody
//    WidgetTag deleteWidgetTag(HttpServletRequest request, HttpServletResponse response, @PathVariable Integer widgetTagId) {
//        return tagService.deleteWidgetTag(widgetTagId);
//    }
}
