package com.example.store;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class StoreController {

    private final ProductRepository productRepository;
    private final BillRepository billRepository;
    private final BillingService billingService;

    public StoreController(ProductRepository productRepository, BillRepository billRepository,
                           BillingService billingService) {
        this.productRepository = productRepository;
        this.billRepository = billRepository;
        this.billingService = billingService;
    }

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("products", productRepository.findAll());
        model.addAttribute("recentBills", billRepository.findAllByOrderByCreatedAtDesc().stream().limit(8).toList());
        model.addAttribute("product", new Product());
        model.addAttribute("billForm", new CreateBillForm());
        return "index";
    }

    @PostMapping("/products")
    public String addProduct(@Valid @ModelAttribute("product") Product product, BindingResult bindingResult,
                             RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("productError", bindingResult.getAllErrors().get(0).getDefaultMessage());
            return "redirect:/#products";
        }
        productRepository.save(product);
        redirectAttributes.addFlashAttribute("success", "Product added.");
        return "redirect:/#products";
    }

    @PostMapping("/bills")
    public String createBill(@ModelAttribute CreateBillForm billForm, RedirectAttributes redirectAttributes) {
        Bill bill = billingService.createBill(billForm.getQuantities());
        return "redirect:/bills/" + bill.getId();
    }

    @GetMapping("/bills/{id}")
    public String viewBill(@PathVariable Long id, Model model) {
        Bill bill = billRepository.findById(id)
                .orElseThrow(() -> new BillNotFoundException(id));
        model.addAttribute("bill", bill);
        return "bill";
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleInvalidBill(IllegalArgumentException exception, Model model) {
        model.addAttribute("message", exception.getMessage());
        return "error";
    }

    @ExceptionHandler(BillNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleBillNotFound(BillNotFoundException exception, Model model) {
        model.addAttribute("message", exception.getMessage());
        return "error";
    }

    private static class BillNotFoundException extends RuntimeException {
        private BillNotFoundException(Long id) {
            super("Bill #" + id + " was not found.");
        }
    }
}
