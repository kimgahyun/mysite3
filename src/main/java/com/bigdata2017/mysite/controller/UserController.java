package com.bigdata2017.mysite.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.bigdata2017.mysite.service.UserService;
import com.bigdata2017.mysite.vo.UserVo;
import com.bigdata2017.security.Auth;
import com.bigdata2017.security.AuthUser;

@Controller
@RequestMapping( "/user" )
public class UserController {
	@Autowired
	private UserService userService;
	
	@RequestMapping( value="/join", method=RequestMethod.GET )
	public String join() {
		return "user/join";
	}

	@RequestMapping( value="/join", method=RequestMethod.POST )
	public String join( @ModelAttribute UserVo userVo ) {
		userService.join(userVo);
		return "redirect:/user/joinsuccess";
	}
	
	@RequestMapping( "/joinsuccess" )
	public String joinSuccess() {
		return "user/joinsuccess";
	}
	
	@RequestMapping(value="/login", method=RequestMethod.GET)
	public String login() {
		return "user/login";
	}
	
	@Auth
	@RequestMapping( value="/modify", method=RequestMethod.GET )
	public String modify(
		@AuthUser UserVo authUser,
		Model model) {
		UserVo userVo = userService.getUser( authUser.getNo() );
		model.addAttribute("userVo", userVo);
		return "user/modify";
	}
	
	@Auth(role=Auth.Role.USER)
	@RequestMapping(value="/modify", method=RequestMethod.POST)
	public String modify(
		@AuthUser UserVo authUser,
		@ModelAttribute UserVo userVo) {
		userVo.setNo(authUser.getNo());
		userService.modifyUser(userVo);
		
		//authUser 변경(화면변경)
		authUser.setName(userVo.getName());
		
		return "redirect:/user/modify?update=success";
	}
	
}
