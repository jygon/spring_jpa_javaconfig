package com.alugagem.controllers;

import java.io.IOException;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.alugagem.daos.ProductDAO;
import com.alugagem.entities.BookType;
import com.alugagem.entities.Product;

@Controller
@Transactional
@RequestMapping("/produtos")
public class ProductsController {

	@Autowired
	private ProductDAO products;
	
	@InitBinder
    protected void initBinder(WebDataBinder binder) {
		//binder.setValidator(new ProductValidator());
		System.err.println("1");
    }	
	

	@RequestMapping(method=RequestMethod.POST)
	@CacheEvict(value="lastProducts", allEntries=true)
	public ModelAndView save(MultipartFile summary,@ModelAttribute("product") @Valid Product product,BindingResult bindingResult,RedirectAttributes redirectAttributes) throws IOException{
		System.err.println("1");
		if(bindingResult.hasErrors()){
			return form(product);
		}
		
		redirectAttributes.addFlashAttribute("success", "Produto cadastrado com sucesso");
		return new ModelAndView("redirect:produtos");
	}
	
	@RequestMapping("/form")
	public ModelAndView form(@ModelAttribute Product product){
		System.err.println("1");
		ModelAndView modelAndView = new ModelAndView("products/form");
		modelAndView.addObject("bookTypes", BookType.values());
		return modelAndView;
	}
	
	@RequestMapping(method=RequestMethod.GET)
	@Cacheable(value="lastProducts")
	public ModelAndView list(){
		System.err.println("1");
		ModelAndView modelAndView = new ModelAndView("products/list");
		modelAndView.addObject("products", products.list());
		return modelAndView;
	}
	
	
	@RequestMapping("/{id}")
	public ModelAndView show(@PathVariable("id") Integer id){
		System.err.println("1");
		ModelAndView modelAndView = new ModelAndView("products/show");
		Product product = products.find(id);
		modelAndView.addObject("product", product);
		return modelAndView;
	}
	
}
