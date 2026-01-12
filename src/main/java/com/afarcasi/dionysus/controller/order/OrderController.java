package com.afarcasi.dionysus.controller.order;

import com.afarcasi.dionysus.exception.InsufficientTicketsException;
import com.afarcasi.dionysus.exception.TicketCategoryNotFoundException;
import com.afarcasi.dionysus.exception.UserNotFoundException;
import com.afarcasi.dionysus.model.dto.order.OrderViewDTO;
import com.afarcasi.dionysus.model.dto.order.TicketSaleDTO;
import com.afarcasi.dionysus.service.order.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
@Tag(name = "Order Management", description = "Endpoints for ticket sales and order handling.")
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/sell")
    @Operation(summary = "Sell tickets (create an order)")
    public ResponseEntity<OrderViewDTO> sellTickets(@Valid @RequestBody @NotNull TicketSaleDTO dto) {
        try {
            OrderViewDTO order = orderService.sellTickets(dto);
            return new ResponseEntity<>(order, HttpStatus.CREATED);
        } catch (UserNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (TicketCategoryNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (InsufficientTicketsException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get order by ID")
    public ResponseEntity<OrderViewDTO> getOrderById(@PathVariable Long id) {
        Optional<OrderViewDTO> order = orderService.findById(id);
        if (order.isPresent()) {
            return new ResponseEntity<>(order.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping
    @Operation(summary = "Get all orders")
    public ResponseEntity<List<OrderViewDTO>> getAllOrders() {
        List<OrderViewDTO> orders = orderService.findAll();
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }

    @GetMapping("/customer/{customerId}")
    @Operation(summary = "Get all orders for a customer")
    public ResponseEntity<List<OrderViewDTO>> getOrdersByCustomer(@PathVariable Long customerId) {
        List<OrderViewDTO> orders = orderService.findByCustomerId(customerId);
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }
}
